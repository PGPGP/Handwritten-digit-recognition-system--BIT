package com.segroup.seproject_backend.data_item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketItem<T> {
    private long user_id;
    private String type;
    private T object;
}
