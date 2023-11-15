import torch
from torchvision import transforms
from PIL import Image
from torch.utils.data import Dataset

class MyDataset(torch.utils.data.Dataset):#需要继承torch.utils.data.Dataset
    def __init__(self,datainfo):
        self.datainfo = datainfo

    # 返回数据集大小
    def __len__(self):
        return len(self.datainfo)

    # 打开index对应图片进行预处理后return回处理后的图片和标签
    def __getitem__(self, index):
        imgpath,label = self.datainfo[index]
        imgs = Image.open(imgpath)
        imgs = transforms.ToTensor()(imgs)
        return imgs,label

