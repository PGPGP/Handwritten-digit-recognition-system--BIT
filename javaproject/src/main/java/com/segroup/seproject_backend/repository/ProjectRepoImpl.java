package com.segroup.seproject_backend.repository;


import com.segroup.seproject_backend.data_item.DatasetDBItem;
import com.segroup.seproject_backend.data_item.DatasetImageDBItem;
import com.segroup.seproject_backend.data_item.ImageDBItem;
import com.segroup.seproject_backend.data_item.ImageDBItem;
import com.segroup.seproject_backend.data_item.ModelDBItem;
import com.segroup.seproject_backend.data_item.UsageDBItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Locale;

//使用这个类来操作数据库。
//使用方法：将功能的执行过程中，所有对数据库的操作封装成这个类的一个方法（例如RecordOneUse，在数据库中记录一次使用）。
//然后在Controller类的功能代码中调用这个方法。
//注意：新增方法时，需要在接口ProjectRepo中新增方法，再在这个类中实现接口中新增的方法。
//提示：常使用JdbcTemplate的两个方法操作数据库，JdbcTemplate::update、JdbcTemplate::queryForObject和JdbcTemplate::queryForList。
//这些方法的使用都可以参考这个类原有的代码。
@Repository
public class ProjectRepoImpl implements ProjectRepo{

    @Autowired
    private JdbcTemplate jdbc;

    //使用数据库的Date类型时，需要注意：
    //Date有两种：java.util.Date和java.sql.Date。
    //Java代码里经常使用的是java.util.Date，查询数据库得到的/向数据库中插入的是java.sql.Date。
    //java.sql.Date继承自java.util.Date，因此可以直接把java.sql.Date赋值给java.util.Date。
    //但反过来就不可以。所以需要使用下面这个函数，将java.util.Date转换为java.sql.Date。
    //当向sql语句传入java.util.Date变量时，必须先使用这个函数转换Date的类型。
    //另外说一句：希望对Date的转换能够全部局限到这个类的内部，这个类对外提供的方法全部使用java.util.Date，因此在这个类之外，无需考虑Date类型转换的问题。
    private java.sql.Date dateConvert(Date date){
        return new java.sql.Date(date.getTime());
    }

    //为使用频次表添加记录项
    private UsageDBItem insertUsageRecord(Date use_date, long model_id) {
        jdbc.update("INSERT INTO usages(use_date, model_id) VALUES (?, ?)", dateConvert(use_date), model_id);
        return new UsageDBItem(use_date, model_id, 0, 0, 0);
    }

    private String deleteDataset(long dataset_id){
        int rowsAffected = jdbc.update("DELETE FROM datasets WHERE dataset_id = ?", dataset_id);
        if (rowsAffected > 0) {
            // 删除成功
            return "successful";
        } else {
            // 未找到匹配的记录，可能已经被删除或根本不存在
            return "failed";
        }
    }

    private boolean deleteImageFile(String imagePath) {
        // 使用 File 类来删除图像文件
        File file = new File(imagePath);
        return file.delete();
    }
    //记录一次使用
    @Override
    public void recordOneUse(Date use_date, long model_id) {
        //从数据库中取出数据项
        UsageDBItem usage;
        try {
            usage = jdbc.queryForObject("SELECT * FROM usages WHERE use_date = ? AND model_id = ?",
                    new BeanPropertyRowMapper<>(UsageDBItem.class),
                    dateConvert(use_date),
                    model_id);
        }
        catch(EmptyResultDataAccessException e) {
            //如果未取到则新建
            usage = insertUsageRecord(use_date, model_id);
        }

        //更新数据项
        int use_count = usage.getUse_count() + 1;

        //写回数据库
        int res = jdbc.update("UPDATE usages SET use_count = ? WHERE use_date = ? AND model_id = ?",
                use_count,
                dateConvert(use_date),
                model_id);

        if(res != 1){
            throw new RuntimeException("我也不知道怎么了，本来不应该执行到这里的！12445342");
        }
    }

