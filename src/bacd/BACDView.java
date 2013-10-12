/*
 * BACDView.java
 */
package bacd;

import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

/**
 * The application's main frame.
 */
public class BACDView extends FrameView implements PropertyChangeListener {

    String Character_Data_File;
    String Tree_File;
    String File_Listing_Tests;
    String Output_File;
    String Keep_Random_Data;
    String Seed;
    long seed = 10000;
    String Trim_Taxa;
    String[] taxa2Trim = null;
    int nTaxTrim = 0;
    boolean append = false;
    boolean exactNull = true;
    boolean keepRandomData = false;
    String Append_Summaries;
    String Use_Exact_Null;
    String Number_Trees;
    String Number_Taxa;
    int nTrees = 0;
    int nTax = 0;
    private ProgressMonitor progressMonitorR = null;
    private RunTask rtask = null;

    public BACDView(SingleFrameApplication app) {
        super(app);

        initComponents();

        Calendar now = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd hh:mm:ss a zzz");
        progressText.setText("Welcome to BACD ('baked'): Bayesian Analysis of Comparative Data.\n" + formatter.format(now.getTime()) + "\n");
        RandomSeedText.setText("10000");
        OutputFileText.setText("Result");

        File f = new File("Properties.txt");
        if (f.exists()) {
            loadProperties(f);
        }


        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = BACDApp.getApplication().getMainFrame();
            aboutBox = new BACDAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        BACDApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        DataFileText = new javax.swing.JTextField();
        TestListFileText = new javax.swing.JTextField();
        TreeFileText = new javax.swing.JTextField();
        OutputFileText = new javax.swing.JTextField();
        RandomSeedText = new javax.swing.JTextField();
        TreeNumberText = new javax.swing.JTextField();
        NumberTaxaText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        keepRandomDataCheckBox = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        appendSummariesCheckBox = new javax.swing.JCheckBox();
        runButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        TaxaToExcludeText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        progressText = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        exactNullCheckBox = new javax.swing.JCheckBox();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        openPropertiesFileMenuItem = new javax.swing.JMenuItem();
        savePropertiesMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        openDataFileMenuItem = new javax.swing.JMenuItem();
        openTestListFileMenuItem = new javax.swing.JMenuItem();
        openTreeFileMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        saveOutputMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(bacd.BACDApp.class).getContext().getResourceMap(BACDView.class);
        DataFileText.setText(resourceMap.getString("DataFileText.text")); // NOI18N
        DataFileText.setName("DataFileText"); // NOI18N

        TestListFileText.setText(resourceMap.getString("TestListFileText.text")); // NOI18N
        TestListFileText.setName("TestListFileText"); // NOI18N

        TreeFileText.setText(resourceMap.getString("TreeFileText.text")); // NOI18N
        TreeFileText.setName("TreeFileText"); // NOI18N

        OutputFileText.setText(resourceMap.getString("OutputFileText.text")); // NOI18N
        OutputFileText.setName("OutputFileText"); // NOI18N

        RandomSeedText.setText(resourceMap.getString("RandomSeedText.text")); // NOI18N
        RandomSeedText.setName("RandomSeedText"); // NOI18N

        TreeNumberText.setText(resourceMap.getString("TreeNumberText.text")); // NOI18N
        TreeNumberText.setName("TreeNumberText"); // NOI18N

        NumberTaxaText.setText(resourceMap.getString("NumberTaxaText.text")); // NOI18N
        NumberTaxaText.setName("NumberTaxaText"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        keepRandomDataCheckBox.setText(resourceMap.getString("keepRandomDataCheckBox.text")); // NOI18N
        keepRandomDataCheckBox.setName("keepRandomDataCheckBox"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        appendSummariesCheckBox.setText(resourceMap.getString("appendSummariesCheckBox.text")); // NOI18N
        appendSummariesCheckBox.setName("appendSummariesCheckBox"); // NOI18N

        runButton.setText(resourceMap.getString("runButton.text")); // NOI18N
        runButton.setName("runButton"); // NOI18N
        runButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runButtonActionPerformed(evt);
            }
        });

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        TaxaToExcludeText.setName("TaxaToExcludeText"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        progressText.setColumns(20);
        progressText.setRows(5);
        progressText.setName("progressText"); // NOI18N
        jScrollPane1.setViewportView(progressText);

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        exactNullCheckBox.setText(resourceMap.getString("exactNullCheckBox.text")); // NOI18N
        exactNullCheckBox.setName("exactNullCheckBox"); // NOI18N
        exactNullCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exactNullCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6)
                                .addComponent(jLabel4)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1)
                                .addComponent(jLabel11))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(OutputFileText, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TreeFileText, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TestListFileText, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(DataFileText, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                            .addGap(128, 128, 128)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(RandomSeedText, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TreeNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(NumberTaxaText, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TaxaToExcludeText, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5)
                                .addComponent(jLabel12)
                                .addComponent(jLabel10))
                            .addGap(48, 48, 48)
                            .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(appendSummariesCheckBox)
                                .addGroup(mainPanelLayout.createSequentialGroup()
                                    .addComponent(exactNullCheckBox)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 519, Short.MAX_VALUE)
                                    .addComponent(runButton))
                                .addComponent(keepRandomDataCheckBox)))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                            .addGap(20, 20, 20)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9)))
                .addContainerGap())
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {DataFileText, NumberTaxaText, OutputFileText, RandomSeedText, TaxaToExcludeText, TestListFileText, TreeFileText, TreeNumberText});

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(DataFileText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(TestListFileText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(TreeFileText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(OutputFileText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(RandomSeedText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(TreeNumberText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(NumberTaxaText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(TaxaToExcludeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(appendSummariesCheckBox))
                        .addGap(2, 2, 2)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(keepRandomDataCheckBox))
                        .addGap(2, 2, 2)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(exactNullCheckBox))
                        .addGap(13, 13, 13))
                    .addComponent(runButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        openPropertiesFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        openPropertiesFileMenuItem.setText(resourceMap.getString("openPropertiesFileMenuItem.text")); // NOI18N
        openPropertiesFileMenuItem.setName("openPropertiesFileMenuItem"); // NOI18N
        openPropertiesFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openPropertiesFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openPropertiesFileMenuItem);

        savePropertiesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        savePropertiesMenuItem.setText(resourceMap.getString("savePropertiesMenuItem.text")); // NOI18N
        savePropertiesMenuItem.setName("savePropertiesMenuItem"); // NOI18N
        savePropertiesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savePropertiesMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(savePropertiesMenuItem);

        jSeparator2.setName("jSeparator2"); // NOI18N
        fileMenu.add(jSeparator2);

        openDataFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        openDataFileMenuItem.setText(resourceMap.getString("openDataFileMenuItem.text")); // NOI18N
        openDataFileMenuItem.setName("openDataFileMenuItem"); // NOI18N
        openDataFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDataFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openDataFileMenuItem);

        openTestListFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        openTestListFileMenuItem.setText(resourceMap.getString("openTestListFileMenuItem.text")); // NOI18N
        openTestListFileMenuItem.setName("openTestListFileMenuItem"); // NOI18N
        openTestListFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openTestListFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openTestListFileMenuItem);

        openTreeFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        openTreeFileMenuItem.setText(resourceMap.getString("openTreeFileMenuItem.text")); // NOI18N
        openTreeFileMenuItem.setName("openTreeFileMenuItem"); // NOI18N
        openTreeFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openTreeFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openTreeFileMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        saveOutputMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        saveOutputMenuItem.setText(resourceMap.getString("saveOutputMenuItem.text")); // NOI18N
        saveOutputMenuItem.setName("saveOutputMenuItem"); // NOI18N
        saveOutputMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveOutputMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveOutputMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        fileMenu.add(jSeparator3);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(bacd.BACDApp.class).getContext().getActionMap(BACDView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N
        statusPanel.add(statusMessageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N
        statusPanel.add(statusAnimationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        progressBar.setName("progressBar"); // NOI18N
        statusPanel.add(progressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 0, -1, -1));

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

private void openDataFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDataFileMenuItemActionPerformed
    JFileChooser fc = new JFileChooser();
    fc.setMultiSelectionEnabled(false);

    //Add a custom file filter and disable the default
    //(Accept All) file filter.



    //Show it.
    int returnVal = fc.showDialog(null, "Open");

    //Process the results.
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        //set the text
        File DataFile = fc.getSelectedFile();
        String DataFileName = DataFile.getAbsolutePath();
        this.DataFileText.setText(DataFileName);
        //open the file and read the data
    } else {
    }
    //Reset the file chooser for the next time it's shown.
    //fc.setSelectedFile(null);
    fc.setVisible(true);


}//GEN-LAST:event_openDataFileMenuItemActionPerformed

private void openTestListFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openTestListFileMenuItemActionPerformed
    JFileChooser fc = new JFileChooser();
    fc.setMultiSelectionEnabled(false);

    //Add a custom file filter and disable the default
    //(Accept All) file filter.



    //Show it.
    int returnVal = fc.showDialog(null, "Open");

    //Process the results.
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        //set the text
        File DataFile = fc.getSelectedFile();
        String DataFileName = DataFile.getAbsolutePath();
        this.TestListFileText.setText(DataFileName);
        //open the file and read the data
    } else {
    }
    //Reset the file chooser for the next time it's shown.
    //fc.setSelectedFile(null);
    fc.setVisible(true);
// TODO add your handling code here:
}//GEN-LAST:event_openTestListFileMenuItemActionPerformed

