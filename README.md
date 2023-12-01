# Handwritten-digit-recognition-system--BIT
BIT20级卓工训练项目——基于minist数据集的手写数字识别系统
## wechat文件夹为手机的微信小程序前端：
  可用微信开发者工具打开
## Web文件夹为PC前端：
  web端口：localhost:2579
  
  用vscode开发 需要安装nodejs(版本≥16)
  
  vscode插件（建议安装）：Vetur , Volar
  
  vsc打开，npm install 安装相关依赖，npm run dev 启动
## pyproject文件夹为Python端：
  下载requirement.txt文件后，在放置该文件的目录下用命令行输入pip install -r requirement.txt，安装第三方库  
  
  http_server.py 包括了预测，组建数据集以及训切换模型，端口：8088
  
  websocket_server.py 主要为训练模型和暂停训练，端口：8089

## javaproject文件夹为Java端：
  web端口：8080
  
  IDE：Intellij IDEA
  
  需要安装MySQL，并且创建用户（用户名和密码均为"rosy"，且有访问和修改se_project数据库的权限）
  
  ，创建好相应的数据库（创建数据库的代码见数据库文档），项目才能运行。
  
