import java.awt.*;
import javax.swing.*;
//import javax.swing.filechooser.FileSystemView;
import java.io.File;
//import com.sun.lwuit.Component;
//import com.sun.lwuit.Font;
//import com.sun.lwuit.Image;
//import com.sun.lwuit.Label;
//import com.sun.lwuit.List;
//import com.sun.lwuit.list.ListCellRenderer;
//import com.sun.lwuit.plaf.Border;
import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.Map;
//import java.util.HashMap;
//import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
//import javax.swing.JScrollPane;
//import javax.swing.SwingUtilities;
import java.awt.image.*;
//import javax.swing.*;
//import java.util.*;
//import java.awt.*;
//import java.awt.font.TextAttribute;
import java.nio.file.*;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.nio.file.attribute.BasicFileAttributes;
//import java.text.AttributedCharacterIterator;
//import java.text.AttributedString;
import org.apache.commons.io.FileUtils;
//import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.nio.file.attribute.FileTime;
//import java.util.Collections;
//import java.util.Arrays;
import java.text.Collator;
import org.apache.commons.io.comparator.SizeFileComparator;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import java.util.ArrayList;
import java.util.Arrays;
//import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
//import javax.swing.plaf.BorderUIResource;
//import org.netbeans.lib.awtextra.AbsoluteLayout;
//import java.awt.Dimension;
//import java.awt.Toolkit;
import javax.imageio.ImageIO;
//import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.event.*;
//import javax.swing.border.MatteBorder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
//import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
//import java.nio.file.attribute.FileAttribute;

enum AttrSetting
{
    Size, DateModified;
}

enum SortType
{
    Name, Size, DateModified;
}

enum FocusState
{
    focused, Unfocused, KeyBoardInitialized;
}

enum USBState
{
    noneDetected, USBdetected;
}

enum SelectionMode
{
    singleSelect, MultiSelect;
}

public class gui extends javax.swing.JFrame {
    String homeDirectory, botHomeDirectory, topHomeDirectory, bottomWindowCurrentDirectory, topWindowCurrentDirectory;
    String[] bottomDirectories; int thisBottomDirectorySelectionNumber, lastBottomDirectorySelectionNumber;
    String[] topDirectories; BasicFileAttributes[] topAttr, botAttr, campatibleAttr;  int thisTopDirectorySelectionNumber, lastTopDirectorySelectionNumber;
    String topDirectoryFileSearchString, bottomDirectoryFileSearchString;
    String[] compatibleDirectories;
    AttrSetting topAttrSetting, botAttrSetting;
    Map <Integer, Integer> keyPressed;
    Map <String, Integer> fileType;
    String[] fileTypes = {".txt", ".png"};
    SortType topSortType, botSortType;
    Integer[] sortTypeSelectIntTop; Integer sortTypeIter;
    Integer toSort = 1;
    File[] savedFilesTop, savedFilesBot;
    String savedTopCurrentDirectory, lastTopCurrentDirectory, lastUSBDirectory;
    String SizeText = "Size (mb)", NameText = "Name", DateText = "Date";
    Rectangle topBound, botBound;
    FocusState textFocusState;
    USBState usbState;
    SelectionMode selectionMode;
    Robot bot;
    /**
     * Creates new form gui
     */
    public gui() {
        try{
            bot = new Robot();
        }
        catch(Exception e){
            
        }
        int linesOfOutput = 0;
        try {
          Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","pidof florence"});
          BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
          String line = input.readLine();
          while (line != null)
          {
            linesOfOutput = (int)line.chars().count();
          }
        } catch (Exception err) {
          System.out.println(err);
        } 
        if (linesOfOutput == 0)    
        try{
         //   Runtime.getRuntime().exec("florence -u /home/wilks/Music/Can/config/florence2.conf");
            Process p1 = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence -u /home/wilks/Music/Can/config/florence.conf"});
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence hide"});
            //  textFocusState = textFocusState.KeyBoardInitialized;
          }
          catch (Exception e){
             // textFocusState = textFocusState.KeyBoardInitialized;
          }
        initComponents(); 
        selectionMode = selectionMode.singleSelect;
        determineInitialDirectories();
        fileType = new HashMap<String, Integer>(); 
        botCombo.removeAllItems();
        botSortType = SortType.Size;
        botAttrSetting = AttrSetting.Size;
      //  homeDirectory = new File("").getAbsolutePath();
      //  homeDirectory = "/home/wilks/Music"; botHomeDirectory = homeDirectory;
     //   bottomWindowCurrentDirectory = homeDirectory;
        lastBottomDirectorySelectionNumber = thisBottomDirectorySelectionNumber = -1;
        setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
        upOneLevelBottom.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        newFolderBottom.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
        homeIconBottom.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
        sortButtonBot.setIcon(UIManager.getIcon( "Table.ascendingSortIcon" ));
        matchDirBottom.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        bottomTextField.setText("");
        bottomScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        bottomScrollPane.getVerticalScrollBar().setModel( AttributeScrollPaneBot.getVerticalScrollBar().getModel() );
        AttributeScrollPaneBot.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        bottomScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        botSortText.setText(SizeText);
        
        topCombo.removeAllItems();
        topSortType = SortType.Size;
        topAttrSetting = AttrSetting.Size;
   //     topWindowCurrentDirectory = homeDirectory; topHomeDirectory = homeDirectory;
        thisTopDirectorySelectionNumber = lastTopDirectorySelectionNumber = -1;
        setDirectoryListTop(topWindowCurrentDirectory, topWindow);
        upOneLevelTop.setIcon(UIManager.getIcon("FileChooser.upFolderIcon"));
        newFolderTop.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
        homeIconTop.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
        sortButtonTop.setIcon(UIManager.getIcon( "Table.ascendingSortIcon" ));
        matchDirTop.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        topTextField.setText("");
        topScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        topScrollPane.getVerticalScrollBar().setModel( AttributeScrollPane.getVerticalScrollBar().getModel() );
        AttributeScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        topScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
        topSortText.setText(SizeText);
        
        textFocusState = FocusState.Unfocused;
        
        keyPressed = new HashMap<Integer, Integer>();
        keyPressed.put(17, 0);
      //  topCombo.getParent().setBackground( Color.black );
    }
    
