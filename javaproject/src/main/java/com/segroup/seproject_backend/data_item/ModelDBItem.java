package com.segroup.seproject_backend.data_item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelDBItem {
    private long model_id;
    private long user_id;
    private String model_name;
    private String model_path;
    private long dataset_id;
    private double train_accuracy;
    private int is_active;
    private Date model_create_date;
    private Date model_activate_date;
}
