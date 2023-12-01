package com.segroup.seproject_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segroup.seproject_backend.controller.ModelTrainController;
import com.segroup.seproject_backend.data_item.TrainFinishP2JWebItem;
import com.segroup.seproject_backend.data_item.TrainInfoWebItem;
import com.segroup.seproject_backend.data_item.TrainStartF2JWebItem;
import com.segroup.seproject_backend.data_item.WebSocketItem;
import jakarta.websocket.Session;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashMap;

// 训练模型时的websocket客户端，用于和python后端连接
@Component
public class TrainModelJ2PClient extends WebSocketClient {
    // 初始化时，连接python后端
    public TrainModelJ2PClient() {
        super(URI.create("ws://localhost:8089/train"));
        this.setConnectionLostTimeout(0);
    }

    // public ModelTrainController controller;

    //用户id和controller间的对应关系
    public HashMap<Long, ModelTrainController> userMap = new HashMap<>();

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("WebSocket：和python后端连接成功。");
    }

    @Override
    public void onMessage(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            WebSocketItem<Object> object = mapper.readValue(text, new TypeReference<>(){});
            long user_id = object.getUser_id();;
            if(object.getType().equals("info")) {
                TrainInfoWebItem infoItem = mapper.readValue(text, new TypeReference<WebSocketItem<TrainInfoWebItem>>(){}).getObject();
                ModelTrainController controller = userMap.get(user_id);
                controller.onPInfo(user_id, infoItem);
            }
            else if (object.getType().equals("finish")) {
                TrainFinishP2JWebItem finishItem = mapper.readValue(text, new TypeReference<WebSocketItem<TrainFinishP2JWebItem>>(){}).getObject();
                ModelTrainController controller = userMap.get(user_id);
                controller.onPFinish(user_id, finishItem);
            }
            else {
                System.out.println("WebSocket错误：python后端发送的消息格式出错");
            }
        }
        catch (Exception e) {
            System.out.println("出现异常：");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("WebSocket：和python后端连接关闭");
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }

    // 若没有连接/连接以断开，尝试连接
    public void tryConnect() {
        if(!getReadyState().equals(ReadyState.OPEN)) {
            if(getReadyState().equals(ReadyState.NOT_YET_CONNECTED)) {
                connect();
            }
            else {
                reconnect();
            }
            while(!getReadyState().equals(ReadyState.OPEN)) {
                // System.out.println("连上了，但没完全连上");
            }
        }
    }
}
