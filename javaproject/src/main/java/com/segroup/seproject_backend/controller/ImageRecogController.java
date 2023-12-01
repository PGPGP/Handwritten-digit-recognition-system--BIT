package com.segroup.seproject_backend.controller;

import com.segroup.seproject_backend.data_item.ImageRecogF2JWebItem;
import com.segroup.seproject_backend.data_item.ImageRecogJ2FWebItem;
import com.segroup.seproject_backend.data_item.ImageRecogJ2PWebItem;
import com.segroup.seproject_backend.data_item.ImageRecogP2JWebItem;
import com.segroup.seproject_backend.repository.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.commons.io.FileUtils.readFileToString;

@Controller
public class ImageRecogController {

    //用于获取配置文件
    @Autowired
    private Environment environment;

    //数据库
    @Autowired
    private ProjectRepo projectRepo;

    // 用于发送http请求
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/use/image")
    @ResponseBody
    @CrossOrigin
    public ImageRecogJ2FWebItem handleFileUpload(ImageRecogF2JWebItem imageRecogF2JWebItem) throws IOException, InterruptedException {
        // 保存文件
        Date date = new Date();
        String fileName = date.getTime() + imageRecogF2JWebItem.getPostfix();
        String filePathName = environment.getProperty("my-config.image-recog.save-path") + fileName;
        File file = new File(filePathName);
        //File parentfile = file.getParentFile();
        //System.out.println(parentfile.exists());
        if (!(file.createNewFile())) {
            throw new IOException("ImageRecogController.handleFileUpload: 文件已存在！");
        }
        imageRecogF2JWebItem.getFile().transferTo(file);
        System.out.println("成功接收到文件，保存在" + filePathName);

        // 保存使用记录
        projectRepo.recordOneUse(date, 0); // 之后要修改，获取模型id

        // 传给python模型
        // String predictScriptPathname = environment.getProperty("my-config.global.pyscript-path") + "predict_old.py";
        // String modelPathname = environment.getProperty("my-config.global.model-path");
        // ArrayList<String> command = new ArrayList(Arrays.asList("python", predictScriptPathname, "--image", filePathName, "--model", modelPathname));
        // ProcessBuilder processBuilder = new ProcessBuilder(command);
        // processBuilder.inheritIO();
        // System.out.println("----------------预测开始----------------");
        // Process process = processBuilder.start();
        // process.waitFor();
        // System.out.println("----------------预测完成----------------");

        // 传给python后端
        ImageRecogJ2PWebItem imageRecogJ2PWebItem = new ImageRecogJ2PWebItem(filePathName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file_path", imageRecogJ2PWebItem.getFile_path());
        HttpEntity<MultiValueMap<String, Object>> datas = new HttpEntity<>(form, headers);

        String url = environment.getProperty("my-config.global.python-backend-url") + "/predict";

        ImageRecogP2JWebItem recogResult = restTemplate.postForObject(url, datas, ImageRecogP2JWebItem.class);

        // 接收结果并返回
        String resultImagePathName = recogResult.getFile_path();
        ImageRecogJ2FWebItem imageRecogJ2FWebItem = new ImageRecogJ2FWebItem();
        //  读取图片和文本文件
        File resultImage = new File(resultImagePathName);
        byte[] imageData = readFileToByteArray(resultImage);
        //  写入结果对象
        imageRecogJ2FWebItem.setFile(Base64.getEncoder().encodeToString(imageData));
        imageRecogJ2FWebItem.setText(recogResult.getText());

        return imageRecogJ2FWebItem;
    }
}
