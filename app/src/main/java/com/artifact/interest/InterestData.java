package com.artifact.interest;

/**
 * Created by girish.kulkarni on 10/1/2015.
 */
public class InterestData {

    String name;
    boolean isSelected;

    public InterestData(String name, boolean selected) {
        super();
        this.name = name;
        this.isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
