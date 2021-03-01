package com.company;

public class FuncEdit {
    GUI gui;

    public FuncEdit(GUI gui) {
        this.gui = gui;
    }

    public void undo(){
        gui.um.undo();
    }

    public void redo(){
        gui.um.redo();
    }
}
