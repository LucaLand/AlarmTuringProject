package org.tensorflow.lite.examples.detection.AlarmTuring.TelegramBotUtils;

import com.pengrad.telegrambot.request.SendPhoto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class TelegramBot extends com.pengrad.telegrambot.TelegramBot {

    public TelegramBot(String botToken) {
        super(botToken);
    }

    public void sendToTelegram(String chatID, String msg) {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        String botToken = "1931529186:AAGRQxg16hpAZ6LqGrwHjv8HNNntCMnSar0";
        urlString = String.format(urlString, botToken, chatID, msg);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(String chatID, File image){
        SendPhoto sendPhoto = new SendPhoto(chatID, image);
        execute(sendPhoto);
    }
}
