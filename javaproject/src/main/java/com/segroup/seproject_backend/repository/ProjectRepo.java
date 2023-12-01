package com.segroup.seproject_backend.repository;

import com.segroup.seproject_backend.data_item.DatasetDBItem;

import com.segroup.seproject_backend.data_item.ImageDBItem;
import com.segroup.seproject_backend.data_item.ModelDBItem;
import com.segroup.seproject_backend.data_item.UsageDBItem;

import java.util.Date;
import java.util.List;

public interface ProjectRepo {

    //以下三个函数分别在数据库的使用和反馈表（usages）中登记模型的使用和反馈情况，调用一个函数则对应次数+1。
    public void recordOneUse(Date use_date, long model_id);

    public void recordOneRightFeedback(Date use_date, long model_id);

    public void recordOneWrongFeedback(Date use_date, long model_id);

    public String deleteDataBaseItem(long dataset_id);

    //上传images,dataset_image数据库
    public boolean uploadimagesDB(List<String> image_paths, List<String> labels, long dataset_id);

    //上传datasets数据库
    public long uploaddatasetsDB(long user_id, String dataset_name,int image_num);


    // 以下两个函数为训练模型使用
    // 根据数据集id查询该数据集的图片
    public List<ImageDBItem> findImagesByDatasetId(long dataset_id);
    // 向模型表中插入模型
    public void insertModel(ModelDBItem model);

    // 请求统计信息
    public List<UsageDBItem> findUsagesByModelId(long model_id);

    // 获取当前模型
    public ModelDBItem getCurrentModel();
}
