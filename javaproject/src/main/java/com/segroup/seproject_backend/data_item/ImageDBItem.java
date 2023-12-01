package com.segroup.seproject_backend.data_item;

import lombok.Data;

@Data
public class ImageDBItem {
    private long image_id;
    private String image_path;
    private int label;
}
