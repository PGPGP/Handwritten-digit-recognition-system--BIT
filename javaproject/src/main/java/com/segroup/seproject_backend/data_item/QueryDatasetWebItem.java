package com.segroup.seproject_backend.data_item;

import lombok.Data;

import java.util.List;

@Data
public class QueryDatasetWebItem {
    private List<DatasetDBItem> datasets;
}
