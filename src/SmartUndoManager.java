import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class SmartUndoManager extends UndoManager {
    GUI gui;
    String Undo_last ="";
    public SmartUndoManager(GUI gui) {
        super();
        this.gui = gui;
    }

    // helper classes

    public class UndoAction extends AbstractAction {
        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                Undo_last = GUI.str_timer.replace(GUI.undo_timer.get(GUI.undo_timer.size()-1), "");
                GUI.str_timer = GUI.str_timer.replace(GUI.undo_timer.get(GUI.undo_timer.size()-1), "");

                GUI.textPane.setText(Undo_last);
                GUI.str_timer =  GUI.textPane.getText();
                String str_remain = GUI.str_timer.replace(GUI.str_timer2,"");

                if (str_remain != "") {

                    GUI.undo_timer.add(str_remain);
                }
                if (GUI.undo_timer.size() >10) {
                    GUI.undo_timer.remove(0);
                }
                GUI.undo_timer.remove(GUI.undo_timer.size()-1);
                GUI.undo_timer.remove(GUI.undo_timer.size()-1);
                GUI.str_timer2 = GUI.str_timer;
            } catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            gui.redoAction.updateRedoState();

        }

        protected void updateUndoState(){
            if (canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    class RedoAction extends AbstractAction {
        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                redo();
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            gui.undoAction.updateUndoState();
        }

        protected void updateRedoState() {
            if (canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }
}