    //记录一次“正确”反馈
    @Override
    public void recordOneRightFeedback(Date use_date, long model_id) {
        //从数据库中取出数据项
        UsageDBItem usage;
        try {
            usage = jdbc.queryForObject("SELECT * FROM usages WHERE use_date = ? AND model_id = ?",
                    new BeanPropertyRowMapper<>(UsageDBItem.class),
                    dateConvert(use_date),
                    model_id);
        }
        catch(EmptyResultDataAccessException e) {
            //如果未取到则新建
            usage = insertUsageRecord(use_date, model_id);
        }

        //更新数据项
        int right_feedback_count = usage.getRight_feedback_count() + 1;

        //写回数据库
        int res = jdbc.update("UPDATE usages SET right_feedback_count = ? WHERE use_date = ? AND model_id = ?",
                right_feedback_count,
                dateConvert(use_date),
                model_id);

        if(res != 1){
            throw new RuntimeException("我也不知道怎么了，本来不应该执行到这里的！83647282");
        }
    }

    //记录一次“错误”反馈
    @Override
    public void recordOneWrongFeedback(Date use_date, long model_id) {
        //从数据库中取出数据项
        UsageDBItem usage;
        try {
            usage = jdbc.queryForObject("SELECT * FROM usages WHERE use_date = ? AND model_id = ?",
                    new BeanPropertyRowMapper<>(UsageDBItem.class),
                    dateConvert(use_date),
                    model_id);
        }
        catch(EmptyResultDataAccessException e) {
            //如果未取到则新建
            usage = insertUsageRecord(use_date, model_id);
        }

        //更新数据项
        int wrong_feedback_count = usage.getWrong_feedback_count() + 1;

        //写回数据库
        int res = jdbc.update("UPDATE usages SET wrong_feedback_count = ? WHERE use_date = ? AND model_id = ?",
                wrong_feedback_count,
                dateConvert(use_date),
                model_id);

        if(res != 1){
            throw new RuntimeException("我也不知道怎么了，本来不应该执行到这里的！09786844");
        }
    }

    // 根据数据集id查询该数据集的图片
    @Override
    public List<ImageDBItem> findImagesByDatasetId(long dataset_id) {
        return jdbc.query(
            """
                SELECT * FROM images WHERE image_id IN (
                    SELECT image_id FROM dataset_image WHERE dataset_id = ?
                );
            """,
            new BeanPropertyRowMapper<>(ImageDBItem.class),
            dataset_id
        );
    }

    // 向模型表中插入模型
    // 不会插入模型id，因为id由数据库自动分配。
    // 不会插入启用日期，因为新建的模型还未启用。
    @Override
    public void insertModel(ModelDBItem model) {
        jdbc.update("INSERT INTO models(user_id, model_name, model_path, dataset_id, train_accuracy, is_active, model_create_date) VALUES (?, ?, ?, ?, ?, ?, ?)",
            model.getUser_id(),
            model.getModel_name(),
            model.getModel_path(),
            model.getDataset_id(),
            model.getTrain_accuracy(),
            model.getIs_active(),
            dateConvert(model.getModel_create_date())
        );
    }

    // 根据数据集id查询该数据集的图片
    @Override
    public List<UsageDBItem> findUsagesByModelId(long model_id) {
        return jdbc.query(
                "SELECT * FROM usages WHERE model_id = ?;",
                new BeanPropertyRowMapper<>(UsageDBItem.class),
                model_id
        );
    }

    // 获取当前模型
    @Override
    public ModelDBItem getCurrentModel() {
        return jdbc.queryForObject("SELECT * FROM models WHERE is_active = 1", new BeanPropertyRowMapper<>(ModelDBItem.class));
    }

