import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class FuncFile {
    GUI gui;
    String fileName;
    String filePath;

    public FuncFile(GUI gui) {
        this.gui = gui;
    }

    public void newFile(){
        gui.textArea.setText("");
        gui.window.setTitle("Untitled");
        fileName = null;
        filePath = null;
    }

    public void open() {
        FileDialog fileDialog = new FileDialog(gui.window, "Open", FileDialog.LOAD);
        fileDialog.setVisible(true);

        if(fileDialog.getFile() != null) {
            fileName = fileDialog.getFile();
            filePath = fileDialog.getDirectory();
            gui.window.setTitle(fileName);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath + fileName));

            gui.textArea.setText("");

            String line = null;

            while ((line=br.readLine()) != null) {
                gui.textArea.append(line + "\n");
            }

            br.close();
        } catch (Exception e) {
            System.out.println("FILE NOT OPENED!");
        }
    }

    public void save() {
        if (fileName == null) {
            saveAs();
        } else {
            try {
                FileWriter fw = new FileWriter(filePath + fileName);
                fw.write(gui.textArea.getText());
                fw.close();
            } catch (Exception e) {
                System.out.println("SOMETHING WRONG!");
            }
        }
    }

    public void saveAs() {
        FileDialog fileDialog = new FileDialog(gui.window, "Save As", FileDialog.SAVE);
        fileDialog.setVisible(true);

        if (fileDialog.getFile() != null) {
            fileName = fileDialog.getFile();
            filePath = fileDialog.getDirectory();
            gui.window.setTitle(fileName);
        }

        try {
            FileWriter fw = new FileWriter(filePath + fileName);
            fw.write(gui.textArea.getText());
            fw.close();
        } catch (Exception e) {
            System.out.println("SOMETHING WRONG!");
        }
    }

    public void exit(){
        System.exit(0);
    }
}
