package jnotepad;
//
// Name: Whitney, Josh
// Project: #4
// Due: 12/4/15
// Course: cs-245-01-f15
//
// Description:
// MS Notepad clone.
// 


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import javax.swing.*;

class JNotepad implements ActionListener {
    JFrame frame;
    JFileChooser jfc;
    JScrollPane jsPane;
    JTextArea jta;
    String path;
    int findIdx;
    String findStr;
    JCheckBoxMenuItem jcbmiWordWrap;
    

    JNotepad() {
        findIdx = 0;
        frame = new JFrame("Untitled - Notepad");
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create scrollable text area
        jta = new JTextArea();
        jsPane = new JScrollPane(jta);        
        
        //Initialize FileChooser with filter.        
        jfc = new JFileChooser();  
        jfc.setFileFilter(new JavaFileFilter());

        JMenuBar menubar = new JMenuBar();
             
        menubar.add(fileMenu());
        menubar.add(editMenu());
        menubar.add(formatMenu());
        menubar.add(viewMenu());
        menubar.add(helpMenu());

        frame.add(jsPane);

        frame.setJMenuBar(menubar);

        frame.setVisible(true);
    }
    
    //Creates the File menu.
    private JMenu fileMenu(){
        JMenu jmFile = new JMenu("File");
        jmFile.setMnemonic(KeyEvent.VK_F);
        
        //Add new submenu
        JMenuItem jmiNew = new JMenuItem("New");
        jmiNew.setMnemonic(KeyEvent.VK_N);
        jmiNew.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N,
                        InputEvent.CTRL_MASK));
        jmiNew.addActionListener(ae ->{
            jta.setText(null);
            frame.setTitle("Untitled - Notepad");
        });
        jmFile.add(jmiNew);
        
        //Add "Open..." menu
        JMenuItem jmiOpen = new JMenuItem("Open...") ;
        jmiOpen.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O,
                        InputEvent.CTRL_MASK));
        jmiOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent le){
                int result = jfc.showOpenDialog(null);
                
                if(result == JFileChooser.APPROVE_OPTION){
                   loadFile();
                }                                    
            }
        });
        jmFile.add(jmiOpen);
        
        //Add save submenu.
        JMenuItem jmiSave = new JMenuItem("Save");
        jmiSave.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S,
                        InputEvent.CTRL_MASK));
        jmiSave.addActionListener(ae -> {
            if(frame.getTitle().equals("Untitled - Notepad")){
                int result = jfc.showSaveDialog(null);
            
                if(result == JFileChooser.APPROVE_OPTION){
                    if(!jfc.getSelectedFile().getName().endsWith(".java")
                            && !jfc.getSelectedFile().getName().endsWith(".txt")){
                        frame.setTitle(jfc.getSelectedFile().getName() + ".txt");
                        path = jfc.getSelectedFile().getPath() + ".txt";
                        saveFile(path);
                    }
                    else{
                        frame.setTitle(jfc.getSelectedFile().getName());
                        path = jfc.getSelectedFile().getPath();
                        saveFile(path);
                    }
                }                    
            }
            else
                saveFile(path);
            
        });
        jmFile.add(jmiSave);
        
        //Add saveAs submenu
        JMenuItem jmiSaveAs = new JMenuItem("Save as..."); 
        jmiSaveAs.addActionListener(ae -> {
            int result = jfc.showSaveDialog(null);
            
            if(result == JFileChooser.APPROVE_OPTION){
                if(!jfc.getSelectedFile().getName().endsWith(".java")
                        && !jfc.getSelectedFile().getName().endsWith(".txt")){
                    frame.setTitle(jfc.getSelectedFile().getName() + ".txt");
                    path = jfc.getSelectedFile().getPath() + ".txt";
                    saveFile(path);
                }
                else{
                    frame.setTitle(jfc.getSelectedFile().getName());
                    path = jfc.getSelectedFile().getPath();
                    saveFile(path);                    
                }
                
            }
        });
        jmFile.add(jmiSaveAs);
                         
        jmFile.addSeparator();
        
        JMenuItem jmiPageSetup = addMenuItem(jmFile, "Page Setup...");
        jmiPageSetup.setEnabled(false);
        
        JMenuItem jmiPrint = addMenuItem(jmFile, "Print...");
        jmiPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, 
                                    InputEvent.CTRL_MASK));
        jmiPrint.setEnabled(false);
        
        jmFile.addSeparator();
        JMenuItem jmiExit = addMenuItem(jmFile, "Exit", 'x');
        jmiExit.setAccelerator(KeyStroke.getKeyStroke("control X"));
        jmiExit.addActionListener(ae -> {
            System.out.println("Running...");
            System.exit(0);
        });
        
        return jmFile;
    }
    
    //Create the Edit menu.
    private JMenu editMenu(){
        JMenu jmEdit = new JMenu("Edit");
        jmEdit.setMnemonic('E');
        
        //Undo Sub menu (not Implemented)
        JMenuItem jmiUndo = addMenuItem(jmEdit, "Undo");
        jmiUndo.setEnabled(false);
        
        jmEdit.addSeparator();
        
        //Cut Submenu
        JMenuItem jmiCut = addMenuItem(jmEdit, "Cut");
        jmiCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 
                                        InputEvent.CTRL_MASK));        
        jmiCut.addActionListener(ae -> {
            jta.cut();
        });
        
        JMenuItem jmiCopy = addMenuItem(jmEdit, "Copy");
        jmiCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, 
                                        InputEvent.CTRL_MASK));
        jmiCopy.addActionListener(ae -> {
            jta.copy();
        });
        
        JMenuItem jmiPaste = addMenuItem(jmEdit, "Paste");
        jmiPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, 
                                        InputEvent.CTRL_MASK));
        jmiPaste.addActionListener(ae -> {
            jta.paste();
        });
        
        JMenuItem jmiDelete = addMenuItem(jmEdit, "Delete");
        jmiDelete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 
                                        InputEvent.CTRL_MASK));
        jmiDelete.addActionListener(ae -> {
            jta.replaceSelection(null);
        });
        
        jmEdit.addSeparator();
        
        JMenuItem jmiFind = addMenuItem(jmEdit, "Find...");
        jmiFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, 
                                        InputEvent.CTRL_MASK));
        jmiFind.addActionListener(ae -> {            
            Finder find = new Finder();
            find.setVisible(true);            
        });
        
        JMenuItem jmiFindNext = addMenuItem(jmEdit, "Find Next");
        jmiFindNext.addActionListener(al -> {
            find(findIdx + 1);
        });
        JMenuItem jmiReplace = addMenuItem(jmEdit, "Replace...");
        jmiReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, 
                                        InputEvent.CTRL_MASK));
        jmiReplace.setEnabled(false);
        
         //Added Goto menu
        JMenuItem jmiGoto = addMenuItem(jmEdit, "Goto...");
        jmiGoto.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
                                        InputEvent.CTRL_DOWN_MASK));
        jmiGoto.setEnabled(false);
        
        jmEdit.addSeparator();
        
        JMenuItem jmiSelectall = addMenuItem(jmEdit, "Select All");
        jmiSelectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, 
                                        InputEvent.CTRL_MASK));
        jmiSelectall.addActionListener(ae -> {
            jta.selectAll();
        });
        
        JMenuItem jmiTimeDate = addMenuItem(jmEdit, "Time/Date");
        jmiTimeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
        jmiTimeDate.addActionListener(ae -> {
            DateFormat dateFormat =  new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
            Date date = new Date();
            jta.insert(dateFormat.format(date), jta.getCaretPosition());
        });
        
        return jmEdit;
    }
    
    //Create the Format menu.
    private JMenu formatMenu() {
        JMenu jmFormat = new JMenu("Format");
        jmFormat.setMnemonic(KeyEvent.VK_O);

        jcbmiWordWrap = new JCheckBoxMenuItem("Word Wrap");
        jcbmiWordWrap.setEnabled(false);
        jcbmiWordWrap.setMnemonic('W');
        jcbmiWordWrap.addActionListener(al -> {
            if(jcbmiWordWrap.getState()){
                jta.setLineWrap(true);
                jta.setWrapStyleWord(true);
            }
            else{
                jta.setWrapStyleWord(false);
                jta.setLineWrap(false);
            }
                
        });
        jmFormat.add(jcbmiWordWrap);
        JMenuItem jmiFont = addMenuItem(jmFormat, "Font...", 'F');
        jmiFont.addActionListener(al -> {
            FontChooser fonts = new FontChooser(frame, false);
            fonts.setVisible(true);
            
        });
        
        return jmFormat;
    }
    
    //Create the view Menu.
    private JMenu viewMenu() {
        JMenu jmView = new JMenu("View");
        jmView.setMnemonic('V');
        JCheckBoxMenuItem jcbmiStatusBar = new JCheckBoxMenuItem("Status Bar");
        jcbmiStatusBar.setMnemonic('S');
        jcbmiStatusBar.setEnabled(true);
        jmView.add(jcbmiStatusBar);
        jcbmiStatusBar.addActionListener(al -> {
            if(jcbmiStatusBar.getState())
                jcbmiWordWrap.setEnabled(true);
            else jcbmiWordWrap.setEnabled(false);
        });
        
        return jmView;
    }
    
    //Create the Help menu.
    private JMenu helpMenu() {
        JMenu jmHelp = new JMenu("Help");
        jmHelp.setMnemonic('H');
        JMenuItem jmiViewHelp = addMenuItem(jmHelp, "View Help", 'H');
        jmiViewHelp.setEnabled(false);
        jmHelp.addSeparator();
        JMenuItem jmiJNotepad = addMenuItem(jmHelp, "About JNotepad");
        jmiJNotepad.addActionListener(al -> {
            JOptionPane.showMessageDialog(frame, "(c) Joshua Whitney");
        });
        
        return jmHelp;
    }
    
    //Loads a file.
    private void loadFile(){
        FileReader fr;
        try{
            if(jfc.getSelectedFile().getName().endsWith(".java")
                        || jfc.getSelectedFile().getName().endsWith(".txt")){
                frame.setTitle(jfc.getSelectedFile().getName());
                path = jfc.getSelectedFile().getPath();
                fr = new FileReader(jfc.getSelectedFile().getAbsolutePath());
                jta.read(fr, null);
                fr.close();    
            }
            else{
                JOptionPane.showMessageDialog(jfc, 
                        "Incompatible file type.");                
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(jfc, "Error occured loading file.");                
        }                            
            
    }
    
    //Save a file.
    private void saveFile(String p) {
        FileWriter fw;
        try{
            fw = new FileWriter(p);
            jta.write(fw);
            fw.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(jfc, "Error occured saving file.");
        }
    }
    
    private void find(int start){
        String str = jta.getText();
        int idx = str.indexOf(findStr, start);
        //Match found
        if(idx > -1) {
            jta.setCaretPosition(idx);
            findIdx = idx;
        }
        else
            JOptionPane.showMessageDialog(frame, "String not found.");
        jta.requestFocusInWindow();
        
    }

    private static JMenuItem addMenuItem(JMenu jm, String text, char mmemonic) {        
        JMenuItem jmi = new JMenuItem(text, mmemonic);
        jm.add(jmi);

        return jmi;
    }
    
    private static JMenuItem addMenuItem(JMenu jm, String text){
        JMenuItem jmi = new JMenuItem(text);
        jm.add(jmi);

        return jmi;
    }

    public void actionPerformed(ActionEvent ae) {
        // Get the action command from the menu selection. 
        String comStr = ae.getActionCommand();
        
    }

    public static void main(String args[]) {
        // Create the frame on the event dispatching thread.   
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new JNotepad();
            }
        });
    }
       
    private class JavaFileFilter extends FileFilter{
        public boolean accept(File file){
            if(file.getName().endsWith(".java")
                || file.getName().endsWith(".txt")
                || file.isDirectory())
                return true;
            else
                return false;           
        }
        
        public String getDescription(){
            return "Java Source Code / Text Files";
        }

    }
    
    private class Finder extends javax.swing.JFrame {

    /**
     * Creates new form FInder
     */
    public Finder() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        fDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        
        

        fDialog.setMinimumSize(new java.awt.Dimension(200, 200));        

        javax.swing.GroupLayout fDialogLayout = new javax.swing.GroupLayout(fDialog.getContentPane());
        fDialog.getContentPane().setLayout(fDialogLayout);
        fDialogLayout.setHorizontalGroup(
            fDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        fDialogLayout.setVerticalGroup(
            fDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Find what: ");

        jButton1.setText("Find Next");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();        
    }// </editor-fold>                        

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        setVisible(false);
    }                                        

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        findStr = jTextField1.getText();
        findIdx = 0;
        find(findIdx);
    }                                        

    

    // Variables declaration - do not modify                     
    private javax.swing.JDialog fDialog;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
    
    public class FontChooser extends javax.swing.JDialog {
    
    GraphicsEnvironment ge;
    Font[] fonts;
    JList fontChoices;
    JList fontStyles;
    JList fontSizes;
    

    /**
     * Creates new form Font
     */
    public FontChooser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        fonts = ge.getAllFonts();
        fontChoices = new JList(fonts);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jsPaneStyle = new javax.swing.JScrollPane();
        jsPaneSize = new javax.swing.JScrollPane();
        jtFieldFont = new javax.swing.JTextField();
        jtFieldStyle = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane(fontChoices);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Font");

        jLabel2.setText("Font Style");

        jLabel3.setText("Size");

        jButton1.setText("OK");

        jButton2.setText("Cancel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(26, 26, 26))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jtFieldFont, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jsPaneStyle)
                    .addComponent(jtFieldStyle, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jsPaneSize)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtFieldFont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtFieldStyle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsPaneStyle, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jsPaneSize, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addGap(16, 16, 16))
        );

        pack();
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JScrollPane jsPaneSize;
    private javax.swing.JScrollPane jsPaneStyle;
    private javax.swing.JTextField jtFieldFont;
    private javax.swing.JTextField jtFieldStyle;
    // End of variables declaration                   
}
        
}





