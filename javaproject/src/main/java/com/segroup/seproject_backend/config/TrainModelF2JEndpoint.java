package com.segroup.seproject_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segroup.seproject_backend.controller.ModelTrainController;
import com.segroup.seproject_backend.data_item.TrainStartF2JWebItem;
import com.segroup.seproject_backend.data_item.WebSocketItem;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

// 训练模型时的websocket服务端，用于和前端连接
@ServerEndpoint("/manage/train")
@Component
public class TrainModelF2JEndpoint {

    // 当前的连接
    private Session currentSession = null;

//    @Autowired
//    public ModelTrainController controller;
    static private BeanFactory beanFactory;
    @Autowired
    public void setBeanFactory(BeanFactory _beanFactory) {
        TrainModelF2JEndpoint.beanFactory = _beanFactory;
    }

    private ModelTrainController controller;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        if(controller == null) {
            controller = beanFactory.getBean(ModelTrainController.class);
        }
        if(currentSession == null) {
            currentSession = session;
            controller.session = session;
            System.out.println("WebSocket：和前端连接成功。");
        }
        else {
            throw new IOException("WebSocket错误：前端同时进行了多个连接");
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        if(currentSession == null || currentSession.equals(session)) {
            System.out.println("WebSocket：和前端连接关闭");
            currentSession = null;
        }
        else {
            throw new IOException("WebSocket错误：前端关闭了其它连接");
        }
    }

    @OnMessage
    public void onMsg(String text) throws IOException, InterruptedException {
        System.out.println(controller);
        ObjectMapper mapper = new ObjectMapper();
        WebSocketItem<Object> object = mapper.readValue(text, new TypeReference<>() {});
        long user_id = object.getUser_id();
        if(object.getType().equals("start")) {
            TrainStartF2JWebItem startItem = mapper.readValue(text, new TypeReference<WebSocketItem<TrainStartF2JWebItem>>(){}).getObject();
            controller.onFStart(user_id, startItem);
            System.out.println("start:");
            System.out.println(startItem);
        }
        else if (object.getType().equals("stop")) {
            controller.onFStop(user_id);
            System.out.println("stop");
        }
        else {
            System.out.println("WebSocket错误：前端发送的消息格式出错");
        }

    }

    static public void send(String text, Session session) throws IOException {
        if(session == null) {
            System.out.println("WebSocket错误：前端连接已关闭");
            return;
        }
        session.getBasicRemote().sendText(text);
    }

}
