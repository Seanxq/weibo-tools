package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.mp.bean.message.WeiboPassiveMessage;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;

@Slf4j
public class WeiboPassiveMessageGsonAdapter implements JsonSerializer<WeiboPassiveMessage> {

  @Override
  public JsonElement serialize(WeiboPassiveMessage message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();
    messageJson.addProperty("result", message.getResult());
    messageJson.addProperty("type", message.getType().getMsgTypeText());
    messageJson.addProperty("receiver_id", message.getReceiver_id());
    messageJson.addProperty("sender_id", message.getSender_id());

    JsonObject data = new JsonObject();
    switch (message.getType()) {
      case MSG_TEXT:
        data.addProperty("text", message.getText());
        break;
      case MSG_ARTICLES:
        JsonArray jsonArray = new JsonArray();
        if (message.getArticles() != null)
          message.getArticles().forEach(t -> {
            jsonArray.add(t.toJsonObject());
          });
        data.add("articles", jsonArray);
        break;
      case MSG_POSITION:
        data.addProperty("longitude", message.getPos().getLatitude());
        data.addProperty("latitude", message.getPos().getLatitude());
        break;
      default:
        throw new RuntimeException("非法消息类型，暂不支持");
    }
    try {
      messageJson.addProperty("data", URLEncoder.encode(new Gson().toJson(data), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      log.error("WeiboPassiveMessageGsonAdapter", e);
    }
    return messageJson;
  }

}
