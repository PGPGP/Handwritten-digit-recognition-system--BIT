package com.segroup.seproject_backend.data_item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainStartJ2PWebItem {
    private String model_name;
    private String txt_path;
    private double ratio;
    private int epoch;
    private int batch_size;
    private double learning_rate;
}