private void openTreeFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openTreeFileMenuItemActionPerformed
    JFileChooser fc = new JFileChooser();
    fc.setMultiSelectionEnabled(false);

    //Add a custom file filter and disable the default
    //(Accept All) file filter.



    //Show it.
    int returnVal = fc.showDialog(null, "Open");

    //Process the results.
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        //set the text
        File DataFile = fc.getSelectedFile();
        String DataFileName = DataFile.getAbsolutePath();
        this.TreeFileText.setText(DataFileName);
        //open the file and read the data
    } else {
    }
    //Reset the file chooser for the next time it's shown.
    //fc.setSelectedFile(null);
    fc.setVisible(true);
// TODO add your handling code here:
}//GEN-LAST:event_openTreeFileMenuItemActionPerformed

    private void loadProperties(File propertyFile) {
        loadProperties(propertyFile.getAbsolutePath());
    }

    private void loadProperties(String propertyFile) {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(propertyFile));
        } catch (Exception noFile) {
            progressText.append("Could not read your properties file " + propertyFile + "\n");
            noFile.printStackTrace();
        }
        Character_Data_File = props.getProperty("Character_Data_File");
        Tree_File = props.getProperty("Tree_File");
        File_Listing_Tests = props.getProperty("File_Listing_Tests");
        Output_File = props.getProperty("Output_File");
        Keep_Random_Data = props.getProperty("Keep_Random_Data");
        Seed = props.getProperty("Seed");
        Trim_Taxa = props.getProperty("Trim_Taxa");
        Use_Exact_Null = props.getProperty("Use_Exact_Null");
        if (Use_Exact_Null != null) {
            if (Use_Exact_Null.toLowerCase().equals("no")) {
                exactNull = false;
            }
        }

        taxa2Trim = null;
        if (Trim_Taxa != null) {
            taxa2Trim = Trim_Taxa.split("\\s*,\\s*");
        }
        nTaxTrim = 0;
        if (taxa2Trim == null) {
        } else {
            nTaxTrim = taxa2Trim.length;
        }
        append = false;
        Append_Summaries = props.getProperty("Append_Summaries");
        if (Append_Summaries != null) {
            if (Append_Summaries.toLowerCase().equals("yes")) {
                append = true;
            }
        }
        Number_Trees = props.getProperty("Number_Trees");
        Number_Taxa = props.getProperty("Number_Taxa");

        if (Character_Data_File == null || Tree_File == null || File_Listing_Tests == null || Output_File == null || Number_Trees == null || Number_Taxa == null) {
            progressText.append("Please provide a properties file with the appropriate information. \nCheck the README file for more information.\n");
        }

        nTrees = 0;
        nTax = 0;

        try {
            nTrees = Integer.parseInt(Number_Trees);
            nTax = Integer.parseInt(Number_Taxa);
        } catch (Exception ne) {
            progressText.append("Could not format number strings for number of trees or number of taxa. \nMake sure the input data are positive integers.\n");
            ne.printStackTrace();
        }
        DataFileText.setText(Character_Data_File);
        TestListFileText.setText(File_Listing_Tests);
        TreeFileText.setText(Tree_File);
        OutputFileText.setText(Output_File);
        RandomSeedText.setText(Seed);
        TreeNumberText.setText(Number_Trees);
        NumberTaxaText.setText(Number_Taxa);
        TaxaToExcludeText.setText(Trim_Taxa);
        if (append) {
            appendSummariesCheckBox.setSelected(true);
        } else {
            appendSummariesCheckBox.setSelected(false);
        }
        if (exactNull) {
            exactNullCheckBox.setSelected(true);
        } else {
            exactNullCheckBox.setSelected(false);
        }
        if (Keep_Random_Data.toLowerCase().equals("no")) {
            keepRandomDataCheckBox.setSelected(false);
            keepRandomData = false;
        } else {
            keepRandomDataCheckBox.setSelected(true);
            keepRandomData = true;
        }

    }

