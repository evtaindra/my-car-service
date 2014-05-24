package pl.rzeszow.wsiz.carservice.model;

import android.graphics.Bitmap;

/**
 * Created by rsavk_000 on 5/24/2014.
 */
public class Message {
    private int sender;
    private String date;
    private String content;
    private Bitmap attachment;
    private boolean isRead;

    public Message(int sender, String date, String content, Bitmap attachment, boolean isRead) {
        this.sender = sender;
        this.date = date;
        this.content = content;
        this.attachment = attachment;
        this.isRead = isRead;
    }

    public int getSender() {
        return sender;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public Bitmap getAttachment() {
        return attachment;
    }

    public boolean isRead() {
        return isRead;
    }
}
