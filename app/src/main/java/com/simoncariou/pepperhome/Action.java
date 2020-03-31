package com.simoncariou.pepperhome;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Action {

    //lightStatus will be gotten from the tts
    public Action(Boolean on, Integer bri, Integer hue, Integer sat){
        this.on = on;
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
    }

    @SerializedName("on")
    @Expose
    private Boolean on;
    @SerializedName("bri")
    @Expose
    private Integer bri;
    @SerializedName("hue")
    @Expose
    private Integer hue;
    @SerializedName("sat")
    @Expose
    private Integer sat;

    //getters setters for the put req
    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public Integer getBri() {
        return bri;
    }

    public void setBri(Integer bri) {
        this.bri = bri;
    }

    public Integer getHue() {
        return hue;
    }

    public void setHue(Integer hue) {
        this.hue = hue;
    }

    public Integer getSat() {
        return sat;
    }

    public void setSat(Integer sat) {
        this.sat = sat;
    }
}