private void openPropertiesFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openPropertiesFileMenuItemActionPerformed
// TODO add your handling code here:

    JFileChooser fc = new JFileChooser();
    fc.setMultiSelectionEnabled(false);

    //Add a custom file filter and disable the default
    //(Accept All) file filter.



    //Show it.
    int returnVal = fc.showDialog(null, "Open");

    //Process the results.
    if (returnVal == JFileChooser.APPROVE_OPTION) {
        //set the text
        File dataFile = fc.getSelectedFile();
        loadProperties(dataFile);
        /*
        progressText.append("Opening properties file " + dataFile.getAbsolutePath() + "\n");
        Properties props = new Properties();
        try {
        props.load(new FileInputStream(dataFile));
        } catch (Exception noFile) {
        progressText.append("Could not read your properties file " + dataFile.getAbsolutePath() + "\n");
        noFile.printStackTrace();
        }
        Character_Data_File = props.getProperty("Character_Data_File");
        Tree_File = props.getProperty("Tree_File");
        File_Listing_Tests = props.getProperty("File_Listing_Tests");
        Output_File = props.getProperty("Output_File");
        Keep_Random_Data = props.getProperty("Keep_Random_Data");
        Seed = props.getProperty("Seed");
        Trim_Taxa = props.getProperty("Trim_Taxa");
        taxa2Trim = null;
        if (Trim_Taxa != null) {
        taxa2Trim = Trim_Taxa.split("\\s*,\\s*");
        }
        nTaxTrim = 0;
        if (taxa2Trim == null) {
        } else {
        nTaxTrim = taxa2Trim.length;
        }
        append = false;
        Append_Summaries = props.getProperty("Append_Summaries");
        if (Append_Summaries != null) {
        if (Append_Summaries.toLowerCase().equals("yes")) {
        append = true;
        }
        }
        Number_Trees = props.getProperty("Number_Trees");
        Number_Taxa = props.getProperty("Number_Taxa");

        if (Character_Data_File == null || Tree_File == null || File_Listing_Tests == null || Output_File == null || Number_Trees == null || Number_Taxa == null) {
        progressText.append("Please provide a properties file with the appropriate information. \nCheck the README file for more information.\n");
        }

        nTrees = 0;
        nTax = 0;

        try {
        nTrees = Integer.parseInt(Number_Trees);
        nTax = Integer.parseInt(Number_Taxa);
        } catch (Exception ne) {
        progressText.append("Could not format number strings for number of trees or number of taxa. \nMake sure the input data are positive integers.\n");
        ne.printStackTrace();
        }
        DataFileText.setText(Character_Data_File);
        TestListFileText.setText(File_Listing_Tests);
        TreeFileText.setText(Tree_File);
        OutputFileText.setText(Output_File);
        RandomSeedText.setText(Seed);
        TreeNumberText.setText(Number_Trees);
        NumberTaxaText.setText(Number_Taxa);
        TaxaToExcludeText.setText(Trim_Taxa);
        if (append) {
        appendSummariesCheckBox.setSelected(true);
        } else {
        appendSummariesCheckBox.setSelected(false);
        }
        if (Keep_Random_Data.toLowerCase().equals("no")) {
        keepRandomDataCheckBox.setSelected(false);
        keepRandomData = false;
        } else {
        keepRandomDataCheckBox.setSelected(true);
        keepRandomData = true;
        }
         */
    }

    //Reset the file chooser for the next time it's shown.
    //fc.setSelectedFile(null);
    fc.setVisible(true);
}//GEN-LAST:event_openPropertiesFileMenuItemActionPerformed

