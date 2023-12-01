package com.segroup.seproject_backend.data_item;

import lombok.Data;

@Data
public class VerifyKeyDBItem {
    private long verify_key;
    private int max_count;
    private int remain_count;
}
