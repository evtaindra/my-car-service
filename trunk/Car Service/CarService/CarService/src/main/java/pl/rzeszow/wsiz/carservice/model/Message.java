package pl.rzeszow.wsiz.carservice.model;

import android.graphics.Bitmap;

/**
 * Klasa reprezentująca Serwis.
 * <p>
 *    Przechowywuje wszystkie informacje o wiadomościach.
 * </p>
 */
public class Message {
    private int sender;         //!< od kogo jest przesłana wiadomość od użytkownika lub serwisu.
    private String date;        //!< data wiadomości
    private String content;     //!< treść wiadomości
    private Bitmap attachment;  //!< załącznik wiadomości.
    private boolean isRead;     //!< czy wiadoomość została przeczytana.

    /**
     * Message Konstruktor
     * <p>
     *     Inicjalizuje prywatne zmienne.
     * </p>
     * @param sender od kogo jest przesłana wiadomość od użytkownika lub serwisu.
     * @param date data wiadomości
     * @param content treść wiadomości
     * @param attachment załącznik wiadomości.
     * @param isRead czy wiadoomość została przeczytana.
     */
    public Message(int sender, String date, String content, Bitmap attachment, boolean isRead) {
        this.sender = sender;
        this.date = date;
        this.content = content;
        this.attachment = attachment;
        this.isRead = isRead;
    }

    /**
     * Nadaje dostęp do zmiennej od kogo jest przesłana wiadomość od użytkownika lub serwisu.
     * @return przesłana wiadomość jest od użytkownika lub serwisu.
     */
    public int getSender() {
        return sender;
    }
    /**
     * Nadaje dostęp do zmiennej od kogo jest przesłana wiadomość od użytkownika lub serwisu.
     * @return przesłana wiadomość jest od użytkownika lub serwisu.
     */
    public String getDate() {
        return date;
    }
    /**
     * Nadaje dostęp do zmiennej treść wiadomości
     * @return treść wiadomości
     */
    public String getContent() {
        return content;
    }
    /**
     * Nadaje dostęp do zmiennej załącznik wiadomości
     * @return załącznik wiadomości
     */
    public Bitmap getAttachment() {
        return attachment;
    }
    /**
     * Nadaje dostęp do zmiennej czy wiadoomość została przeczytana.
     * @return czy wiadoomość została przeczytana.
     */
    public boolean isRead() {
        return isRead;
    }
}
