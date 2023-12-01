package com.segroup.seproject_backend.data_item;

import lombok.Data;

@Data
public class TrainInfoWebItem {
    private int epoch;
    private double train_loss;
    private double train_acc;
    private double test_loss;
    private double test_acc;
}
