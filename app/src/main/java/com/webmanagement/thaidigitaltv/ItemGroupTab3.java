package com.webmanagement.thaidigitaltv;

import java.util.ArrayList;

/**
 * Created by SystemDLL on 15/9/2557.
 */
public class ItemGroupTab3 {

    private String TypeName;
    private int TypeId;
    private ArrayList<ItemChildTab3> Items;

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String TypeName) {
        this.TypeName = TypeName;
    }

    public ArrayList<ItemChildTab3> getItems() {
        return Items;
    }

    public void setItems(ArrayList<ItemChildTab3> Items) {
        this.Items = Items;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int TypeId) {
        this.TypeId = TypeId;
    }
}