private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
//Read the input parameters
//
    runButton.setText("Running...");
    runButton.setEnabled(false);
    progressMonitorR = new ProgressMonitor(this.mainPanel, "Running analyses", "", 0, 100);
    progressMonitorR.setProgress(0);
    rtask = new RunTask();
    rtask.addPropertyChangeListener(this);
    rtask.execute();
}//GEN-LAST:event_runButtonActionPerformed

    class RunTask extends SwingWorker<Void, Void> {

        int nO = 0;
        int progress = 0;

        @Override
        public synchronized Void doInBackground() {



            setProgress(progress);
            progress = 1;
            setProgress(progress);

            progressText.append("\nReading user input:\n\n");

            Character_Data_File = DataFileText.getText();
            File_Listing_Tests = TestListFileText.getText();
            Tree_File = TreeFileText.getText();
            Output_File = OutputFileText.getText();
            Seed = RandomSeedText.getText();
            Trim_Taxa = TaxaToExcludeText.getText();
            Number_Trees = TreeNumberText.getText();
            Number_Taxa = NumberTaxaText.getText();
            append = appendSummariesCheckBox.isSelected();
            exactNull = exactNullCheckBox.isSelected();
            keepRandomData = keepRandomDataCheckBox.isSelected();



            try {
                nTrees = Integer.parseInt(Number_Trees);
                nTax = Integer.parseInt(Number_Taxa);
                if (Seed != null) {

                    if (Seed.length() > 1) {
                        seed = Long.parseLong(Seed);
                    }
                } else {
                    seed = 10000;
                    RandomSeedText.setText("10000");
                }
            } catch (Exception ne) {
                progressText.append("Could not format number strings for number of trees, number of taxa, or random seed.\n");
                ne.printStackTrace();
                runButton.setText("Run");
                runButton.setEnabled(true);
                return null;
            }


            try {
                setProgress(2);

                progressText.append("Beginning analyses with the following input:\nData file: " + Character_Data_File + "\nFile listing tests: " + File_Listing_Tests + "\nTree file: " + Tree_File + "\nOutput file label: " + Output_File + "\nRandom number generator seed: " + seed + "\nTaxa to exclude from analysis: " + Trim_Taxa + "\nAppend summaries: " + append + "\nKeep random data: " + keepRandomData + "\nUse exact null: " + exactNull + "\n");
                progressText.setCaretPosition(progressText.getText().length());
//Run the analyses

                Vector files = new Vector();
                Random rng = new Random(seed);
                CharacterEvolution ce = new CharacterEvolution(rng);
                File rDir = new File("Results");
                if (!rDir.mkdirs() && !rDir.exists()) {
                    progressText.append("Could not make directory 'Results'\n");
                    runButton.setText("Run");
                    runButton.setEnabled(true);
                    return null;
                }

//read in characters
                progressText.append("Reading character data.\n");
                progressText.setCaretPosition(progressText.getText().length());
                Hashtable charHash = CharacterEvolution.characterHashFromTabText(Character_Data_File, nTax);

//get taxon names
                String[] taxonNames = (String[]) charHash.get("species");
                if (taxonNames == null) {
                    progressText.append("Taxon names were not found correctly in your data file " + Character_Data_File + ". Please make sure one of the columns in your data file is named 'species' (i.e., has the word 'species' on the first line) and lists the taxa.\nIf this doesn't work, make sure your input number of taxa is correct.\n");
                    runButton.setText("Run");
                    runButton.setEnabled(true);
                    return null;
                }

                if (taxonNames.length != nTax) {
                    progressText.append("The incorrect number of taxa were found in your data file " + Character_Data_File + ". Please make sure that the taxa you want to trim do not appear in your data file. The number of taxa put in the properties file should be the number of taxa minus the number of taxa to be trimmed (if any).\n");
                    progressText.append("Number of taxon names: " + taxonNames.length + "\n");
                    progressText.append("Total number of taxa (including taxa to trim) minus number of taxa to trim: " + nTax + "\n");
                    progressText.append("Number of taxa to trim: " + nTaxTrim + "\n");
                    progressText.setCaretPosition(progressText.getText().length());
                    runButton.setText("Run");
                    runButton.setEnabled(true);
                    return null;
                }

//read in trees
                progressText.append("Reading " + nTrees + " trees from " + Tree_File + "\n");

                String[] trees = Tree.treesStringsFromAltnexusFile(new File(Tree_File), nTrees);
                if (trees == null) {
                    progressText.append("Could not read in trees from your tree file " + Tree_File + ". Please check to make sure all comments have been removed from your tree file and your tree file format is altnexus. See README for more information.\n");
                    runButton.setText("Run");
                    runButton.setEnabled(true);
                    return null;
                }
                if (trees.length < nTrees) {
                    progressText.append("Could not read in " + nTrees + " trees from your tree file " + Tree_File + ". Please check to make sure all comments have been removed from your tree file and your tree file format is altnexus. See README for more information.\n");
                    runButton.setText("Run");
                    runButton.setEnabled(true);
                    return null;
                }

//check that the taxon names in the trees match the taxon names in the character data file
                try {
                    String[] taxonNamesTree = Tree.getTaxonLabels(new Tree(trees[0]));
                    if (taxonNamesTree.length != taxonNames.length + nTaxTrim) {
                        progressText.append("The number of taxa in your data file does not match the number of taxa in your trees. Please check to make sure the names and the number of taxa match in your data file and your trees.\n\tThere are " + taxonNames.length + " + taxa in your data file, and " + taxonNamesTree.length + " taxa in your trees.");
                        for (int n = 0; n < taxonNames.length; n++) {
                            progressText.append("Taxon in tree: '" + taxonNamesTree[n] + "'");
                        }
                        for (int n = 0; n < taxonNames.length; n++) {
                            progressText.append("Taxon in data file: '" + taxonNames[n] + "'");
                        }
                        runButton.setText("Run");
                        runButton.setEnabled(true);
                        return null;
                    }
                    for (int l = 0; l < taxonNames.length; l++) {
                        boolean hit = false;
                        for (int m = 0; m < taxonNamesTree.length; m++) {
                            if (taxonNamesTree[m].equals(taxonNames[l])) {
                                hit = true;
                                m = taxonNamesTree.length;
                            }
                        }
                        if (!hit) {
                            progressText.append(taxonNames[l] + " from your data file did not match any taxon names in your tree. Please check to make sure the names and the number of taxa match in your data file and your trees.\n");
                            runButton.setText("Run");
                            runButton.setEnabled(true);
                            return null;
                        }
                    }
                } catch (Exception tbad) {
                    progressText.append("There was an error parsing your trees. Please make sure your trees are in an altnexus-format file, your tree file, " + Tree_File + ", has no comments, and it has no blank lines.\n");
                    tbad.printStackTrace();
                    runButton.setText("Run");
                    runButton.setEnabled(true);
                    return null;
                }

//read in tests
                progressText.append("Reading in comparisons to make.\n");
                progressText.setCaretPosition(progressText.getText().length());


                BufferedReader d = new BufferedReader(new FileReader(new File(File_Listing_Tests)));
                String line;
                int totTests = 0;
                while ((line = d.readLine()) != null) {
                    if (line.length() > 0) {
                        totTests++;
                    }
                }
                d.close();

                d = new BufferedReader(new FileReader(new File(File_Listing_Tests)));

                int testCnt = 0;
//foreach test
                while ((line = d.readLine()) != null) {
//open output file
                    if (line.length() <= 0) {
                        continue;
                    }
                    if (progress > 100) {

                        break;

                    }
                    String[] temp = line.split("\\t");
                    String c1 = temp[0];
                    String c2 = temp[1];
                    String typeC1 = temp[2];
                    String typeC2 = temp[3];
                    progressText.append("Test " + testCnt + ": " + c1 + " " + c2 + " " + typeC1 + " " + typeC2 + "\n");
                    progressText.setCaretPosition(progressText.getText().length());
                    FileWriter writer = new FileWriter("Results/" + Output_File + "_" + c1 + "_X_" + c2 + ".txt");
                    writer.write("Test Number\tTest Variable 1\tTest Variable 2\tVariable 1 Type\tVariable 2 Type\tRandom_Actual_Data\tTree Number\tContrast Node 1\tContrast Node 2\tContrast Variable 1\tContrast Variable 2\n");
                    progressText.updateUI();
//	foreach tree
                    for (int i = 0; i < trees.length; i++) {
                        if ((i + 1) % 1000 == 0) {
                            progressText.append("\t" + (i + 1));
                            progressText.setCaretPosition(progressText.getText().length());
                        }
                        Tree rTree = new Tree(trees[i]);
                        Tree eTree = new Tree(trees[i]);

                        if (taxa2Trim != null) {
                            for (int j = 0; j < taxa2Trim.length; j++) {
                                eTree = Tree.trimTaxon(eTree, taxa2Trim[j], true);
                                rTree = Tree.trimTaxon(rTree, taxa2Trim[j], true);
                            }
                            if (rTree == null) {
                                progressText.append("Error trimming taxa from tree " + i + " for random data.\n");
                                runButton.setText("Run");
                                runButton.setEnabled(true);
                                return null;
                            }
                            if (eTree == null) {
                                progressText.append("Error trimming taxa from tree" + i + "\n");
                                runButton.setText("Run");
                                runButton.setEnabled(true);
                                return null;
                            }
                        }

                        String preTextRand = Integer.toString(testCnt) + "\t" + c1 + "\t" + c2 + "\t" + typeC1 + "\t" + typeC2 + "\tRandom\t" + Integer.toString(i) + "\t";
                        String preTextAct = Integer.toString(testCnt) + "\t" + c1 + "\t" + c2 + "\t" + typeC1 + "\t" + typeC2 + "\tActual\t" + Integer.toString(i) + "\t";
                        String[] randTax = new String[5000];
                        Hashtable randomHash = new Hashtable();
                        if (!exactNull) {
//		evolve random data
                            if (i == 0) {
                                File tDir = new File("RandomData_1/" + c1 + "__" + c2 + "/");
                                if (!tDir.mkdirs() && !tDir.exists()) {
                                    progressText.append("Could not make directory RandomData_1/" + c1 + "__" + c2 + "\n");
                                    runButton.setText("Run");
                                    runButton.setEnabled(true);
                                    return null;
                                }
                            }
                            if (!ce.makeRandomData(i, rTree, new String("RandomData_1/" + c1 + "__" + c2 + "/" + c1 + "__" + c2), (String[]) charHash.get(c1), (String[]) charHash.get(c2), typeC1, typeC2)) {
                                progressText.append("Random data could not be created for unknown reason.\n");
                                runButton.setText("Run");
                                runButton.setEnabled(true);
                                return null;
                            }
//		read in random data
                            randomHash = CharacterEvolution.characterHashFromTabText("RandomData_1/" + c1 + "__" + c2 + "/" + c1 + "__" + c2 + "_R_" + i, nTax);
//		**Delete random data due to storage considerations 1600 comparisons * 10000 trees per comparison equals 16,000,000 files - spells danger.
                            File tempRF = new File("RandomData_1/" + c1 + "__" + c2 + "/" + c1 + "__" + c2 + "_R_" + i);
                            if (Keep_Random_Data != null) {
                                if (Keep_Random_Data.toLowerCase().equals("no")) {
                                    if (!tempRF.delete()) {
                                        progressText.append("Could not delete RandomData_1/" + c1 + "__" + c2 + "/" + c1 + "__" + c2 + "_R_" + i + "\n");
                                    }
                                }
                            }
                            randTax = (String[]) randomHash.get("species");

                            if (randTax.length != taxonNames.length) {
                                progressText.append("Error making random characters on tree. Taxon names do not match.\n");
                                runButton.setText("Run");
                                runButton.setEnabled(true);
                                return null;
                            }

                           
                        }

//Check to make sure the variable names in the file listing tests match the variable names in the data file
                        String[] tC1 = (String[]) charHash.get(c1);
                        String[] tC2 = (String[]) charHash.get(c2);
                        if (tC1 == null) {
                            progressText.append("Character " + c1 + " did not match any of the characters in your data file " + Character_Data_File + ". Please check to make sure the names of the variables in your file listing your tests are the same as the names in the header line of your character data file.\n");
                            runButton.setText("Run");
                            runButton.setEnabled(true);
                            return null;
                        }
                        if (tC2 == null) {
                            progressText.append("Character " + c2 + " did not match any of the characters in your data file " + Character_Data_File + ". Please check to make sure the names of the variables in your file listing your tests are the same as the names in the header line of your character data file.\n");
                            runButton.setText("Run");
                            runButton.setEnabled(true);
                            return null;
                        }
                        if (tC1.length != taxonNames.length) {
                            progressText.append("Data for character " + c1 + " are not listed for all taxa.\n");
                            runButton.setText("Run");
                            runButton.setEnabled(true);
                            return null;
                        }
                        if (tC2.length != taxonNames.length) {
                            progressText.append("Data for character " + c2 + " are not listed for all taxa.\n");
                            runButton.setText("Run");
                            runButton.setEnabled(true);
                            return null;
                        }
                        
                                
//		calculate contrasts for random data
                        //System.out.println("Random tree");
                        if (!exactNull) {
                            if (typeC1.equals("dich") && typeC2.equals("cont")) {
                                //System.out.println("dich cont");
                                //CharacterEvolution.calculateBrunchContrasts(rTree, randTax, (String[]) randomHash.get("char1"), Utilities.StringArrayToDoubleArray((String[]) randomHash.get("char2")), writer, preTextRand);
                                CharacterEvolution.calculateBrunchContrasts(rTree, taxonNames, randTax, (String[]) charHash.get(c1), Utilities.StringArrayToDoubleArray((String[]) randomHash.get("char2")), writer, preTextRand);
                            } else if (typeC1.equals("cont") && typeC2.equals("cont")) {
                                CharacterEvolution.assignICStates(rTree, randTax, randTax, Utilities.StringArrayToDoubleArray((String[]) randomHash.get("char1")), Utilities.StringArrayToDoubleArray((String[]) randomHash.get("char2")), writer, preTextRand);
                                //CharacterEvolution.assignICStates(rTree, taxonNames, randTax, Utilities.StringArrayToDoubleArray((String[]) charHash.get(c1)), Utilities.StringArrayToDoubleArray((String[]) randomHash.get("char2")), writer, preTextRand);
                            } else {
                                progressText.append("Your characters need to be defined as either continuous (cont) or dichotomous (dich) in your file that lists tests, " + File_Listing_Tests + ". Both characters can be continuous, but at most one of them can be dichotomous. Please see README for additional information on appropriate formatting of " + File_Listing_Tests + ".\n");
                                runButton.setText("Run");
                                runButton.setEnabled(true);
                                return null;
                            }
                        }
//		calculate contrasts for acutal data
                        //System.out.println("Actual tree");
                        if (typeC1.equals("dich") && typeC2.equals("cont")) {

                            CharacterEvolution.calculateBrunchContrasts(eTree, taxonNames, taxonNames, (String[]) charHash.get(c1), Utilities.StringArrayToDoubleArray((String[]) charHash.get(c2)), writer, preTextAct);
                        } else if (typeC1.equals("cont") && typeC2.equals("cont")) {
                            CharacterEvolution.assignICStates(eTree, taxonNames, taxonNames, Utilities.StringArrayToDoubleArray((String[]) charHash.get(c1)), Utilities.StringArrayToDoubleArray((String[]) charHash.get(c2)), writer, preTextAct);
                        } else {
                            progressText.append("Your characters need to be defined as either continuous (cont) or dichotomous (dich) in your file that lists tests, " + File_Listing_Tests + ". Both characters can be continuous, but at most one of them can be dichotomous. Please see README for additional information on appropriate formatting of " + File_Listing_Tests + ".\n");
                            runButton.setText("Run");
                            runButton.setEnabled(true);
                            return null;
                        }
                    }
                    progressText.append("\n");
                    progressText.setCaretPosition(progressText.getText().length());
                    testCnt++;
                    progress = (int) testCnt * 100 / totTests;
                    if (progress < 0) {
                        progress = 0;
                    }
                    setProgress(Math.min(progress, 100));
                    progressMonitorR.setProgress(Math.min(progress, 100));

                    writer.close();
//	summarize contrasts for the test
                    progressText.append("Summarize contrasts for " + Output_File + "_" + c1 + "_X_" + c2 + ".txt\n");
                    progressText.setCaretPosition(progressText.getText().length());
                    CharacterEvolution.summarizeContrastData("Results/" + Output_File + "_" + c1 + "_X_" + c2 + ".txt", exactNull, rng);

                    try
                    {
                        (new File("Results/" + Output_File + "_" + c1 + "_X_" + c2 + ".txt")).delete();
                    }
                    catch(Exception fe)
                    {
                        fe.printStackTrace(); progressText.append("Could not delete file Results/" + Output_File + "_" + c1 + "_X_" + c2 + ".txt\n Please be aware of the large file size...");

                    }
                    //Utilities.runSystemCommand("gzip Results/" + Output_File + "_" + c1 + "_X_" + c2 + ".txt");

                    files.add("Results/" + Output_File + "_" + c1 + "_X_" + c2 + ".txt.Summary");
                }
                d.close();
                String[] fs = new String[files.size()];
                files.copyInto(fs);
                progressText.append("Summarize comparative tests and writing results to Results/" + File_Listing_Tests + ".Summary\n");
                progressText.setCaretPosition(progressText.getText().length());
                File_Listing_Tests = File_Listing_Tests.replaceAll("/", ".");
                CharacterEvolution.summarizeComparativeTests(fs, trees.length, "Results/" + File_Listing_Tests + ".Summary", false);




            } catch (Exception ignore) {
                progressText.append("Unknown error.\nPlease make sure the input number of taxa is correct and file names are correct.\n\n");
                progressText.setCaretPosition(progressText.getText().length());
                runButton.setText("Run");
                runButton.setEnabled(true);
                ignore.printStackTrace();
                return null;
            }
            // showMessage("Done with input");
            setProgress(100);
            progressMonitorR.close();
            progressMonitorR = null;

            progressText.setCaretPosition(progressText.getText().length());
            runButton.setText("Run");
            runButton.setEnabled(true);
            return null;
        }

        @Override
        public void done() {
            //System.out.println("In done");
            try {
                //showMessage("Beginning Done method");
                Toolkit.getDefaultToolkit().beep();

                progressText.setCaretPosition(progressText.getText().length());
                runButton.setText("Run");
                runButton.setEnabled(true);
                //showMessage("Done beeping");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //progressMonitor.setProgress(0);
        }
    }

private void savePropertiesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savePropertiesMenuItemActionPerformed

    String out = null;

    Character_Data_File = DataFileText.getText();
    File_Listing_Tests = TestListFileText.getText();
    Tree_File = TreeFileText.getText();
    Output_File = OutputFileText.getText();
    Seed = RandomSeedText.getText();
    Trim_Taxa = TaxaToExcludeText.getText();
    Number_Trees = TreeNumberText.getText();
    Number_Taxa = NumberTaxaText.getText();

    String tAppend = "no";
    String tKRD = "no";
    if (appendSummariesCheckBox.isSelected()) {
        tAppend = "yes";
    }
    if (keepRandomDataCheckBox.isSelected()) {
        tKRD = "yes";
    }


    out = "Character_Data_File = " + Character_Data_File + "\nFile_Listing_Tests = " + File_Listing_Tests + "\nTree_File = " + Tree_File + "\nOutput_File = " + Output_File + "\nSeed = " + Seed + "\nTrim_Taxa = " + Trim_Taxa + "\nNumber_Trees = " + Number_Trees + "\nNumber Taxa = " + Number_Taxa + "\nAppend_Summaries = " + tAppend + "\nKeep_Random_Data = " + tKRD + "\n";

    Utilities.writeFile("Properties.txt", out);

}//GEN-LAST:event_savePropertiesMenuItemActionPerformed

