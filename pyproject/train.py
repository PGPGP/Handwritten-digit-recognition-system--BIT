import torch

device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

def train(dataloader,model,loss_fn,optimizer):
    size = len(dataloader.dataset)
    num_batches = len(dataloader)

    train_loss,train_acc = 0,0

    for X,y in dataloader:
        X,y = X.to(device),y.to(device)

        pred = model(X)
        loss =loss_fn(pred,y)

        optimizer.zero_grad()
        loss.backward()
        optimizer.step()

        train_acc += (pred.argmax(1)==y).type(torch.float).sum().item()
        train_loss += loss.item()

    train_acc /= size
    train_loss /= num_batches

    return train_acc,train_loss


def test(dataloader, model, loss_fn):
    size = len(dataloader.dataset)  # 测试集的大小，一共10000张图片
    num_batches = len(dataloader)  # 批次数目，313（10000/32=312.5，向上取整）
    test_loss, test_acc = 0, 0

    # 当不进行训练时，停止梯度更新，节省计算内存消耗
    with torch.no_grad():
        for imgs, target in dataloader:
            imgs, target = imgs.to(device), target.to(device)

            # 计算loss
            target_pred = model(imgs)
            loss = loss_fn(target_pred, target)

            test_loss += loss.item()
            test_acc += (target_pred.argmax(1) == target).type(torch.float).sum().item()

    test_acc /= size
    test_loss /= num_batches

    return test_acc, test_loss