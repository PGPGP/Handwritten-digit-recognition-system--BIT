package com.segroup.seproject_backend.data_item;

import lombok.Data;

import java.util.List;

@Data
public class QueryModelWebItem {
    private List<ModelDBItem> models;
    private ModelDBItem current_model;
}
