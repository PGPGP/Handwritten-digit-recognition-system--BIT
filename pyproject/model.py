import torch
from torch import nn
import torch.nn.functional as F

num_classes = 10

class CNN(nn.Module):  # 我们建立的CNN继承nn.Module这个模块
    def __init__(self):
        super().__init__()
        # 特征提取网络
        self.conv1 = nn.Conv2d(1, 32, kernel_size=3)  # 第一层卷积,卷积核大小为3*3
        self.pool1 = nn.MaxPool2d(2)  # 设置池化层，池化核大小为2*2
        self.drop1 = nn.Dropout(p=0.15)
        self.conv2 = nn.Conv2d(32, 64, kernel_size=3)  # 第二层卷积,卷积核大小为3*3
        self.pool2 = nn.MaxPool2d(2)
        self.drop2 = nn.Dropout(p=0.15)

        # 分类网络
        self.fc1 = nn.Linear(1600, 64)
        self.fc2 = nn.Linear(64, num_classes)

    # 前向传播
    def forward(self, x):
        x = self.drop1(self.pool1(F.relu(self.conv1(x))))
        x = self.drop2(self.pool2(F.relu(self.conv2(x))))

        x = torch.flatten(x, start_dim=1)

        x = F.relu(self.fc1(x))
        x = self.fc2(x)

        return x