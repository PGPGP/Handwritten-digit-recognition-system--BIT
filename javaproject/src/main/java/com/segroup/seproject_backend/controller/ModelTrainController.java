package com.segroup.seproject_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segroup.seproject_backend.config.TrainModelF2JEndpoint;
import com.segroup.seproject_backend.config.TrainModelJ2PClient;
import com.segroup.seproject_backend.data_item.*;
import com.segroup.seproject_backend.repository.ProjectRepo;
import jakarta.websocket.Session;
import lombok.NoArgsConstructor;
import org.java_websocket.WebSocket;
import org.java_websocket.enums.ReadyState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

// 专门用于训练模型
@Component
@NoArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class ModelTrainController {

    // 与前端通信
    // private TrainModelF2JEndpoint f2jEndpoint;
    public Session session;

    // 与python后端通信
    static private TrainModelJ2PClient j2pClient = new TrainModelJ2PClient();

    // 操作数据库的类

    static private ProjectRepo repo;

    @Autowired
    public void setRepo(ProjectRepo _repo) {
        ModelTrainController.repo = _repo;
    }

    //用于获取配置文件
    // @Autowired
    // private Environment environment;

    @Value("${my-config.dataset-file-save-path}")
    private String dataset_file_path;

    // 正在训练的模型的数据类
    // 同时，其是否为null表明是否有训练任务正在进行
    private ModelDBItem model = null;

    // 前端开始训练，调用此函数
    public void onFStart(long user_id, TrainStartF2JWebItem body) throws IOException, InterruptedException {
        if(model != null) {
            System.out.println("错误：有模型正在训练中");
            return;
        }
//        if(j2pClient.isOpen()) {
//            System.out.println("876trdfyujhgfdszxcvbngfd");
//            return;
//        }

        // 实例化模型的数据类
        model = new ModelDBItem();
        model.setUser_id(body.getUser_id());
        model.setModel_name(body.getModel_name());
        model.setDataset_id(body.getDataset_id());
        model.setIs_active(0);
        model.setModel_create_date(new Date());
        model.setModel_activate_date(null);

        // 获取数据集内容
        long dataset_id = body.getDataset_id();
        List<ImageDBItem> image_list = repo.findImagesByDatasetId(dataset_id);
        String filePathName;

        if(dataset_id == 0) {
            // mnist数据集
            filePathName = "mnist";
        }
        else {
            // 创建数据集文件
            Date date = new Date();
            filePathName = dataset_file_path + "dataset" + date.getTime() + ".txt";
            System.out.println(filePathName);
            File file = new File(filePathName);
            if (!(file.createNewFile())) {
                throw new IOException("ModelTrainController.onFStart: 无法创建文件，文件已存在！");
            }
            FileWriter fileWriter = new FileWriter(file);
            for (ImageDBItem image: image_list) {
                String write_str = String.format("%s, %d\n", image.getImage_path(), image.getLabel());
                fileWriter.append(write_str);
            }
            fileWriter.close();
            System.out.println("创建数据集文件成功。");

        }

        System.out.println(filePathName);

        // 准备传递给python后端的数据类，并将其序列化
        TrainStartJ2PWebItem toPData = new TrainStartJ2PWebItem(
                body.getModel_name(),
                filePathName,
                body.getRatio(),
                body.getEpoch(),
                body.getBatch_size(),
                body.getLearning_rate()
            );
        WebSocketItem<TrainStartJ2PWebItem> toPItem = new WebSocketItem<>(user_id, "start", toPData);
        ObjectMapper mapper = new ObjectMapper();
        String toPStr = mapper.writeValueAsString(toPItem);

        System.out.println("onFStart-toPStr:");
        System.out.println(toPStr);

        // 发送信息给python后端
        j2pClient.userMap.put(user_id, this);
        j2pClient.tryConnect();

        System.out.println("onFStart-toPStr:");
        System.out.println(toPStr);
        j2pClient.send(toPStr);
    }

    // 前端停止训练，调用此函数
    public void onFStop(long user_id) throws JsonProcessingException, InterruptedException {
        // 此时，已经连接python后端
        if(j2pClient.isClosed()) {
            System.out.println("0987ytfghjitrdcvbnjuytf");
            return;
        }

        // 直接将消息转发到python后端
        WebSocketItem<Object> toPItem = new WebSocketItem<>(user_id, "stop", null);
        ObjectMapper mapper = new ObjectMapper();
        String toPStr = mapper.writeValueAsString(toPItem);

        j2pClient.userMap.put(user_id, this);
        j2pClient.tryConnect();

        System.out.println("onFStop-toPStr:");
        System.out.println(toPStr);
        j2pClient.send(toPStr);
    }

    // python后端发来训练信息，调用此函数
    public void onPInfo(long user_id, TrainInfoWebItem body) throws IOException {
        //更新模型数据类的准确率
        model.setTrain_accuracy(body.getTest_acc());

        // 将消息转发到前端
        WebSocketItem<TrainInfoWebItem> toFItem = new WebSocketItem<>(user_id, "info", body);
        ObjectMapper mapper = new ObjectMapper();
        String toFStr = mapper.writeValueAsString(toFItem);
        System.out.println("onPInfo-toFStr:");
        System.out.println(toFStr);
        TrainModelF2JEndpoint.send(toFStr, session);
    }

    // python后端训练完成，调用此函数
    public void onPFinish(long user_id, TrainFinishP2JWebItem body) throws IOException {
        String model_path = body.getPath();
        //记录模型路径
        model.setModel_path(model_path);
        //在数据库中保存模型
        repo.insertModel(model);
        //断开和后端的连接
        // j2pClient.close();
        //清空模型数据类
        model = null;

        // 将消息发送到前端
        WebSocketItem<Object> toFItem = new WebSocketItem<>(user_id, "finish", null);
        ObjectMapper mapper = new ObjectMapper();
        String toFStr = mapper.writeValueAsString(toFItem);
        System.out.println("onPFinish-toFStr:");
        System.out.println(toFStr);
        TrainModelF2JEndpoint.send(toFStr, session);
    }
}

