package com.example.skillyouneed.models;

import java.io.Serializable;

public class Gadget implements Serializable {

    private String gadgetUid, gadgetName, gadgetUrlIcon;

    public Gadget() {
    }

    public Gadget(String gadgetUid, String gadgetName, String gadgetUrlIcon) {
        this.gadgetUid = gadgetUid;
        this.gadgetName = gadgetName;
        this.gadgetUrlIcon = gadgetUrlIcon;
    }

    public String getGadgetUid() {
        return gadgetUid;
    }

    public void setGadgetUid(String gadgetUid) {
        this.gadgetUid = gadgetUid;
    }

    public String getGadgetName() {
        return gadgetName;
    }

    public void setGadgetName(String gadgetName) {
        this.gadgetName = gadgetName;
    }

    public String getGadgetUrlIcon() {
        return gadgetUrlIcon;
    }

    public void setGadgetUrlIcon(String gadgetUrlIcon) {
        this.gadgetUrlIcon = gadgetUrlIcon;
    }
}
