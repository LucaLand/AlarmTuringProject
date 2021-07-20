package org.tensorflow.lite.examples.detection.AlarmTuring.TelegramBotUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TelegramBot {
    public void sendToTelegram(String chatID, String msg) {
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

        //Telegram Bot token
        String apiToken = "1931529186:AAHupySu8O4R-CeCbVJrFgQ97tF52XqBxbI";

        //chatId
        //String chatId = "-480710730";

        //MSG
        //String text = "Hello world!";

        urlString = String.format(urlString, apiToken, chatID, msg);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
