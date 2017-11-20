package com.intelthings.intelthings.Logic;

import java.util.HashMap;

/**
 * Created by Lab1 on 07.11.2017.
 */

public class Light {

    public Light(){}

    public Light(String name, Boolean state){
        setName(name);
        setState(state);
        shedule = new HashMap<String, Boolean>();
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Boolean getStateOutsideSwitch() {
        return stateOutsideSwitch;
    }

    public void setStateOutsideSwitch(Boolean stateOutsideSwitch) {
        this.stateOutsideSwitch = stateOutsideSwitch;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic() {
        this.topic = name + "/" + state.toString();
    }

    private String topic;
    private String name;
    private Boolean state;
    private Boolean stateOutsideSwitch;
    private Double temperature;
    private HashMap<String, Boolean> shedule;
}
