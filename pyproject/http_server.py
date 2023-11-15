from flask import Flask, request, jsonify
import os
from preprocess import *
from train import *
from model import CNN

app = Flask(__name__)

# 根据需要调整路径
MODEL_ROOT = 'cnn2_Mnist.pkl'

current_model = CNN()
current_model.load_state_dict(torch.load(MODEL_ROOT))
current_model.eval()

DS_SAVE_IMG_ROOT = 'D:/imageRecog_temp/py/mydata/img/'

if not os.path.exists(DS_SAVE_IMG_ROOT):
    os.makedirs(DS_SAVE_IMG_ROOT)


# 预测函数
def predict(img_data):
    img = img_data.astype('float32') / 255
    img = torch.tensor(img)
    img = img.permute(0, 3, 1, 2)
    pred = current_model(img)
    pred = torch.softmax(pred, dim=1)

    results = pred.argmax(1).data.numpy()
    return results

@app.route('/switch_model', methods=['POST'])
def switch_model():
    new_model_path = request.form.get('new_model_path')
    # print(new_model_path)
    global current_model
    try:
        current_model.load_state_dict(torch.load(new_model_path))
        current_model.eval()
        return jsonify({'result': 'successful'})
    except Exception as e:
        print(e)
        return jsonify({'result': 'failed'})


@app.route('/form_dataset', methods=['POST'])
def form_dataset():
    DATA_SET_NAME = request.form.get('dataset_name')
    RAW_IMG_PATH = request.form.getlist('image_paths')

    SAVE_IMG_PATH = DS_SAVE_IMG_ROOT + DATA_SET_NAME
    PROCESSED_IMG_PATH = []

    if not os.path.exists(SAVE_IMG_PATH):
        os.makedirs(SAVE_IMG_PATH)

    # 处理图像
    for i in range(len(RAW_IMG_PATH)):
        image_name = RAW_IMG_PATH[i].split('\\')[-1]
        # print(image_name)

        images_processed_root = SAVE_IMG_PATH + '/' + image_name
        PROCESSED_IMG_PATH.append(images_processed_root)
        borders = findBorderContours(RAW_IMG_PATH[i])
        img_data = transMNIST(RAW_IMG_PATH[i], borders)[0]
        # print(images_processed_root)
        cv2.imwrite(images_processed_root, img_data)
        # print(f'{i} img is ok!')

    # 返回处理后文件的路径
    return jsonify({'image_paths': PROCESSED_IMG_PATH})


@app.route('/predict', methods=['POST'])
def predict_from_image():
    IMG_SRC_PATH = request.form.get('file_path')
    print(IMG_SRC_PATH)
    IMG_RST_PATH = IMG_SRC_PATH + '_pred.jpg'  # 识别结果图片

    print('图片预处理...')
    borders = findBorderContours(IMG_SRC_PATH)
    imgData = transMNIST(IMG_SRC_PATH, borders)

    print('识别数字...')
    results = predict(imgData)
    results_str = ",".join(map(str, results))

    print('存储结果...')
    img_rst = getResults(IMG_SRC_PATH, borders, results)
    cv2.imwrite(IMG_RST_PATH, img_rst)

    print('预测完成！')

    # 返回处理后文件的路径
    return jsonify({'file_path': IMG_RST_PATH, 'text': results_str})

if __name__ == '__main__':
    # socketio.run(app, host='0.0.0.0', port=8088, debug=True, allow_unsafe_werkzeug=True)
    app.run( host='0.0.0.0', port=8088,threaded = True)