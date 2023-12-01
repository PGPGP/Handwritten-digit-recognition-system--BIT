package com.segroup.seproject_backend.data_item;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadDatasetF2JWebItem {
    private String user_id;
    private String dataset_name;
    private List<MultipartFile> images;
    private List<String> labels;
}
