package com.example.servertest.model;
// Tạo lớp ItemData.java
public class Category {
    private int imageResId;
    private String itemgridName;
    private int groupId;
    public Category(int imageResId, String itemgridName, int gruopId) {
        this.imageResId = imageResId;
        this.itemgridName = itemgridName;
        this.groupId = gruopId;
    }


    public int getImageResId() {
        return imageResId;
    }

    public String getItemgridName() {
        return itemgridName;
    }

    public int getGroupId() {
        return groupId;
    }
}
