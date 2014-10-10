package com.webmanagement.thaidigitaltv;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 15/9/2557.
 */
public class ItemGroupTab2 {

    private String Name, Image;
    private ArrayList<ItemChildTab2> Items;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public ArrayList<ItemChildTab2> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ItemChildTab2> Items) {
        this.Items = Items;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}


