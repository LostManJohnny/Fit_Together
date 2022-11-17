package com.example.fittogether.Models.Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Exercise {
    private String name;
    private String gifURL;

    private final ArrayList<String> equipmentList;
    private final ArrayList<String> bodyParts;
    private final ArrayList<String> targets;

    public Exercise(String bodyPart, String equipment, String gifURL, String name, String target){
        this.gifURL = gifURL;
        this.name = name;

        equipmentList = new ArrayList<>();
        bodyParts = new ArrayList<>();
        targets = new ArrayList<>();

        equipmentList.add(equipment);
        bodyParts.add(bodyPart);
        targets.add(target);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addEquipment(String equipment){
        equipmentList.add(equipment);
    }

    public void addBodyPart(String bodyPart){
        bodyParts.add(bodyPart);
    }

    public void addTarget(String target){
        targets.add(target);
    }

    public ArrayList<String> getEquipment(){
        return equipmentList;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Equipment", equipmentList);
        map.put("BodyParts", bodyParts);
        map.put("Targets", targets);
        map.put("GIF", gifURL);

        return map;
    }

    public String getGifURL() {
        return gifURL;
    }

    public void setGifURL(String gifURL) {
        this.gifURL = gifURL;
    }
}
