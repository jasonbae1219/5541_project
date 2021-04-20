import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class GUI extends JFrame {
    // GUI text components
    public static JTextPane textPane;
    AbstractDocument doc;
    JTextArea changeLog;
    JScrollPane scrollPane, scrollPaneForLog;
    JSplitPane splitPane;
    public static ArrayList<String> ten_Edit = new ArrayList<String>();
    public final static int ONE_SECOND = 1000;
    private static Timer timer;

    public static String str, str_timer, str_timer2 = "";
    public static String[] str2, ten;
    public static int temp_size = 0;
    public static ArrayList<String> ten_edit = new ArrayList<>();
    public static ArrayList<String> undo_ten_edit = new ArrayList<>();
    public static ArrayList<String> undo_timer = new ArrayList<>();
    public static int size= 0, size_temp=0;
    public static ArrayList<Integer> index = new ArrayList<Integer>(1000);
    public static ArrayList<Integer> index_undo = new ArrayList<Integer>(100);

    // DATA
    String newline = "\n";
    HashMap<Object, Action> actions;

    // top menu bar
    JMenuBar menuBar;

    // file options
    FileManager file = new FileManager(this);
    protected FileManager.NewAction newAction;
    protected FileManager.OpenAction openAction;
    protected FileManager.SaveAction saveAction;
    protected FileManager.SaveAsAction saveAsAction;
    protected FileManager.ExitAction exitAction;

    // edit (undo/redo) options
    protected SmartUndoManager um = new SmartUndoManager(this);
    protected SmartUndoManager.UndoAction undoAction;
    protected SmartUndoManager.RedoAction redoAction;


    // constructor -----------------------------------------------
    public GUI() {
        // set main window
        super("Smart Text Editor"); // design: name
        setSize(800, 600); // design: default size
        setLayout(new BorderLayout()); // layout: Border Layout
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // design: default closing behavior

        createTextPane();
        createSidePanel();
        createSplitPane();
        createMenuBar();
        setVisible(true);

        addListeners(); // start listening for edits
    }

    // helper methods ---------------------------------------------
    public void createTextPane() {
        textPane = new JTextPane();
        StyledDocument styleDoc = textPane.getStyledDocument();
        if (styleDoc instanceof AbstractDocument) {
            doc = (AbstractDocument) styleDoc;
        } else {
            System.err.println("Text pane's document isn't an AbstractDocument!");
            System.exit(-1);
        }

        scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // remove scroll pane boarder
    }

    public void createSidePanel(){
        changeLog = new JTextArea();
        changeLog.setEditable(false);
        scrollPaneForLog = new JScrollPane(changeLog);
    }

    public void createSplitPane() {
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, scrollPaneForLog);

        scrollPane.setMinimumSize(new Dimension(500,600));
        scrollPaneForLog.setMinimumSize(new Dimension(150, 600));

        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.75);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);
    }

    public void createMenuBar() {
        actions = createActionTable(textPane);
        JMenu fileMenu = createFileMenu();
        JMenu editMenu = createEditMenu();
        JMenu styleMenu = createStyleMenu();
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(styleMenu);
        this.setJMenuBar(menuBar);
    }

    protected JMenu createFileMenu() {
        JMenu menu = new JMenu("File");

        newAction = file.new NewAction();
        openAction = file.new OpenAction();
        saveAction = file.new SaveAction();
        saveAsAction = file.new SaveAsAction();
        exitAction = file.new ExitAction();

        menu.add(newAction);
        menu.add(openAction);
        menu.add(saveAction);
        menu.add(saveAsAction);
        menu.add(exitAction);

        return menu;
    }

    protected JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");

        undoAction = um.new UndoAction();
        menu.add(undoAction);

        redoAction = um.new RedoAction();
        menu.add(redoAction);

        menu.addSeparator();

        // default editor kit
        menu.add(getActionByName(DefaultEditorKit.copyAction));
        menu.add(getActionByName(DefaultEditorKit.copyAction));
        menu.add(getActionByName(DefaultEditorKit.pasteAction));

        menu.addSeparator();

        menu.add(getActionByName(DefaultEditorKit.selectAllAction));
        return menu;
    }

    protected JMenu createStyleMenu(){
        JMenu menu = new JMenu("Style");

        Action action = new StyledEditorKit.BoldAction();
        action.putValue(Action.NAME, "Bold");
        menu.add(action);

        action = new StyledEditorKit.ItalicAction();
        action.putValue(Action.NAME, "Italic");
        menu.add(action);

        action = new StyledEditorKit.UnderlineAction();
        action.putValue(Action.NAME, "Underline");
        menu.add(action);

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontSizeAction("12", 12));
        menu.add(new StyledEditorKit.FontSizeAction("14", 14));
        menu.add(new StyledEditorKit.FontSizeAction("18", 18));

        menu.addSeparator();

        menu.add(new StyledEditorKit.FontFamilyAction("Serif","Serif"));
        menu.add(new StyledEditorKit.FontFamilyAction("SansSerif","SansSerif"));

        menu.addSeparator();

        menu.add(new StyledEditorKit.ForegroundAction("Red", Color.red));
        menu.add(new StyledEditorKit.ForegroundAction("Green", Color.green));
        menu.add(new StyledEditorKit.ForegroundAction("Blue", Color.blue));
        menu.add(new StyledEditorKit.ForegroundAction("Black", Color.black));

        return menu;
    }

    private HashMap<Object, Action> createActionTable(JTextComponent textComponent) {
        HashMap<Object, Action> actions = new HashMap<Object, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (Action a : actionsArray) {
            actions.put(a.getValue(Action.NAME), a);
        }
        return actions;
    }

    private Action getActionByName(String name) {
        return actions.get(name);
    }

    // listeners
    public void addListeners(){
        //doc.addUndoableEditListener(um);
        //doc.addUndoableEditListener(new MyUndoableEditListener());
        //doc.addDocumentListener(new MyDocumentListener());
        textPane.getDocument().addUndoableEditListener(new MyUndoableEditListener());
        textPane.getDocument().addDocumentListener(new MyDocumentListener());
        textPane.getDocument().putProperty("name", "Text Field");
        int delay = 1000; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("1sec");

                str_timer =  textPane.getText();
                String str_remain;
                if (str_timer2.length() < str_timer.length()) {
                    str_remain = str_timer.replace(str_timer2,"");
                }
                else {
                    str_remain = str_timer2.replace(str_timer,"");

                }

                System.out.println("str_timer" + str_timer);
                System.out.println("str_timer2" + str_timer2);
                if (str_remain != "") {

                    undo_timer.add(str_remain);
                }
                if (undo_timer.size() >10) {
                    undo_timer.remove(0);
                }
                System.out.println("str_remain" + str_remain);

                System.out.println("undo_timer" + undo_timer);

                str_timer2 = str_timer;



            }
        };
        new Timer(delay, taskPerformer).start();
    }

    protected class MyUndoableEditListener implements UndoableEditListener {
        public void undoableEditHappened(UndoableEditEvent e) {
            um.addEdit(e.getEdit());
            changeLog.append(e.getEdit().getPresentationName() + newline);
            undoAction.updateUndoState();
            redoAction.updateRedoState();
        }
    }

    protected class MyDocumentListener implements DocumentListener {
        public <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
        {

            // Create a new ArrayList
            ArrayList<T> newList = new ArrayList<T>();

            // Traverse through the first list
            for (T element : list) {

                // If this element is not present in newList
                // then add it
                if (!newList.contains(element)) {

                    newList.add(element);
                }
            }

            // return the new list
            return newList;
        }
        public void insertUpdate(DocumentEvent e) { displayEditInfo(e);}
        public void removeUpdate(DocumentEvent e) { displayEditInfo(e);}
        public void changedUpdate(DocumentEvent e) { displayEditInfo(e);}
        private void displayEditInfo(DocumentEvent e) {
        }
    }




}
