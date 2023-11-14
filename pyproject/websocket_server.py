import threading
from flask import Flask
from flask_sockets import Sockets
import torch.nn as nn
from torch.utils.data import DataLoader
import os
from train import *
from model import CNN
from dataset import MyDataset
from sklearn.model_selection import train_test_split
from gevent import pywsgi
from geventwebsocket.handler import WebSocketHandler
import json
import threading

app = Flask(__name__)
sockets = Sockets(app)

# 存储每个用户的停止训练标志
stop_training_flags = {}

MODEL_SAVE_ROOT = 'D:/imageRecog_temp/py/model/'
if not os.path.exists(MODEL_SAVE_ROOT):
    os.makedirs(MODEL_SAVE_ROOT)


@sockets.route('/train')
def on_msg(ws):
    while not ws.closed:
        message = ws.receive()
        message_dict = json.loads(message)
        user_id = message_dict["user_id"]
        type = message_dict["type"]
        if type == "start":
            # 启动训练
            object_dict = message_dict["object"]
            print("执行train_model()...")
            # train_model(ws, user_id, object_dict)
            threading.Thread(target=train_model, args=(ws, user_id, object_dict)).start()
            print("进程建立完成，可以执行下一个命令")
        elif type == "stop":
            # 停止训练
            # handle_disconnect(user_id)
            threading.Thread(target=handle_disconnect, args=(user_id,)).start()
        else:
            print("websocket: 未定义的消息类型")


def handle_disconnect(user_id):
    stop_training_flags[user_id] = True


def train_model(ws, user_id, data):
    print("data:")
    print(data)
    model_name = data['model_name']
    epochs = data['epoch']
    learning_rate = data['learning_rate']
    batch_size = data['batch_size']
    ratio = data['ratio']
    txt_path = data['txt_path']

    stop_training_flags[user_id] = False
    

    print("准备打开文件...")
    with open(txt_path, 'r') as file:
        lines = file.readlines()
    file.close()
    datainfo = [line.strip('\n').split(',') for line in lines]
    datainfo = [(path, int(label)) for path, label in datainfo]
    train_data, test_data = train_test_split(datainfo, test_size=1 - ratio, random_state=42)

    print("开始训练...")

    train_dataset = MyDataset(train_data)
    test_dataset = MyDataset(test_data)

    train_loader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
    test_loader = DataLoader(test_dataset, batch_size=batch_size, shuffle=True)
    train_loss = []
    train_acc = []
    test_loss = []
    test_acc = []

    model = CNN().to(device)
    loss_fn = nn.CrossEntropyLoss()  # 创建损失函数
    optimizer = torch.optim.SGD(model.parameters(), lr=learning_rate)

    template = 'Epoch:{:2d}, Train_acc:{:.1f}%, Train_loss:{:.3f}, Test_acc:{:.1f}%,Test_loss:{:.3f}'
    for epoch in range(epochs):
        if stop_training_flags[user_id]:
            break
        model.train()
        epoch_train_acc, epoch_train_loss = train(train_loader, model, loss_fn, optimizer)

        model.eval()
        epoch_test_acc, epoch_test_loss = test(test_loader, model, loss_fn)

        train_acc.append(epoch_train_acc)
        train_loss.append(epoch_train_loss)
        test_acc.append(epoch_test_acc)
        test_loss.append(epoch_test_loss)

        # print(
        #     template.format(epoch + 1, epoch_train_acc * 100, epoch_train_loss, epoch_test_acc * 100, epoch_test_loss))

        ws.send(json.dumps({
            "user_id": user_id,
            "type": "info",
            "object": {
                "epoch": epoch + 1,
                "train_acc": epoch_train_acc * 100,
                "train_loss": epoch_train_loss,
                "test_acc": epoch_test_acc * 100,
                "test_loss": epoch_test_loss
            }
        })
        )

    if stop_training_flags[user_id]:
        print('Stop')
    else:
        print('Done')
    torch.save(model.state_dict(), MODEL_SAVE_ROOT + model_name)  # 保存模型
    ws.send(json.dumps({
        "user_id": user_id,
        "type": "finish",
        "object": {
            "path": MODEL_SAVE_ROOT + model_name
        }
    }))


if __name__ == '__main__':
    server = pywsgi.WSGIServer(('0.0.0.0', 8089), application=app, handler_class=WebSocketHandler)
    print('server started')
    server.serve_forever()
