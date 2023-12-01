package com.segroup.seproject_backend.data_item;

import lombok.Data;

import java.util.Date;

@Data
public class DatasetDBItem {
    private long dataset_id;
    private long user_id;
    private String dataset_name;
    private int image_num;
    private Date dataset_create_date;
}
