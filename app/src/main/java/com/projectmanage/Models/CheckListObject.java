package com.projectmanage.Models;

public class CheckListObject {

     String checkListText;
     Boolean checkBoxBool;
     String checkKey;


    public CheckListObject(String checkListText, Boolean checkBoxBool,String checkKey) {
        this.checkListText = checkListText;
        this.checkBoxBool = checkBoxBool;
        this.checkKey = checkKey;

    }

    public String getCheckListText() {
        return checkListText;
    }

    public void setCheckListText(String checkListText) {
        this.checkListText = checkListText;
    }

    public Boolean getCheckBoxBool() {
        return checkBoxBool;
    }

    public void setCheckBoxBool(Boolean checkBoxBool) {
        this.checkBoxBool = checkBoxBool;
    }

    public String getCheckKey() {
        return checkKey;
    }

    public void setCheckKey(String checkKey) {
        this.checkKey = checkKey;
    }
}
