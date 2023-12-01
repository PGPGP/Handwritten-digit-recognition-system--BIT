package com.segroup.seproject_backend.data_item;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageRecogF2JWebItem {
    private MultipartFile file;
    private String postfix;
}
