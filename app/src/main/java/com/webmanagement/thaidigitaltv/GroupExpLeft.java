package com.webmanagement.thaidigitaltv;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 15/9/2557.
 */
public class GroupExpLeft {

    private String Name, Image;
    private ArrayList<ItemExpLeft> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<ItemExpLeft> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ItemExpLeft> Items) {
        this.Items = Items;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}


