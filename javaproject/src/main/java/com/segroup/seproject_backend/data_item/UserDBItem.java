package com.segroup.seproject_backend.data_item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;

@Data
@Table(name="Users")
@AllArgsConstructor
@NoArgsConstructor
public class UserDBItem {
    private long user_id;
    private long verify_key;
    private String user_name;
    private String user_password;
}
