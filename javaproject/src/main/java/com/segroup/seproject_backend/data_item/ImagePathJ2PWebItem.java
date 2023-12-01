package com.segroup.seproject_backend.data_item;
import lombok.Data;

import java.util.List;
@Data
public class ImagePathJ2PWebItem {
    private String dataset_name;
    private List<String> image_paths;

    // 构造函数
    public ImagePathJ2PWebItem(String dataset_name, List<String> image_paths) {
        this.dataset_name = dataset_name;
        this.image_paths = image_paths;
    }
}