    @Override
    public String deleteDataBaseItem(long dataset_id){
        //根据dataset_id,查找dataset_image中需要删除的image_id,组成image_ids
        List<DatasetImageDBItem> image_ids;
        try{
            image_ids = jdbc.query("SELECT * FROM dataset_image WHERE dataset_id = ?",
                    new BeanPropertyRowMapper<>(DatasetImageDBItem.class),
                    dataset_id
            );
        }
        catch (EmptyResultDataAccessException e){
            image_ids = null;
        }
        //根据image_ids,在images中查找对应图片路径，在相应位置删除图片
        if (image_ids != null) {
            for (DatasetImageDBItem imageItem : image_ids) {

                long imageId = imageItem.getImage_id();

                ImageDBItem image = null;
                try{
                    image = jdbc.queryForObject("SELECT * FROM images WHERE image_id=?",
                            new BeanPropertyRowMapper<>(ImageDBItem.class),
                            imageId);
                }catch (EmptyResultDataAccessException ex) {

                    ex.printStackTrace();
                }

                //获取图片路径
                String imagePath;
                if (image != null) {
                    imagePath = image.getImage_path();

                    // 在这里继续处理 imagePath，例如删除文件等操作
                } else {
                    return "无法定位图片";
                }

                // Delete the image file
                boolean deleted = deleteImageFile(imagePath);
                if(!deleted){
                    return "图片为成功删除";
                }

                int res1 = jdbc.update("DELETE FROM images WHERE image_id = ?", imageId);
                if(res1<=0)
                {
                    return "failed";
                }
                int res2 = jdbc.update("DELETE FROM dataset_image WHERE image_id = ?", imageId);
                if(res2<=0)
                {
                    return "failed";
                }


            }
        }

        //删除datasets表项
        DatasetDBItem dataset = null;
        try {
            dataset = jdbc.queryForObject("SELECT * FROM datasets WHERE dataset_id = ?",
                    new BeanPropertyRowMapper<>(DatasetDBItem.class),
                    dataset_id);
        }

        catch(EmptyResultDataAccessException e) {
            //如果未取到则新建
//            usage = insertUsageRecord(use_date, model_id);
            System.out.println("数据库中不存在相应数据集");
        }
        if (dataset != null) {
            // 执行删除操作
            return deleteDataset(dataset.getDataset_id());
        }else{
            return "failed";
        }

    }

    @Override
    public boolean uploadimagesDB(List<String> image_paths, List<String> labels, long dataset_id){
        if (image_paths.size() != labels.size()) {
            // 确保两个列表的长度相等，以避免数据不一致
            return false;
        }
        for (int i = 0; i < image_paths.size(); i++) {
            String image_path = image_paths.get(i);
            String labelString = labels.get(i);
            int label = Integer.parseInt(labelString);

            ImageDBItem temp = null;
            // 插入到数据库
            int res1 = jdbc.update("INSERT INTO images(image_path, label) VALUES (?, ?)", image_path, label);
            try{
                temp = jdbc.queryForObject("SELECT * FROM images WHERE image_path=?",
                        new BeanPropertyRowMapper<>(ImageDBItem.class),
                        image_path);
            }catch (EmptyResultDataAccessException ex) {

                ex.printStackTrace();
            }
            long image_id = temp.getImage_id();

            int res2 = jdbc.update("INSERT INTO dataset_image(image_id,dataset_id) VALUES (?,?)", image_id,dataset_id);
            if(res1<=0||res2<=0){
                return false;
            }

        }

        // 所有插入操作成功
        return true;

    }

    @Override
    public long uploaddatasetsDB(long user_id, String dataset_name,int image_num){
        LocalDateTime localDateTime = LocalDateTime.now();
        Date utilDate = java.util.Date.from(localDateTime.atZone(java.time.ZoneId.systemDefault()).toInstant());
        System.out.println(utilDate);

        int res = jdbc.update("INSERT INTO datasets(user_id,dataset_name,image_num,dataset_create_date) VALUES (?,?,?,?)", user_id,dataset_name,image_num,dateConvert(utilDate));
        if(res<=0){
            return -1;
        }
        DatasetDBItem temp = null;
        try{
            temp = jdbc.queryForObject("SELECT * FROM datasets WHERE dataset_name=?",
                    new BeanPropertyRowMapper<>(DatasetDBItem.class),
                    dataset_name);
        }catch (EmptyResultDataAccessException ex) {

            ex.printStackTrace();
        }
        long result = temp.getDataset_id();
        return result;

    }

}
