package com.pornattapat.dper;

public class Board {
    private String head,pictureHead,pictureContent;

    public Board() {

    }

    public Board(String head, String pictureHead, String pictureContent) {
        this.head = head;
        this.pictureHead = pictureHead;
        this.pictureContent = pictureContent;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPictureHead() {
        return pictureHead;
    }

    public void setPictureHead(String pictureHead) {
        this.pictureHead = pictureHead;
    }

    public String getPictureContent() {
        return pictureContent;
    }

    public void setPictureContent(String pictureContent) {
        this.pictureContent = pictureContent;
    }
}
