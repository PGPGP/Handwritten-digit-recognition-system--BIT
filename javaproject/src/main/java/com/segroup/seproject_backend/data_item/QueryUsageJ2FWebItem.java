package com.segroup.seproject_backend.data_item;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QueryUsageJ2FWebItem {
    private List<UsageDBItem> usages;
}
