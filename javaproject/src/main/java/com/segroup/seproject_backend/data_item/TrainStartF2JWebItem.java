package com.segroup.seproject_backend.data_item;

import lombok.Data;

@Data
public class TrainStartF2JWebItem {
    private String model_name;
    private long user_id;
    private long dataset_id;
    private double ratio;
    private int epoch;
    private int batch_size;
    private double learning_rate;
}
