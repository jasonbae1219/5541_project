package com.company;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {
    JFrame window;
    JTextArea textArea;
    JScrollPane scrollPane;

    // top menu bar
    JMenuBar menuBar;
    JMenu menuFile, menuEdit;

    // file menu
    JMenuItem miNew, miOpen, miSave, miSaveAs, miExit;
    // edit menu
    JMenuItem miUndo, miRedo;

    FuncFile file = new FuncFile(this);
    FuncEdit edit = new FuncEdit(this);

    UndoManager um = new UndoManager();

    public GUI() {
        createWindow();
        createTextArea();
        createMenuBar();
        createFileMenu();
        createEditMenu();
        window.setVisible(true);
    }

    public void createWindow() {
        window = new JFrame("Smart Text Editor"); // design: name
        window.setSize(800, 600); // design: default size
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // design: default closing behavior
    }

    public void createTextArea() {
        textArea = new JTextArea();
        textArea.getDocument().addUndoableEditListener(
                new UndoableEditListener() {
                    @Override
                    public void undoableEditHappened(UndoableEditEvent e) {
                        um.addEdit(e.getEdit());
                    }
                }
        );
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // remove scroll pane boarder
        window.add(scrollPane);
    }

    public void createMenuBar() {
        menuBar = new JMenuBar();
        window.setJMenuBar(menuBar);

        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");

        menuBar.add(menuFile);
        menuBar.add(menuEdit);
    }

    public void createFileMenu() {
        miNew = new JMenuItem("New");
        miNew.addActionListener(this);
        miNew.setActionCommand("New");

        miOpen = new JMenuItem("Open");
        miOpen.addActionListener(this);
        miOpen.setActionCommand("Open");

        miSave = new JMenuItem("Save");
        miSave.addActionListener(this);
        miSave.setActionCommand("Save");

        miSaveAs = new JMenuItem("Save As");
        miSaveAs.addActionListener(this);
        miSaveAs.setActionCommand("SaveAs");

        miExit = new JMenuItem("Exit");
        miExit.addActionListener(this);
        miExit.setActionCommand("Exit");

        menuFile.add(miNew);
        menuFile.add(miOpen);
        menuFile.add(miSave);
        menuFile.add(miSaveAs);
        menuFile.add(miExit);
    }

    public void createEditMenu() {
        miUndo = new JMenuItem("Undo");
        miUndo.addActionListener(this);
        miUndo.setActionCommand("Undo");

        miRedo = new JMenuItem("Redo");
        miRedo.addActionListener(this);
        miRedo.setActionCommand("Redo");

        menuEdit.add(miUndo);
        menuEdit.add(miRedo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        switch (command) {
            case "New":
                file.newFile();
                break;
            case "Open":
                file.open();
                break;
            case "Save":
                file.save();
                break;
            case "SaveAs":
                file.saveAs();
                break;
            case "Exit":
                file.exit();
                break;
            case "Undo":
                edit.undo();
                break;
            case "Redo":
                edit.redo();
                break;
        }
    }
}
