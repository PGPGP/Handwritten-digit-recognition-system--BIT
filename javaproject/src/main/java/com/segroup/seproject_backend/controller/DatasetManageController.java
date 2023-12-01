package com.segroup.seproject_backend.controller;

import java.io.File;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segroup.seproject_backend.data_item.*;
import com.segroup.seproject_backend.repository.ProjectRepo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class DatasetManageController {
    @Autowired
    private ProjectRepo projectRepo;
    private JdbcTemplate jdbc;
    //对应接口：删除数据集
    @PostMapping("/manage/delete_dataset")
    @ResponseBody
    @CrossOrigin
    public ResultWebItem handleDeleteDataset(DeleteDatasetWebItem deletedatasetwebitem) {
//        System.out.println("已获取删除数据集请求");
//        System.out.println("用户ID"+deletedatasetwebitem.getUser_id());
//        System.out.println("数据集ID"+deletedatasetwebitem.getDataset_id());

        String datasetIdString = deletedatasetwebitem.getDataset_id();
        long datasetId ;
        datasetId = Long.parseLong(datasetIdString);
            // 进一步处理 numericValue
//        ResultWebItem result = null;
        System.out.println("待删除的数据集id：" + datasetId);
        String res = projectRepo.deleteDataBaseItem(datasetId);
        System.out.println("返回结果："+res);
        ResultWebItem result = new ResultWebItem(res);
        return result;
    }

    @Autowired
    private RestTemplate restTemplate;
    //对应接口：上传数据集
    @PostMapping(value = "/manage/upload_dataset")
    @ResponseBody
    @CrossOrigin
    public ResultWebItem handleUploadDataset(UploadDatasetF2JWebItem uploaddatasetf2jwebitem) {
        // 需要你们来实现
        ResultWebItem result = new ResultWebItem("failed");

        System.out.println("已获取提交数据集请求");
        System.out.println("提交数据集是用户ID为"+uploaddatasetf2jwebitem.getUser_id());
        System.out.println("提交数据集名称为"+uploaddatasetf2jwebitem.getDataset_name());

        // 获取文件列表
        List<MultipartFile> fileList = uploaddatasetf2jwebitem.getImages();
        List<String> imagePaths = new ArrayList<>();

        for (MultipartFile file : fileList) {
            // 得到上传文件后缀
            try {
                String originalName = file.getOriginalFilename();
                String ext = "." + FilenameUtils.getExtension(originalName);
                // 新生成的文件名称
                String fileName = FilenameUtils.getBaseName(originalName) + ext;
                // 得到新的文件 File 对象
                File targetFile = new File("D:\\image_store", fileName);
                // 开始复制文件
                FileUtils.writeByteArrayToFile(targetFile, file.getBytes());
                // 将图片路径保存到列表中
                imagePaths.add(targetFile.getAbsolutePath());
            }catch (IOException e) {
                // 处理 IOException 异常
                e.printStackTrace(); // 这里可以根据实际情况进行日志记录或其他处理
            }
        }

// 将数据传给 Python 后端
        String pythonBackendUrl = "http://localhost:8088/form_dataset";
        ImagePathJ2PWebItem formData = new ImagePathJ2PWebItem(uploaddatasetf2jwebitem.getDataset_name(), imagePaths);

// 构造 MultiValueMap
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("dataset_name", formData.getDataset_name());  // 添加 dataset_name

// 将 image_paths 作为单独的键值对添加到 form 中，而不是使用 addAll
        for (String imagePath : formData.getImage_paths()) {
            form.add("image_paths", imagePath);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(form, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(pythonBackendUrl, request, String.class);

        // 处理 Python 后端的响应
        String responseBody = response.getBody();

// 将 JSON 字符串转换为 Map<String, Object>
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> jsonMap = null;
        try {
            jsonMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            // 处理 JSON 解析异常
            e.printStackTrace();
            // 在这里你可以根据实际需求进行其他处理，例如返回默认值或向用户显示错误信息
        }

// 从 Map 中获取 "image_paths" 对应的值，然后将其转换为 List<String>
        List<String> processedImagePaths = (List<String>) jsonMap.get("image_paths");

        // 替换之前数据集中各图片保存到本地的路径数组的内容
        for (int i = 0; i < imagePaths.size(); i++) {
            if (i < processedImagePaths.size()) {
                String processedPath = processedImagePaths.get(i);
                // 这里你可以根据需要进行其他处理
                // System.out.println("处理后的图片路径：" + processedPath);
                // 替换原始路径
//                fileList.get(i).transferTo(new File(processedPath));
                imagePaths.set(i, processedPath);
            }
        }


        //存datasets
        List<String>labels = uploaddatasetf2jwebitem.getLabels();
        long user_id = Long.parseLong(uploaddatasetf2jwebitem.getUser_id());

        String dataset_name = uploaddatasetf2jwebitem.getDataset_name();
        List<MultipartFile> fileLists = uploaddatasetf2jwebitem.getImages();
        int image_num = fileLists.size();//获取数据量大小

        long dateset_id = projectRepo.uploaddatasetsDB(user_id,dataset_name,image_num);
        if(dateset_id == -1)
        {
            result.setResult("failed");
            return result;
        }

        System.out.println(dateset_id );
        //
        boolean res = projectRepo.uploadimagesDB(imagePaths,labels,dateset_id);

        if(res){
            result.setResult("successful");
        }else {
            result.setResult("failed");
        }

        return result;
    }

}
