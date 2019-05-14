package com.projectmanage.Notes;


public class NoteObject {

    private String note;
    private String noteKey;
    private String date;

    public NoteObject(String note, String noteKey, String date) {
        this.note = note;
        this.noteKey = noteKey;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNoteKey() {
        return noteKey;
    }

    public void setNoteKey(String noteKey) {
        this.noteKey = noteKey;
    }
}