private void saveOutputMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveOutputMenuItemActionPerformed
// TODO add your handling code here:
    String file = OutputFileText.getText();
    file += "ProgressLog.txt";
    progressText.append("Writing log to " + file);
    progressText.setCaretPosition(progressText.getText().length());
    Utilities.writeFile(file, progressText.getText());
}//GEN-LAST:event_saveOutputMenuItemActionPerformed

private void exactNullCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exactNullCheckBoxActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_exactNullCheckBoxActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DataFileText;
    private javax.swing.JTextField NumberTaxaText;
    private javax.swing.JTextField OutputFileText;
    private javax.swing.JTextField RandomSeedText;
    private javax.swing.JTextField TaxaToExcludeText;
    private javax.swing.JTextField TestListFileText;
    private javax.swing.JTextField TreeFileText;
    private javax.swing.JTextField TreeNumberText;
    private javax.swing.JCheckBox appendSummariesCheckBox;
    private javax.swing.JCheckBox exactNullCheckBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JCheckBox keepRandomDataCheckBox;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openDataFileMenuItem;
    private javax.swing.JMenuItem openPropertiesFileMenuItem;
    private javax.swing.JMenuItem openTestListFileMenuItem;
    private javax.swing.JMenuItem openTreeFileMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextArea progressText;
    private javax.swing.JButton runButton;
    private javax.swing.JMenuItem saveOutputMenuItem;
    private javax.swing.JMenuItem savePropertiesMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            //System.out.println("Property change");
            int progress = (Integer) evt.getNewValue();
            String message =
                    String.format("Completed %d%%\n", progress);

            if (progressMonitorR != null) {
                progressMonitorR.setProgress(progress);
                progressMonitorR.setNote(message);
                if ((progressMonitorR.isCanceled() || rtask.isDone())) {
                    Toolkit.getDefaultToolkit().beep();
                }
                if (progressMonitorR.isCanceled()) {
                    rtask.progress = 100 + 1; // quit the loop
                    rtask.cancel(true);
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