    private void determineInitialDirectories(){
        homeDirectory = "/home/wilks/Music";
        
        File folder = new File("/home/wilks/Music/Windows10");
        File[] files = folder.listFiles();
        String[] stringBuffer;
        
        if (!new File("/home/wilks/Music/Can/config/bookmarks.txt").exists())
            {
                try {
                            File file = new File("/home/wilks/Music/Can/config/bookmarks.txt");
                            FileWriter fileWriter = new FileWriter(file);
                            //fileWriter.write("");
                            fileWriter.flush();
                            fileWriter.close();
                    } catch (IOException e) {
                    }
                topWindowCurrentDirectory = bottomWindowCurrentDirectory = homeDirectory;
            }
        
        try {
                        File file = new File("/home/wilks/Music/Can/config/bookmarks.txt");
                        FileReader fileReader = new FileReader(file);
                        BufferedReader bufferedReader = new BufferedReader(fileReader);
                        stringBuffer = new String[4];
                        String line;
                        Integer iter = 0;
                        while ((line = bufferedReader.readLine()) != null) {
                                stringBuffer[iter] = line;
                                iter++;
                        }
                        bufferedReader.close();
                } catch (IOException e) {
                        stringBuffer = new String[0];
                }
        lastTopCurrentDirectory = stringBuffer[0];
        if (stringBuffer[3] != null)
            lastUSBDirectory = stringBuffer[3];
        if (stringBuffer[2] != null && stringBuffer[2].chars().count() >1)
            homeDirectory = stringBuffer[2];
        topHomeDirectory = homeDirectory;
        
        if (files.length == 0){
                if (stringBuffer[0] != null && stringBuffer[0].chars().count() >1 && new File(stringBuffer[0]).exists())
                    topWindowCurrentDirectory = stringBuffer[0];
                else
                    topWindowCurrentDirectory = homeDirectory;
                    usbState = usbState.noneDetected;
            }
        else {
            usbState = usbState.USBdetected;
            if (stringBuffer[3] != null && stringBuffer[3].chars().count() >1 && new File(stringBuffer[3]).exists())
                topWindowCurrentDirectory = stringBuffer[3];
            else 
                topWindowCurrentDirectory = files[0].toString();
            topHomeDirectory = files[0].toString();
        }
        if (stringBuffer[1] != null && stringBuffer[1].chars().count() >1 && new File(stringBuffer[1]).exists())
            bottomWindowCurrentDirectory = stringBuffer[1];
        else
            bottomWindowCurrentDirectory = homeDirectory;
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();
        upOneLevelBottom = new javax.swing.JButton();
        homeIconBottom = new javax.swing.JButton();
        newFolderBottom = new javax.swing.JButton();
        topScrollPane = new javax.swing.JScrollPane();
        topWindow = new javax.swing.JList<>();
        upOneLevelTop = new javax.swing.JButton();
        homeIconTop = new javax.swing.JButton();
        newFolderTop = new javax.swing.JButton();
        topTextField = new javax.swing.JTextField();
        bottomTextField = new javax.swing.JTextField();
        transfer = new javax.swing.JButton();
        AttributeScrollPane = new javax.swing.JScrollPane();
        topRightAttributeList = new javax.swing.JList<>();
        AttributeScrollPaneBot = new javax.swing.JScrollPane();
        bottomRightAttributeList = new javax.swing.JList<>();
        bottomScrollPane = new javax.swing.JScrollPane();
        bottomWindow = new javax.swing.JList<>();
        topCombo = new javax.swing.JComboBox<>();
        topSortText = new javax.swing.JLabel();
        sortButtonTop = new javax.swing.JButton();
        botCombo = new javax.swing.JComboBox<>();
        sortButtonBot = new javax.swing.JButton();
        botSortText = new javax.swing.JLabel();
        delete = new javax.swing.JButton();
        rename = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        multiSelect = new javax.swing.JButton();
        matchDirTop = new javax.swing.JButton();
        matchDirBottom = new javax.swing.JButton();
        setHome = new javax.swing.JButton();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(6);
        setUndecorated(true);
        setResizable(false);
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        upOneLevelBottom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                upOneLevelBottomMouseClicked(evt);
            }
        });

        homeIconBottom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeIconBottomMouseClicked(evt);
            }
        });

        newFolderBottom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newFolderBottomMouseClicked(evt);
            }
        });

        topScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        topWindow.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(0, 0, 0)));
        topWindow.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        topWindow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topWindowMouseClicked(evt);
            }
        });
        topWindow.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                topWindowKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                topWindowKeyReleased(evt);
            }
        });
        topScrollPane.setViewportView(topWindow);

        upOneLevelTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                upOneLevelTopMouseClicked(evt);
            }
        });

        homeIconTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeIconTopMouseClicked(evt);
            }
        });

        newFolderTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                newFolderTopMouseClicked(evt);
            }
        });

        topTextField.setText("jTextField1");
        topTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                topTextFieldFocusGained(evt);
            }
        });
        topTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                topTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                topTextFieldKeyReleased(evt);
            }
        });

        bottomTextField.setText("jTextField1");
        bottomTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bottomTextFieldFocusGained(evt);
            }
        });
        bottomTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bottomTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bottomTextFieldKeyReleased(evt);
            }
        });

        transfer.setText("Transfer");
        transfer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transferMouseClicked(evt);
            }
        });

        AttributeScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        topRightAttributeList.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        topRightAttributeList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        AttributeScrollPane.setViewportView(topRightAttributeList);

        AttributeScrollPaneBot.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        bottomRightAttributeList.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(0, 0, 0)));
        bottomRightAttributeList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        AttributeScrollPaneBot.setViewportView(bottomRightAttributeList);

        bottomScrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        bottomWindow.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(0, 0, 0)));
        bottomWindow.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        bottomWindow.setAutoscrolls(false);
        bottomWindow.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bottomWindowMouseClicked(evt);
            }
        });
        bottomWindow.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bottomWindowKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bottomWindowKeyReleased(evt);
            }
        });
        bottomScrollPane.setViewportView(bottomWindow);

        topCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        topCombo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        topCombo.setOpaque(false);
        topCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                topComboItemStateChanged(evt);
            }
        });
        topCombo.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                topComboHierarchyChanged(evt);
            }
        });
        topCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topComboMouseClicked(evt);
            }
        });
        topCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                topComboActionPerformed(evt);
            }
        });
        topCombo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                topComboPropertyChange(evt);
            }
        });

        topSortText.setText("jLabel1");
        topSortText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        topSortText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                topSortTextMouseClicked(evt);
            }
        });

        sortButtonTop.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        sortButtonTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sortButtonTopMouseClicked(evt);
            }
        });

        botCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        botCombo.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 0, 0)));
        botCombo.setOpaque(false);
        botCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                botComboItemStateChanged(evt);
            }
        });
        botCombo.addHierarchyListener(new java.awt.event.HierarchyListener() {
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                botComboHierarchyChanged(evt);
            }
        });
        botCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botComboMouseClicked(evt);
            }
        });
        botCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botComboActionPerformed(evt);
            }
        });
        botCombo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                botComboPropertyChange(evt);
            }
        });

        sortButtonBot.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        sortButtonBot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sortButtonBotMouseClicked(evt);
            }
        });

        botSortText.setText("jLabel1");
        botSortText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        botSortText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                botSortTextMouseClicked(evt);
            }
        });

        delete.setText("Delete");
        delete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                deleteMouseClicked(evt);
            }
        });
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        rename.setText("Rename");
        rename.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                renameMouseClicked(evt);
            }
        });

        Back.setText("Back");
        Back.setMaximumSize(new java.awt.Dimension(118, 25));
        Back.setMinimumSize(new java.awt.Dimension(118, 25));
        Back.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackMouseClicked(evt);
            }
        });

        multiSelect.setText("Multi Select");
        multiSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                multiSelectMouseClicked(evt);
            }
        });

        matchDirTop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                matchDirTopMouseClicked(evt);
            }
        });

        matchDirBottom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                matchDirBottomMouseClicked(evt);
            }
        });

        setHome.setText("Set Home");
        setHome.setMaximumSize(new java.awt.Dimension(118, 25));
        setHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setHomeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(topCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(upOneLevelTop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(homeIconTop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(newFolderTop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(matchDirTop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(sortButtonTop, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(topSortText))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(botCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(upOneLevelBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(homeIconBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(newFolderBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(matchDirBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortButtonBot, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(botSortText))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(topTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(topScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(AttributeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(bottomScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(AttributeScrollPaneBot, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(multiSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setHome, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rename, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(transfer, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(bottomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(topCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(upOneLevelTop, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(homeIconTop, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(newFolderTop, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sortButtonTop, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(matchDirTop, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(topSortText)
                                .addGap(8, 8, 8)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(transfer)
                        .addGap(6, 6, 6)
                        .addComponent(delete)
                        .addGap(6, 6, 6)
                        .addComponent(rename))
                    .addComponent(topScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AttributeScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(topTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(upOneLevelBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(homeIconBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(newFolderBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(botSortText))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sortButtonBot, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(matchDirBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(botCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(AttributeScrollPaneBot, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(multiSelect)
                        .addGap(6, 6, 6)
                        .addComponent(setHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bottomScrollPane))
                .addGap(5, 5, 5)
                .addComponent(bottomTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void homeIconBottomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeIconBottomMouseClicked
        setDirectoryListBottom(homeDirectory, bottomWindow);
        bottomWindowCurrentDirectory = homeDirectory;
        lastBottomDirectorySelectionNumber = thisBottomDirectorySelectionNumber = -1;
        bottomTextField.setText("");
    }//GEN-LAST:event_homeIconBottomMouseClicked

    
    private void upOneLevelBottomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_upOneLevelBottomMouseClicked
        bottomWindowCurrentDirectory = Paths.get(bottomWindowCurrentDirectory).getParent().toString();
        setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
        thisBottomDirectorySelectionNumber = lastBottomDirectorySelectionNumber = -1;
        bottomTextField.setText("");
      //  jTextArea1.append( Paths.get(bottomWindowCurrentDirectory).getParent().toString() );
    }//GEN-LAST:event_upOneLevelBottomMouseClicked

    private void topWindowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_topWindowMouseClicked
        if (!evt.isControlDown() && selectionMode == selectionMode.singleSelect) //runs if ctrl is not pressed.
        {
            String clickedDirectory;
            if (topTextField.getText().chars().count() == 0)
                clickedDirectory =  topDirectories[topWindow.getSelectedIndex()];
            else
                clickedDirectory = compatibleDirectories[topWindow.getSelectedIndex()];
        //    debugField.append( String.valueOf(topWindow.getSelectedIndex() ) );
            lastTopDirectorySelectionNumber = thisTopDirectorySelectionNumber;
            thisTopDirectorySelectionNumber = topWindow.getSelectedIndex();
            if (thisTopDirectorySelectionNumber == lastTopDirectorySelectionNumber)
            {
                thisTopDirectorySelectionNumber = lastTopDirectorySelectionNumber = -1;
               
                if (! new File(clickedDirectory).isFile()){
                //if (!clickedDirectory.contains(".")){
                topTextField.setText("");
                setDirectoryListTop(clickedDirectory, topWindow);
                topWindowCurrentDirectory = clickedDirectory;
                }
            }
        }
        bottomWindow.clearSelection();
        thisBottomDirectorySelectionNumber = lastBottomDirectorySelectionNumber = -1;
    }//GEN-LAST:event_topWindowMouseClicked

    private void upOneLevelTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_upOneLevelTopMouseClicked
        topWindowCurrentDirectory = Paths.get(topWindowCurrentDirectory).getParent().toString();
        setDirectoryListTop(topWindowCurrentDirectory, topWindow);
        thisTopDirectorySelectionNumber = lastTopDirectorySelectionNumber = -1;
        topTextField.setText("");
    }//GEN-LAST:event_upOneLevelTopMouseClicked

    private void homeIconTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeIconTopMouseClicked
        setDirectoryListTop(topHomeDirectory, topWindow);
        topWindowCurrentDirectory = topHomeDirectory;
        thisTopDirectorySelectionNumber = lastTopDirectorySelectionNumber = -1;
        topTextField.setText("");
    }//GEN-LAST:event_homeIconTopMouseClicked

    private void topTextFieldKeyPressedHelper(){
        topDirectoryFileSearchString = topTextField.getText();
        if (topDirectoryFileSearchString != "")
        {
            compatibleDirectories = new String[topDirectories.length];
            campatibleAttr = new BasicFileAttributes[topDirectories.length];
            int iterator = 0;
            for (int x = 0; x < topDirectories.length; x++)
            {
                if (topDirectories[x].toLowerCase().contains(topDirectoryFileSearchString.toLowerCase() ))
                {
                    compatibleDirectories[iterator] = topDirectories[x];
                    campatibleAttr[iterator] = topAttr[x];
                    iterator++;
                }
            }
            DefaultListModel listModel = new DefaultListModel();
            DefaultListModel listModelAttr = new DefaultListModel();
            for (int x = 0; x < iterator; x++)
            {
                listModel.addElement(compatibleDirectories[x].toString().substring( compatibleDirectories[x].toString().lastIndexOf('/') + 1 ));
                if (topAttrSetting == topAttrSetting.Size)
                {
                    if (new File(compatibleDirectories[x]).isFile())
                        listModelAttr.addElement( processSize( (int)campatibleAttr[x].size() ) );
                    else
                        listModelAttr.addElement( "D" );
                }
                else{
                    listModelAttr.addElement( processDateModified( campatibleAttr[x].lastModifiedTime() ) );
                }
                    
            }
            topRightAttributeList.setModel(listModelAttr);
            topWindow.setModel(listModel);
        }
        else
        {
            DefaultListModel listModel = new DefaultListModel();
            listModel.addElement(topDirectories);
            topWindow.setModel(listModel);
            
            DefaultListModel listModelAttr = new DefaultListModel();
            for (int x = 0; x < topAttr.length; x++)
            {
                if (topAttrSetting == topAttrSetting.Size)
                {
                    if (new File(topDirectories[x]).isFile())
                        listModelAttr.addElement( processSize( (int)topAttr[x].size() ) );
                    else
                        listModelAttr.addElement("D");
                }
                else if (topAttrSetting == topAttrSetting.DateModified)
                    listModelAttr.addElement( topAttr[x].lastModifiedTime() );
            }
            topRightAttributeList.setModel( listModelAttr );
        }
       // debugField.append( String.valueOf(evt.getKeyCode()) );
    }
    
    private void topTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_topTextFieldKeyPressed
        topTextFieldKeyPressedHelper();
      /*  if (textFocusState == textFocusState.KeyBoardInitialized){
            if (evt.getKeyCode() == 13)
                try{
                    Runtime.getRuntime().exec("killall florence");
                  //  Runtime.getRuntime().exec("florence hide");
                    textFocusState = textFocusState.Unfocused;
                }
                catch (Exception e){

                }    
        } */
    }//GEN-LAST:event_topTextFieldKeyPressed

    private void bottomTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bottomTextFieldKeyPressed
        bottomDirectoryFileSearchString = bottomTextField.getText();
        if (bottomDirectoryFileSearchString != "")
        {
            compatibleDirectories = new String[bottomDirectories.length];
            campatibleAttr = new BasicFileAttributes[bottomDirectories.length];
            int iterator = 0;
            for (int x = 0; x < bottomDirectories.length; x++)
            {
                if (bottomDirectories[x].toLowerCase().contains(bottomDirectoryFileSearchString.toLowerCase()))
                {
                    compatibleDirectories[iterator] = bottomDirectories[x];
                    campatibleAttr[iterator] = botAttr[x];
                    iterator++;
                }
            }
            DefaultListModel listModel = new DefaultListModel();
            DefaultListModel listModelAttr = new DefaultListModel();
            for (int x = 0; x < iterator; x++)
            {
                listModel.addElement(compatibleDirectories[x].toString().substring( compatibleDirectories[x].toString().lastIndexOf('/') + 1 ));
                if (topAttrSetting == topAttrSetting.Size)
                {
                    if (new File(compatibleDirectories[x]).isFile())
                        listModelAttr.addElement( processSize( (int)campatibleAttr[x].size() ) );
                    else
                        listModelAttr.addElement( "D" );
                }
                else{
                    listModelAttr.addElement( processDateModified( campatibleAttr[x].lastModifiedTime() ) );
                }
                    
            }
            bottomRightAttributeList.setModel(listModelAttr);
            bottomWindow.setModel(listModel);
        }
        else
        {
            DefaultListModel listModel = new DefaultListModel();
            listModel.addElement(bottomDirectories);
            bottomWindow.setModel(listModel);
            
            DefaultListModel listModelAttr = new DefaultListModel();
            for (int x = 0; x < botAttr.length; x++)
            {
                if (botAttrSetting == botAttrSetting.Size)
                {
                    if (new File(bottomDirectories[x]).isFile())
                        listModelAttr.addElement( processSize( (int)botAttr[x].size() ) );
                    else
                        listModelAttr.addElement("D");
                }
                else if (botAttrSetting == botAttrSetting.DateModified)
                    listModelAttr.addElement( botAttr[x].lastModifiedTime() );
            }
            bottomRightAttributeList.setModel( listModelAttr );
        }
      //  debugField.append( String.valueOf(evt.getKeyCode()) );
    }//GEN-LAST:event_bottomTextFieldKeyPressed

    private void topTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_topTextFieldKeyReleased
        topTextFieldKeyPressed(evt);
    }//GEN-LAST:event_topTextFieldKeyReleased

    private void bottomTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bottomTextFieldKeyReleased
        bottomTextFieldKeyPressed(evt);
    }//GEN-LAST:event_bottomTextFieldKeyReleased

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
     //   keyPressed.put(evt.getKeyCode(), 1);
        thisTopDirectorySelectionNumber = lastTopDirectorySelectionNumber = -1;
        thisBottomDirectorySelectionNumber = lastBottomDirectorySelectionNumber = -1;
      //  debugField.append( String.valueOf(evt.getKeyCode()) );
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
       // keyPressed.put(evt.getKeyCode(), 0);
    }//GEN-LAST:event_formKeyReleased

    private void topWindowKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_topWindowKeyPressed
        formKeyPressed(evt);
    }//GEN-LAST:event_topWindowKeyPressed

    private void topWindowKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_topWindowKeyReleased
      //  formKeyReleased(evt);
    }//GEN-LAST:event_topWindowKeyReleased

    private void transferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transferMouseClicked
        int[] list;
        String[] selectedDirectories;
        String destinationDirectory;
        if (topWindow.getSelectedIndices().length > bottomWindow.getSelectedIndices().length){
            list = topWindow.getSelectedIndices();
            if (topTextField.getText().chars().count() > 0)
                selectedDirectories = compatibleDirectories;
            else
                selectedDirectories = topDirectories;
            destinationDirectory = bottomWindowCurrentDirectory;
        }
        else{
            list = bottomWindow.getSelectedIndices();
            if (bottomTextField.getText().chars().count() > 0)
                selectedDirectories = compatibleDirectories;
            else
                selectedDirectories = bottomDirectories;
            destinationDirectory = topWindowCurrentDirectory;
        }
        String[] selectedFiles = new String[list.length];
        for (int x = 0; x < list.length; x++)
        {   if (selectedDirectories[ list[x] ] != null)
                selectedFiles[x] = selectedDirectories[ list[x] ];
          //  debugField.setText( String.valueOf( list[x] ) );
        }
        
        for (int x = 0; x < selectedFiles.length; x++)
        {
            if (selectedFiles[x].contains("."))
            {
                try{
                Files.copy(Paths.get(selectedFiles[x]) , Paths.get(destinationDirectory + "/" + selectedFiles[x].substring( selectedFiles[x].lastIndexOf("/") + 1 ) ), StandardCopyOption.REPLACE_EXISTING);
                }
                catch(Exception e){
                 //   debugField.setText( selectedFiles[x].substring( selectedFiles[x].lastIndexOf("/") + 1 ) + " popshot ");
                }
            }
            else
            {
                try{
                FileUtils.copyDirectory(new File(selectedFiles[x]), new File(destinationDirectory + selectedFiles[x].substring( selectedFiles[x].lastIndexOf("/") )));
                }
                catch(Exception e){
                  //  debugField.setText( selectedFiles[x].substring( selectedFiles[x].lastIndexOf("/") + 1 ) + " popshot ");
                }
            }
        }
        if (destinationDirectory == topWindowCurrentDirectory)
            setDirectoryListTop(destinationDirectory, topWindow);
        else
            setDirectoryListBottom(destinationDirectory, bottomWindow);
        
    }//GEN-LAST:event_transferMouseClicked

    private void bottomWindowMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bottomWindowMouseClicked
        if (!evt.isControlDown()) //runs if ctrl is not pressed.
        {
            String clickedDirectory;
            if (bottomTextField.getText().chars().count() == 0)
                clickedDirectory =  bottomDirectories[bottomWindow.getSelectedIndex()];
            else
                clickedDirectory = compatibleDirectories[bottomWindow.getSelectedIndex()];
          //  debugField.append( String.valueOf(bottomWindow.getSelectedIndex() ) );
            lastBottomDirectorySelectionNumber = thisBottomDirectorySelectionNumber;
            thisBottomDirectorySelectionNumber = bottomWindow.getSelectedIndex();
            if (thisBottomDirectorySelectionNumber == lastBottomDirectorySelectionNumber)
            {
                thisBottomDirectorySelectionNumber = lastBottomDirectorySelectionNumber = -1;
               
                if (! new File(clickedDirectory).isFile()){
                //if (!clickedDirectory.contains(".")){
                bottomTextField.setText("");
                setDirectoryListBottom(clickedDirectory, bottomWindow);
                bottomWindowCurrentDirectory = clickedDirectory;
                }
            }
        }
        topWindow.clearSelection();
        thisTopDirectorySelectionNumber = lastTopDirectorySelectionNumber = -1;
    }//GEN-LAST:event_bottomWindowMouseClicked

    private void bottomWindowKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bottomWindowKeyPressed
        formKeyPressed(evt);
    }//GEN-LAST:event_bottomWindowKeyPressed

    private void bottomWindowKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bottomWindowKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_bottomWindowKeyReleased

    private void topComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_topComboMouseClicked
        
    }//GEN-LAST:event_topComboMouseClicked

    private void topComboPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_topComboPropertyChange
        
    }//GEN-LAST:event_topComboPropertyChange

    private void topComboHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_topComboHierarchyChanged
        
    }//GEN-LAST:event_topComboHierarchyChanged

    private void topComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_topComboActionPerformed
        
    }//GEN-LAST:event_topComboActionPerformed

    private void topComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_topComboItemStateChanged
        if (topCombo.getSelectedIndex() != topCombo.getItemCount()-1)
        {
            setDirectoryListTop( topCombo.getSelectedItem().toString() , topWindow);
            topWindowCurrentDirectory = topCombo.getSelectedItem().toString();
        }
    }//GEN-LAST:event_topComboItemStateChanged

    private void topSortTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_topSortTextMouseClicked
        
        if( topSortText.getText() == SizeText )
        {
         //   topSortType = SortType.DateModified;
            topAttrSetting = AttrSetting.DateModified;
            topSortText.setText(DateText);
            
            topBound = topSortText.getBounds();
            Container parent = topSortText.getParent();
            parent.remove(topSortText);
            topSortText.setBorder(new EtchedBorder());
            topSortText.setBounds(topBound.x + 9, topBound.y, topBound.width-25, topBound.height);
            parent.add(topSortText);
            parent.validate();
            parent.repaint(); 
        }
        else if ( topSortText.getText() == DateText )
        {
         //   topSortType = SortType.Name;
            topSortText.setText(NameText);
            
            Rectangle bound = topSortText.getBounds();
            Container parent = topSortText.getParent();
            parent.remove(topSortText);
            topSortText.setBorder(new EtchedBorder());
            topSortText.setBounds(bound.x-2, bound.y, bound.width+5, bound.height);
            parent.add(topSortText);
            parent.validate();
            parent.repaint(); 
        }
        else
        {
        //    topSortType = SortType.Size;
            topAttrSetting = AttrSetting.Size;
            topSortText.setText(SizeText);
            
            Container parent = topSortText.getParent();
            parent.remove(topSortText);
            topSortText.setBorder(new EtchedBorder());
            topSortText.setBounds(topBound.x, topBound.y, topBound.width, topBound.height);
            parent.add(topSortText);
            parent.validate();
            parent.repaint(); 
        } 
        
        DefaultListModel listModelAttr = new DefaultListModel();
        for (int x = 0; x < savedFilesTop.length; x++)
        {
            BasicFileAttributes attr;
                try{
                    attr = Files.readAttributes(savedFilesTop[x].toPath(), BasicFileAttributes.class);
                    if (topAttrSetting == AttrSetting.Size)   
                        if ( savedFilesTop[x].isFile() )
                            listModelAttr.addElement(processSize( (int)attr.size() ) );
                        else
                            listModelAttr.addElement("D");
                    else if (topAttrSetting == AttrSetting.DateModified)
                        listModelAttr.addElement( processDateModified(attr.lastModifiedTime()) );
                }
                catch(Exception e){
                    
                }
        }
        topRightAttributeList.setModel(listModelAttr);
        
       
    }//GEN-LAST:event_topSortTextMouseClicked

    private void sortButtonTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sortButtonTopMouseClicked
        
        if( topSortText.getText() == SizeText )
        {
            topSortType = SortType.Size;
          //  topAttrSetting = AttrSetting.DateModified;
            topWindowCurrentDirectory = topCombo.getSelectedItem().toString();
            setDirectoryListTop(topWindowCurrentDirectory, topWindow);
         //   topSortText.setText("Date");
         //   topSortText.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if ( topSortText.getText() == DateText )
        {
            topSortType = SortType.DateModified;
            topWindowCurrentDirectory = topCombo.getSelectedItem().toString();
            setDirectoryListTop(topWindowCurrentDirectory, topWindow);
          //  topSortText.setText("Name");
        //    topSortText.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else
        {
            topSortType = SortType.Name;
         //   topAttrSetting = AttrSetting.Size;
            topWindowCurrentDirectory = topCombo.getSelectedItem().toString();
            setDirectoryListTop(topWindowCurrentDirectory, topWindow);
         ///   topSortText.setText("Size (mb)");
        }
    }//GEN-LAST:event_sortButtonTopMouseClicked

    private void botComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_botComboItemStateChanged
        if (botCombo.getSelectedIndex() != botCombo.getItemCount()-1)
        {
            setDirectoryListBottom( botCombo.getSelectedItem().toString() , bottomWindow);
            bottomWindowCurrentDirectory = botCombo.getSelectedItem().toString();
        }
    }//GEN-LAST:event_botComboItemStateChanged

    private void botComboHierarchyChanged(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_botComboHierarchyChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_botComboHierarchyChanged

    private void botComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botComboMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_botComboMouseClicked

    private void botComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botComboActionPerformed

    private void botComboPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_botComboPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_botComboPropertyChange

    private void sortButtonBotMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sortButtonBotMouseClicked
        if( botSortText.getText() == SizeText )
        {
            botSortType = SortType.Size;
          //  botAttrSetting = AttrSetting.DateModified;
            bottomWindowCurrentDirectory = botCombo.getSelectedItem().toString();
            setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
         //   topSortText.setText("Date");
         //   topSortText.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else if ( botSortText.getText() == DateText )
        {
            botSortType = SortType.DateModified;
            bottomWindowCurrentDirectory = botCombo.getSelectedItem().toString();
            setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
          //  topSortText.setText("Name");
        //    topSortText.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        else
        {
            botSortType = SortType.Name;
         //   topAttrSetting = AttrSetting.Size;
            bottomWindowCurrentDirectory = botCombo.getSelectedItem().toString();
            setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
         ///   topSortText.setText("Size (mb)");
        }
    }//GEN-LAST:event_sortButtonBotMouseClicked

    private void botSortTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botSortTextMouseClicked
        if( botSortText.getText() == SizeText )
        {
         //   topSortType = SortType.DateModified;
            botAttrSetting = AttrSetting.DateModified;
            botSortText.setText(DateText);
            
            botBound = botSortText.getBounds();
            Container parent = botSortText.getParent();
            parent.remove(botSortText);
            botSortText.setBorder(new EtchedBorder());
            botSortText.setBounds(botBound.x + 9, botBound.y, botBound.width-25, botBound.height);
            parent.add(botSortText);
            parent.validate();
            parent.repaint(); 
        }
        else if ( botSortText.getText() == DateText )
        {
         //   topSortType = SortType.Name;
            botSortText.setText(NameText);
            
            Rectangle bound = botSortText.getBounds();
            Container parent = botSortText.getParent();
            parent.remove(botSortText);
            botSortText.setBorder(new EtchedBorder());
            botSortText.setBounds(bound.x-2, bound.y, bound.width+5, bound.height);
            parent.add(botSortText);
            parent.validate();
            parent.repaint(); 
        }
        else
        {
        //    topSortType = SortType.Size;
            botAttrSetting = AttrSetting.Size;
            botSortText.setText(SizeText);
            
            Container parent = botSortText.getParent();
            parent.remove(botSortText);
            botSortText.setBorder(new EtchedBorder());
            botSortText.setBounds(botBound.x, botBound.y, botBound.width, botBound.height);
            parent.add(botSortText);
            parent.validate();
            parent.repaint(); 
        } 
        
        DefaultListModel listModelAttr = new DefaultListModel();
        for (int x = 0; x < savedFilesBot.length; x++)
        {
            BasicFileAttributes attr;
                try{
                    attr = Files.readAttributes(savedFilesBot[x].toPath(), BasicFileAttributes.class);
                    if (botAttrSetting == AttrSetting.Size)   
                        if ( savedFilesBot[x].isFile() )
                            listModelAttr.addElement(processSize( (int)attr.size() ) );
                        else
                            listModelAttr.addElement("D");
                    else if (botAttrSetting == AttrSetting.DateModified)
                        listModelAttr.addElement( processDateModified(attr.lastModifiedTime()) );
                }
                catch(Exception e){
                    
                }
        }
        bottomRightAttributeList.setModel(listModelAttr);
    }//GEN-LAST:event_botSortTextMouseClicked
    private void deleteMouseClickedHelper(){
        int[] list;
        String[] selectedDirectories;
        if (topWindow.getSelectedIndices().length > bottomWindow.getSelectedIndices().length){
            list = topWindow.getSelectedIndices();
            if (topTextField.getText().chars().count() > 0)
                selectedDirectories = compatibleDirectories;
            else
                selectedDirectories = topDirectories;
        }
        else{
            list = bottomWindow.getSelectedIndices();
            if (bottomTextField.getText().chars().count() > 0)
                selectedDirectories = compatibleDirectories;
            else
                selectedDirectories = bottomDirectories;
        }
        String[] selectedFiles = new String[list.length];
        for (int x = 0; x < list.length; x++)
        {   if (selectedDirectories[ list[x] ] != null)
                selectedFiles[x] = selectedDirectories[ list[x] ];
        //    debugField.setText( String.valueOf( list[x] ) );
        }
        
        String popupString;
        int numFiles=0, numDir=0;
        for (int x = 0; x < selectedFiles.length; x++)
        {
            if (new File(selectedFiles[x]).isFile())
                numFiles++;
            else
                numDir++;
        }
        
        JPopupMenu popup = new JPopupMenu();
        String string = ("<html>Are you sure you want to delete <br/> <center> " + String.valueOf(numDir) + " Directories and " + String.valueOf(numFiles) + " Files?<center/></html>" );
        JMenuItem menuItem = new JMenuItem(string);
        
        popup.add(menuItem); 
      //  popup.setBorder(new EmptyBorder(0,0,0,0));
        JFrame f = new JFrame("Delete Files");
        f.setSize(400, 200); 
       // try{Thread.sleep(200);}catch(Exception e){};
        f.setLocation(200, 100);
        //f.setBounds(200, 100, 400, 200);
        f.setUndecorated(true); f.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
        
        f.setLayout(null); f.setResizable(false); 
        
        

        BufferedImage image;
        JPanel backgroundForColor;
        try{
        image = ImageIO.read(new File("Images/back1.jpg"));
        
        backgroundForColor = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
            }; 
        }
        catch(Exception e){
            backgroundForColor = new JPanel();
        }
      
        
        backgroundForColor.setBounds(0, 0, f.getWidth(), f.getHeight()); backgroundForColor.setBackground(Color.white); f.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
        JPanel possibleTextBackground = new JPanel(); possibleTextBackground.setBounds(0, 0, 50, 50); possibleTextBackground.setBackground(Color.white);
        
        JLabel text = new JLabel(string); 
        text.setBounds(f.getWidth()/2-120,f.getHeight()/2 - 70, 275, 50);
        //text.setBorder(new EtchedBorder(0)); 
        text.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) );
        menuItem.setBounds(0, -100, 500, 500);
        
        JButton yButton = new JButton(); yButton.setBounds(70, 90, 100, 50); yButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        yButton.setText("Yes"); 
        yButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            //    debugField.append( e.getActionCommand() );
                for (int x = 0; x < selectedFiles.length; x++)
                {
                    if (new File(selectedFiles[x]).isFile())
                    {
                        try{ 
                        Files.delete(Paths.get(selectedFiles[x]));
                        }
                        catch(Exception ex){
                         //   debugField.setText( selectedFiles[x].substring( selectedFiles[x].lastIndexOf("/") + 1 ) + " popshot ");
                        }
                    }
                    else
                    {
                        try{
                        FileUtils.deleteDirectory( new File( selectedFiles[x] ) );
                        }
                        catch(Exception ex){
                         //   debugField.setText( selectedFiles[x].substring( selectedFiles[x].lastIndexOf("/") + 1 ) + " popshot ");
                        }
                    }
                }
                setDirectoryListTop(topWindowCurrentDirectory, topWindow);
                setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        JButton nButton = new JButton(); nButton.setBounds(220, 90, 100, 50); nButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        nButton.setText("No"); 
        
        nButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        f.add(nButton); f.add(yButton); 
        f.add(text);
        f.add(possibleTextBackground);
        f.add(backgroundForColor);
        
        f.show();  f.hide(); try{Thread.sleep(10);}catch(Exception e){}; f.setLocation(200, 100); f.show();
    }
    
    private void deleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_deleteMouseClicked
        if (topWindow.getSelectedIndices().length > 0 || bottomWindow.getSelectedIndices().length > 0)
            deleteMouseClickedHelper();
    }//GEN-LAST:event_deleteMouseClicked

    private void renameMouseClickedHelper(){
        int[] list;
        String[] selectedDirectories;
        if (topWindow.getSelectedIndices().length > bottomWindow.getSelectedIndices().length){
            list = topWindow.getSelectedIndices();
            if (topTextField.getText().chars().count() > 0)
                selectedDirectories = compatibleDirectories;
            else
                selectedDirectories = topDirectories;
        }
        else{
            list = bottomWindow.getSelectedIndices();
            if (bottomTextField.getText().chars().count() > 0)
                selectedDirectories = compatibleDirectories;
            else
                selectedDirectories = bottomDirectories;
        }
        String[] selectedFiles = new String[list.length];
        for (int x = 0; x < list.length; x++)
        {   if (selectedDirectories[ list[x] ] != null)
                selectedFiles[x] = selectedDirectories[ list[x] ];
        //    debugField.setText( String.valueOf( list[x] ) );
        }
        
        
        JFrame f = new JFrame("Rename File");  
        f.setBounds(200, 25, 400, 200);
         f.setLayout(null); f.setResizable(false); f.setUndecorated(true); f.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
       // f.setLocationRelativeTo(null); 
         f.setAlwaysOnTop(true);
        
        JTextField renText = new JTextField(); renText.setBounds(35, 128, 100, 25); 
        
        String string = ("Pick a new name for file:" );
        JLabel text = new JLabel(string); 
        text.setBounds(f.getWidth()/2-95,f.getHeight()/2 - 70, 275, 50);
        //text.setBorder(new EtchedBorder(0)); 
        text.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) );
        
        JButton yButton = new JButton(); yButton.setBounds(165, 118, 100, 50); yButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        yButton.setText("Rename");   
        yButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { 
                File initfile = new File(selectedFiles[0]);
                File rename;
                String ext = null;
                if (initfile.isFile() && selectedFiles[0].contains("."))
                    ext = selectedFiles[0].substring( selectedFiles[0].lastIndexOf('.') );
                if (ext == null)
                    rename = new File( selectedFiles[0].substring(0, selectedFiles[0].lastIndexOf('/') + 1) + renText.getText() );
                else
                    rename = new File( selectedFiles[0].substring(0, selectedFiles[0].lastIndexOf('/') + 1) + renText.getText() + ext );
                
                    
                
                try{
                Files.move( initfile.toPath(), rename.toPath());
                Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence hide"});
                }
                catch(Exception e2)
                {
                }
               // new File( selectedFiles[0] ).renameTo( new File( selectedFiles[0].substring(0, selectedFiles[0].lastIndexOf('/')) + renText.toString()) );
                
            //    debugField.append("rename attempted to " + selectedFiles[0].substring(0, selectedFiles[0].lastIndexOf('/')+1) + renText.getText() );
                setDirectoryListTop(topWindowCurrentDirectory, topWindow);
                setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        JButton nButton = new JButton(); nButton.setBounds(290, 118, 80, 50); nButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        nButton.setText("Cancel"); 
        
        nButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence hide"});
                }
                catch(Exception e3){
                    
                }
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        String oldFileName = selectedFiles[0].toString().substring( selectedFiles[0].toString().lastIndexOf('/') + 1 ) ;
        JLabel oldFileNameL = new JLabel(oldFileName); 
        oldFileNameL.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) );
        oldFileNameL.setBounds(text.getLocation().x + 78 - (int)(oldFileName.length() *2.5), text.getLocation().y + 10,  200, 100);
        //text.setBorder(new EtchedBorder(0)); 
        
        
        JPanel backgroundForColor = new JPanel();
        backgroundForColor.setBounds(0, 0, f.getWidth(), f.getHeight()); backgroundForColor.setBackground(Color.white);
        
        f.add(nButton); f.add(renText);
        f.add(oldFileNameL);
        f.add(text);
        f.add(yButton);
        f.add(backgroundForColor);
        
        f.show();  f.hide(); try{Thread.sleep(10);}catch(Exception e){}; f.setLocation(200, 25); f.show();
        renText.addFocusListener(myFocusListener); renText.requestFocus();
        
        
    }
    
    java.awt.event.FocusListener myFocusListener = new java.awt.event.FocusListener() {
    public void focusGained(java.awt.event.FocusEvent focusEvent) {
        String line; int linesOfOutput = 0;
        try {
          Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","pidof florence"});
          BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
          while ((line = input.readLine()) != null)
          {
            linesOfOutput = (int)line.chars().count();
          }
        } catch (Exception err) {
          System.out.println(err);
        } 
        if (linesOfOutput == 0)    
        try{ 
            //Runtime.getRuntime().exec("florence -u /home/wilks/Music/Can/config/florence2.conf");
            Process p1 = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence -u /home/wilks/Music/Can/config/florence3.conf"});
          //  p1.waitFor();
            Thread.sleep(300);
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence move 50,240"});
            //  textFocusState = textFocusState.KeyBoardInitialized;
          }
          catch (Exception e){
             // textFocusState = textFocusState.KeyBoardInitialized;
          }
        else
        {
          try{
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence move 50,240"});
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence show"});
            //Runtime.getRuntime().exec("florence move 50,240");
            //Runtime.getRuntime().exec("florence show");
          }
          catch (Exception e){
          }
        }
    }

    public void focusLost(java.awt.event.FocusEvent focusEvent) {
        
    }
};
    
    private void renameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameMouseClicked
        if (topWindow.getSelectedIndices().length == 1 || bottomWindow.getSelectedIndices().length == 1)
            renameMouseClickedHelper();
            
    }//GEN-LAST:event_renameMouseClicked

    private void topTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_topTextFieldFocusGained
    String line; int linesOfOutput = 0;
    try {
      Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","pidof florence"});
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ((line = input.readLine()) != null)
      {
        linesOfOutput = (int)line.chars().count();
      }
    } catch (Exception err) {
      System.out.println(err);
    } 
    if (linesOfOutput == 0)    
    try{
     //   Runtime.getRuntime().exec("florence");
        Process p1 = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence -u /home/wilks/Music/Can/config/florence.conf"});
      //  p1.waitFor();
        Thread.sleep(300);
        Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence move 50,210"});
        //  textFocusState = textFocusState.KeyBoardInitialized;
      }
      catch (Exception e){
         // textFocusState = textFocusState.KeyBoardInitialized;
      }
        else
        {
          try{
              Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence move 50,210"});
              Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence show"});
            //Runtime.getRuntime().exec("florence move 50,210");
            //Runtime.getRuntime().exec("florence show");
          }
          catch (Exception e){
          }
        }
    }//GEN-LAST:event_topTextFieldFocusGained

    private void bottomTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bottomTextFieldFocusGained
        String line; int linesOfOutput = 0;
        try {
          Process p = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","pidof florence"});
          BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
          while ((line = input.readLine()) != null)
          {
            linesOfOutput = (int)line.chars().count();
          }
        } catch (Exception err) {
          System.out.println(err);
        } 
        if (linesOfOutput == 0)    
        try{
         //   Runtime.getRuntime().exec("florence -u /home/wilks/Music/Can/config/florence2.conf");
            Process p1 = Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence -u /home/wilks/Music/Can/config/florence2.conf"});
          //  p1.waitFor();
            Thread.sleep(300);
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence move 50,0"});
            //  textFocusState = textFocusState.KeyBoardInitialized;
          }
          catch (Exception e){
             // textFocusState = textFocusState.KeyBoardInitialized;
          }
        else
        {
          try{
              Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence move 50,0"});
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence show"});
            //Runtime.getRuntime().exec("florence move 50,0");
            //Runtime.getRuntime().exec("florence show");
          }
          catch (Exception e){
          }
        }
    }//GEN-LAST:event_bottomTextFieldFocusGained

    private void BackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackMouseClicked
        try {
            File file = new File("/home/wilks/Music/Can/config/bookmarks.txt");
            FileWriter fileWriter = new FileWriter(file);
            if (usbState == usbState.USBdetected){
                fileWriter.write( lastTopCurrentDirectory + "\n" ); fileWriter.write( bottomWindowCurrentDirectory +"\n" ); fileWriter.write( homeDirectory + "\n" );
                fileWriter.write( topWindowCurrentDirectory);
            }
            else{
                fileWriter.write( topWindowCurrentDirectory + "\n" ); fileWriter.write( bottomWindowCurrentDirectory +"\n" ); fileWriter.write( homeDirectory + "\n" );
                fileWriter.write( lastUSBDirectory );
            }
            fileWriter.flush();
            fileWriter.close();
            Runtime.getRuntime().exec(new String[]{"/bin/sh","-c", "killall -e florence"});
        } catch (IOException e) {
                e.printStackTrace();
        }
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_BackMouseClicked

    private void multiSelectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_multiSelectMouseClicked
        if ( selectionMode == selectionMode.singleSelect ){
            
            
            selectionMode = selectionMode.MultiSelect;
            bot.keyPress(17);
            multiSelect.setBackground(Color.WHITE);
            multiSelect.setForeground(Color.BLUE);
        }
        else{
            selectionMode = selectionMode.singleSelect;
            bot.keyRelease(17);
          //  topWindow.setSelectionModel(new DefaultListSelectionModel());
          //  bottomWindow.setSelectionModel(new DefaultListSelectionModel());
            multiSelect.setBackground(null);
            multiSelect.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_multiSelectMouseClicked

    private void matchDirTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_matchDirTopMouseClicked
        setDirectoryListTop( bottomWindowCurrentDirectory , topWindow);
        topWindowCurrentDirectory = bottomWindowCurrentDirectory;
        
    }//GEN-LAST:event_matchDirTopMouseClicked

    private void matchDirBottomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_matchDirBottomMouseClicked
        setDirectoryListBottom( topWindowCurrentDirectory, bottomWindow);
        bottomWindowCurrentDirectory = topWindowCurrentDirectory;
    }//GEN-LAST:event_matchDirBottomMouseClicked

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteActionPerformed

    private void newFolderTopMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newFolderTopMouseClicked
        JFrame f = new JFrame("Create new folder");
        
        f.setSize(400,200); f.setLayout(null); f.setResizable(false); f.setUndecorated(true); f.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
       // f.setLocationRelativeTo(null); 
        f.setLocation( 200 , 25); f.setAlwaysOnTop(true);
        
        JTextField renText = new JTextField(); renText.setBounds(40, 128, 100, 25); 
        
        String string = ("Pick a name for folder in:" );
        JLabel text = new JLabel(string); 
        text.setBounds(f.getWidth()/2-95,f.getHeight()/2 - 70, 275, 50);
        //text.setBorder(new EtchedBorder(0)); 
        text.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) );
        
        JButton yButton = new JButton(); yButton.setBounds(160, 118, 100, 50); yButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        yButton.setText("Confirm"); 
        yButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { 
                System.out.println(topWindowCurrentDirectory + "/" + renText.getText());
                new File( topWindowCurrentDirectory + "/" + renText.getText() ).mkdir();
                setDirectoryListTop(topWindowCurrentDirectory, topWindow);
                setDirectoryListBottom( bottomWindowCurrentDirectory , bottomWindow);
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                try{
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence hide"});
                }
                catch(Exception e3){
                    
                }
            }
        });
        
        JButton nButton = new JButton(); nButton.setBounds(290, 118, 80, 50); nButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        nButton.setText("Cancel"); 
        
        nButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence hide"});
                }
                catch(Exception e3){
                    
                }
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        String oldFileName = topWindowCurrentDirectory + "/" ;
        JLabel oldFileNameL = new JLabel(oldFileName); 
        oldFileNameL.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) );
        oldFileNameL.setBounds(text.getLocation().x + 78 - (int)(oldFileName.length() *2.5), text.getLocation().y + 10,  200, 100);
        //text.setBorder(new EtchedBorder(0)); 
        
        
        JPanel backgroundForColor = new JPanel();
        backgroundForColor.setBounds(0, 0, f.getWidth(), f.getHeight()); backgroundForColor.setBackground(Color.white);
        
        f.add(nButton);
        f.add(renText);
        f.add(oldFileNameL);
        f.add(text);
        f.add(yButton);
        f.add(backgroundForColor);
        
        f.show();  f.hide(); try{Thread.sleep(10);}catch(Exception e){}; f.setLocation( 200 , 25); f.show();
        renText.addFocusListener(myFocusListener); renText.requestFocus();
    }//GEN-LAST:event_newFolderTopMouseClicked

    private void newFolderBottomMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_newFolderBottomMouseClicked
        JFrame f = new JFrame("Create new folder");
        
        f.setSize(400,200); f.setLayout(null); f.setResizable(false); f.setUndecorated(true); f.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
       // f.setLocationRelativeTo(null); 
        f.setLocation( 200 , 25); f.setAlwaysOnTop(true);
        
        JTextField renText = new JTextField(); renText.setBounds(40, 128, 100, 25); 
        
        String string = ("Pick a name for folder in:" );
        JLabel text = new JLabel(string); 
        text.setBounds(f.getWidth()/2-95,f.getHeight()/2 - 70, 275, 50);
        //text.setBorder(new EtchedBorder(0)); 
        text.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) );
        
        JButton yButton = new JButton(); yButton.setBounds(160, 118, 100, 50); yButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        yButton.setText("Confirm"); 
        yButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) { 
                System.out.println(bottomWindowCurrentDirectory + "/" + renText.getText());
                new File( bottomWindowCurrentDirectory + "/" + renText.getText() ).mkdir();
                setDirectoryListBottom(bottomWindowCurrentDirectory, bottomWindow);
                setDirectoryListTop( topWindowCurrentDirectory , topWindow);
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                try{
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence hide"});
                }
                catch(Exception e3){
                    
                }
            }
        });
        
        JButton nButton = new JButton(); nButton.setBounds(290, 118, 80, 50); nButton.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) ); 
        nButton.setText("Cancel"); 
        
        nButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Runtime.getRuntime().exec(new String[]{"/bin/sh","-c","florence hide"});
                }
                catch(Exception e3){
                    
                }
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        String oldFileName = bottomWindowCurrentDirectory + "/" ;
        JLabel oldFileNameL = new JLabel(oldFileName); 
        oldFileNameL.setFont(new java.awt.Font("Sans-serif", java.awt.Font.BOLD, 12 ) );
        oldFileNameL.setBounds(text.getLocation().x + 78 - (int)(oldFileName.length() *2.5), text.getLocation().y + 10,  200, 100);
        //text.setBorder(new EtchedBorder(0)); 
        
        
        JPanel backgroundForColor = new JPanel();
        backgroundForColor.setBounds(0, 0, f.getWidth(), f.getHeight()); backgroundForColor.setBackground(Color.white);
        
        f.add(nButton);
        f.add(renText);
        f.add(oldFileNameL);
        f.add(text);
        f.add(yButton);
        f.add(backgroundForColor);
        
        f.show();  f.hide(); try{Thread.sleep(10);}catch(Exception e){}; f.setLocation( 200 , 25); f.show(); 
        renText.addFocusListener(myFocusListener); renText.requestFocus();
    }//GEN-LAST:event_newFolderBottomMouseClicked

    private void setHomeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_setHomeMouseClicked
        homeDirectory = bottomWindowCurrentDirectory;
        if (usbState == usbState.noneDetected)
            topHomeDirectory = bottomWindowCurrentDirectory;
    }//GEN-LAST:event_setHomeMouseClicked
    
    private void setNewAttrLabelTop() {
        DefaultListModel listModelAttr = new DefaultListModel();
    }
    
    private void setDirectoryListBottom(String directory, JList jlist)
    {
      //  fileType = new HashMap<String, Integer>();
        File folder = new File(directory);
        
            File[] files = folder.listFiles();
            files = sortFiles(files, botSortType);
            savedFilesBot = files;
            
            bottomDirectories = new String[files.length];
            botAttr = new BasicFileAttributes[files.length];

            DefaultListModel listModel = new DefaultListModel();
            DefaultListModel listModelAttr = new DefaultListModel();
            for (int x = 0; x < files.length; x++)
            {
               BasicFileAttributes attr;
               try{
                  attr = Files.readAttributes(files[x].toPath(), BasicFileAttributes.class);
                    if (botAttrSetting == AttrSetting.Size)   
                        if ( files[x].isFile() )
                            listModelAttr.addElement(processSize( (int)attr.size() ) );
                        else
                            listModelAttr.addElement("D");
                    else if (botAttrSetting == AttrSetting.DateModified)
                        listModelAttr.addElement( processDateModified(attr.lastModifiedTime()) );
              //      debugField.append("  "+  attr.size() + "  ");
                    botAttr[x] = attr;
                }
                catch(Exception e){
                    
                }
                
               Integer isDirectory = 0;
               if (files[x].isDirectory())
                   isDirectory = 1;
               fileType.put(files[x].toString().substring( files[x].toString().lastIndexOf('/') + 1 ), isDirectory);
               listModel.addElement(files[x].toString().substring( files[x].toString().lastIndexOf('/') + 1 ));
               bottomDirectories[x] = files[x].getAbsolutePath();
            }
            setComboBox(directory, botCombo);
            bottomRightAttributeList.setModel(listModelAttr);
            bottomRightAttributeList.setCellRenderer(new AttRenderer());
            bottomRightAttributeList.setFixedCellHeight(20);
            jlist.setModel(listModel);
            jlist.setCellRenderer(new MarioListRenderer());
    }
    
    private String processSize(int size)
    {
        if (size % 10 > 4)
        {
            size = size + 10;
        }
        
        String numAsString = String.valueOf(size);
        if (numAsString.length() > 3)
        {
            numAsString = numAsString.substring(0, numAsString.length() -1);
            numAsString = numAsString.substring(0, numAsString.length() -2 ) + "." + numAsString.substring(numAsString.length() -2, numAsString.length() );
        }
        return numAsString;
    }
    
    private String processDateModified(FileTime time)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd.hh:mm a");
        long milliseconds = time.toMillis();
        
        return simpleDateFormat.format( milliseconds );
    }
    
    private File[] sortFiles(File[] files, SortType sortType)
    {
        if (sortType == SortType.Name)
        {
            String[] fileStrings = new String[files.length];
            for (int x = 0; x < files.length; x++)
                fileStrings[x] = files[x].toString();
            Arrays.sort(fileStrings, Collator.getInstance());
            files = new File[fileStrings.length];
            for (int x = 0; x < fileStrings.length; x++)
                files[x] = new File( fileStrings[x] );
            return files;
        }
        else if (sortType == SortType.Size)
        {
            Arrays.sort( files, SizeFileComparator.SIZE_REVERSE );
        }
        else if (sortType == SortType.DateModified)
        {
            Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
        }
        return files;
    }
    
    private void setDirectoryListTop(String directory, JList jlist)
    {
       // fileType = new HashMap<String, Integer>();
        File folder = new File(directory);
        
            File[] files = folder.listFiles();
            files = sortFiles(files, topSortType);
            savedFilesTop = files;
            
            topDirectories = new String[files.length];
            topAttr = new BasicFileAttributes[files.length];

            DefaultListModel listModel = new DefaultListModel();
            DefaultListModel listModelAttr = new DefaultListModel();
            for (int x = 0; x < files.length; x++)
            {
                BasicFileAttributes attr;
                try{
                    attr = Files.readAttributes(files[x].toPath(), BasicFileAttributes.class);
                    if (topAttrSetting == AttrSetting.Size)   
                        if ( files[x].isFile() )
                            listModelAttr.addElement(processSize( (int)attr.size() ) );
                        else
                            listModelAttr.addElement("D");
                    else if (topAttrSetting == AttrSetting.DateModified)
                        listModelAttr.addElement( processDateModified(attr.lastModifiedTime()) );
                //    debugField.append("  "+  attr.size() + "  ");
                    topAttr[x] = attr;
                }
                catch(Exception e){
                    
                }
               Integer isDirectory = 0;
               if (files[x].isDirectory())
                   isDirectory = 1;
               fileType.put(files[x].toString().substring( files[x].toString().lastIndexOf('/') + 1 ), isDirectory);
               listModel.addElement(files[x].toString().substring( files[x].toString().lastIndexOf('/') + 1 ));
               topDirectories[x] = files[x].getAbsolutePath();
            }
            setComboBox(directory, topCombo);
            topRightAttributeList.setModel(listModelAttr);
            topRightAttributeList.setCellRenderer(new AttRenderer());
            topRightAttributeList.setFixedCellHeight(20);
            jlist.setModel(listModel);
            jlist.setCellRenderer(new MarioListRenderer());
    }
    
    private void setComboBox(String directory, JComboBox combo)
    {
        combo.removeAllItems();
        ArrayList strings = new ArrayList();
        
        String[] fileStrings = directory.split("/");
        for (int x=0; x < fileStrings.length; x++)
            if (x != fileStrings.length -1 && x!= 0)
                if (x != 1)
                {
                    String stringToAdd="";
                    for (int y = 0; y < x; y++)
                    {
                        stringToAdd += (fileStrings[y] + "/");
                    }
                    stringToAdd += fileStrings[x] + "/";
                    combo.addItem(stringToAdd);
                }
                else
                    combo.addItem("/" + fileStrings[x] + "/");
            else if (x != 0)
            {
                String stringToAdd="";
                for (int y = 0; y < x; y++)
                {
                    stringToAdd += (fileStrings[y] + "/");
                }
                stringToAdd += fileStrings[x] + "/";
                combo.addItem(stringToAdd);
            }
        combo.setSelectedIndex( combo.getItemCount()-1 );
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new gui().setVisible(true);
            }
        });
    }
    private Map<String, ImageIcon> imageMap;
    
    class AttRenderer extends DefaultListCellRenderer {
        java.awt.Font font = new java.awt.Font("helvitica", java.awt.Font.PLAIN, 11);
        
        @Override
        public java.awt.Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
        //    label.setIcon(imageMap.get((String) value));
            label.setFont(font);
            label.setHorizontalAlignment( JLabel.RIGHT );
            label.setHorizontalTextPosition(JLabel.RIGHT);
            return label;
        }
    }
    
    class MarioListRenderer extends DefaultListCellRenderer {
        java.awt.Font font = new java.awt.Font("helvitica", java.awt.Font.PLAIN, 12);

        @Override
        public java.awt.Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            String[] nameList = {"bowser", "bowser", "bowser", "bowser", "bowser"};
            
            
            Icon image2;
            String stringValue = value.toString();
            
            if (fileType.get(value) != null && fileType.get(value) == 1)
    //        if (new File(value.toString()).isDirectory())
         //   if (!value.toString().contains("."))
                image2= UIManager.getIcon("FileView.directoryIcon");
            else
                image2= UIManager.getIcon("FileView.fileIcon");
        //    label.setIcon(imageMap.get((String) value));
            label.setIcon(image2);
            label.setHorizontalTextPosition(JLabel.RIGHT);
            label.setFont(font);
            
            
            
            return label;
        }
    }
    
    
       
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane AttributeScrollPane;
    private javax.swing.JScrollPane AttributeScrollPaneBot;
    private javax.swing.JButton Back;
    private javax.swing.JComboBox<String> botCombo;
    private javax.swing.JLabel botSortText;
    private javax.swing.JList<String> bottomRightAttributeList;
    private javax.swing.JScrollPane bottomScrollPane;
    private javax.swing.JTextField bottomTextField;
    private javax.swing.JList<String> bottomWindow;
    private javax.swing.JButton delete;
    private javax.swing.JButton homeIconBottom;
    private javax.swing.JButton homeIconTop;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JButton matchDirBottom;
    private javax.swing.JButton matchDirTop;
    private javax.swing.JButton multiSelect;
    private javax.swing.JButton newFolderBottom;
    private javax.swing.JButton newFolderTop;
    private javax.swing.JButton rename;
    private javax.swing.JButton setHome;
    private javax.swing.JButton sortButtonBot;
    private javax.swing.JButton sortButtonTop;
    private javax.swing.JComboBox<String> topCombo;
    private javax.swing.JList<String> topRightAttributeList;
    private javax.swing.JScrollPane topScrollPane;
    private javax.swing.JLabel topSortText;
    private javax.swing.JTextField topTextField;
    private javax.swing.JList<String> topWindow;
    private javax.swing.JButton transfer;
    private javax.swing.JButton upOneLevelBottom;
    private javax.swing.JButton upOneLevelTop;
    // End of variables declaration//GEN-END:variables
}
