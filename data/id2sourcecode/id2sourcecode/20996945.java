    public GUI() {
        selectedFiles = null;
        setTitle("jUML_" + jUML.VERSION);
        FontSize = 12;
        GlobalVariables.initTree();
        JP = new JavaParser();
        Right = new JTabbedPaneWithCloseIcons();
        JFCIm = new JFileChooser();
        JFCOp = new JFileChooser();
        Classes = new Vector<ClassData>();
        JFCIm.setFileFilter(new JavaFilter());
        JFCOp.setFileFilter(new XMLFilter());
        Right.setSize(600, 600);
        JScrollPane l = new JScrollPane();
        JScrollPane r = new JScrollPane();
        l.setBackground(Color.WHITE);
        r.setBackground(Color.WHITE);
        l.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        l.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        r.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        r.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        Window = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, l, r);
        Window.setDividerLocation(200);
        testCase = new GenerateTestCase();
        fwSaveDir = "";
        choseSaveDir = false;
        add(Window);
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        JMenuItem newProject = new JMenuItem("New project");
        newProject.setMnemonic(KeyEvent.VK_N);
        JMenuItem fileImp = new JMenuItem("Import source files");
        fileImp.setMnemonic(KeyEvent.VK_I);
        JMenuItem fileOpen = new JMenuItem("Open Project");
        fileOpen.setMnemonic(KeyEvent.VK_O);
        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.setMnemonic(KeyEvent.VK_X);
        saveUML = new JMenuItem("Save project");
        saveUML.setMnemonic(KeyEvent.VK_S);
        saveUML.setEnabled(false);
        JMenuItem openUML = new JMenuItem("Open project");
        openUML.setMnemonic(KeyEvent.VK_R);
        saveUML.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                saveFile();
            }
        });
        openUML.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                openProj();
            }
        });
        newProject.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (GlobalVariables.CLASS != null) GlobalVariables.CLASS.clear();
                saveUML.setEnabled(true);
                diagram = new Diagram(Window);
            }
        });
        fileExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (saveUML.isEnabled()) {
                    System.exit(0);
                } else {
                    System.exit(0);
                }
            }
        });
        fileImp.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                ImportFiles();
            }
        });
        file.add(newProject);
        file.add(saveUML);
        file.add(openUML);
        file.add(fileImp);
        file.add(fileExit);
        JMenu diagrams = new JMenu("Diagrams");
        diagrams.setMnemonic(KeyEvent.VK_D);
        JMenuItem InhDiag = new JMenuItem("Inheritance diagram");
        InhDiag.setMnemonic(KeyEvent.VK_H);
        JMenuItem IntDiag = new JMenuItem("Interface diagram");
        IntDiag.setMnemonic(KeyEvent.VK_F);
        JMenuItem AggDiag = new JMenuItem("Aggregation diagram");
        AggDiag.setMnemonic(KeyEvent.VK_A);
        InhDiag.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (Right.getComponentCount() == 3) {
                    if (Right.getComponent(0).getName().equals("Inheritance")) {
                        Right.setSelectedIndex(0);
                    } else if (Right.getComponent(1).getName().equals("Inheritance")) {
                        Right.setSelectedIndex(1);
                    } else {
                        Right.setSelectedIndex(2);
                    }
                } else if (Right.getComponentCount() == 2) {
                    if (Right.getComponent(0).getName().equals("Inheritance")) {
                        Right.setSelectedIndex(0);
                    } else if (Right.getComponent(1).getName().equals("Inheritance")) {
                        Right.setSelectedIndex(1);
                    } else {
                        DrawInheritance();
                        Right.setSelectedIndex(2);
                        Right.getComponent(2).setName("Inheritance");
                    }
                } else if (Right.getComponentCount() == 1) {
                    if (Right.getComponent(0).getName().equals("Inheritance")) Right.setSelectedIndex(0); else {
                        DrawInheritance();
                        Right.setSelectedIndex(1);
                        Right.getComponent(1).setName("Inheritance");
                    }
                } else {
                    DrawInheritance();
                    Right.setSelectedIndex(0);
                    Right.getComponent(0).setName("Inheritance");
                }
            }
        });
        IntDiag.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (Right.getComponentCount() == 3) {
                    if (Right.getComponent(0).getName().equals("Interface")) {
                        Right.setSelectedIndex(0);
                    } else if (Right.getComponent(1).getName().equals("Interface")) {
                        Right.setSelectedIndex(1);
                    } else {
                        Right.setSelectedIndex(2);
                    }
                } else if (Right.getComponentCount() == 2) {
                    if (Right.getComponent(0).getName().equals("Interface")) {
                        Right.setSelectedIndex(0);
                    } else if (Right.getComponent(1).getName().equals("Interface")) {
                        Right.setSelectedIndex(1);
                    } else {
                        DrawInt();
                        Right.setSelectedIndex(2);
                        Right.getComponent(2).setName("Interface");
                    }
                } else if (Right.getComponentCount() == 1) {
                    if (Right.getComponent(0).getName().equals("Interface")) ; else {
                        DrawInt();
                        Right.setSelectedIndex(1);
                        Right.getComponent(1).setName("Interface");
                    }
                } else {
                    DrawInt();
                    Right.setSelectedIndex(0);
                    Right.getComponent(0).setName("Interface");
                }
            }
        });
        AggDiag.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (Right.getComponentCount() == 3) {
                    if (Right.getComponent(0).getName().equals("Aggregation")) {
                        Right.setSelectedIndex(0);
                    } else if (Right.getComponent(1).getName().equals("Aggregation")) {
                        Right.setSelectedIndex(1);
                    } else {
                        Right.setSelectedIndex(2);
                    }
                } else if (Right.getComponentCount() == 2) {
                    if (Right.getComponent(0).getName().equals("Aggregation")) {
                        Right.setSelectedIndex(0);
                    } else if (Right.getComponent(1).getName().equals("Aggregation")) {
                        Right.setSelectedIndex(1);
                    } else {
                        DrawAggregation();
                        Right.setSelectedIndex(2);
                        Right.getComponent(2).setName("Aggregation");
                    }
                } else if (Right.getComponentCount() == 1) {
                    if (Right.getComponent(0).getName().equals("Aggregation")) ; else {
                        DrawAggregation();
                        Right.setSelectedIndex(1);
                        Right.getComponent(1).setName("Aggregation");
                    }
                } else {
                    DrawAggregation();
                    Right.getComponent(0).setName("Aggregation");
                }
            }
        });
        diagrams.add(InhDiag);
        diagrams.add(IntDiag);
        diagrams.add(AggDiag);
        JMenu tools = new JMenu("Tools");
        tools.setMnemonic(KeyEvent.VK_T);
        JMenuItem generateCode = new JMenuItem("Generate Code");
        generateCode.setMnemonic(KeyEvent.VK_G);
        final JMenuItem compileCode = new JMenuItem("Compile Code");
        compileCode.setMnemonic(KeyEvent.VK_C);
        compileCode.setEnabled(false);
        final JMenuItem generateTestCase = new JMenuItem("Generate Test Case");
        generateTestCase.setMnemonic(KeyEvent.VK_T);
        generateTestCase.setEnabled(false);
        final JMenuItem executeCode = new JMenuItem("Execute Code");
        executeCode.setEnabled(false);
        executeCode.setMnemonic(KeyEvent.VK_E);
        generateCode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (!choseSaveDir) {
                    JFrame frame = new JFrame();
                    JFileChooser JFCIm = new JFileChooser();
                    JFCIm.setDialogTitle("Choose save directory");
                    JFCIm.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    JFCIm.setCurrentDirectory(new File("."));
                    JFCIm.setMultiSelectionEnabled(false);
                    int result = JFCIm.showOpenDialog(frame);
                    if (result == JFileChooser.CANCEL_OPTION) return;
                    choseSaveDir = true;
                    fwSaveDir = JFCIm.getSelectedFile().getAbsolutePath() + "/";
                    testCase.setSaveDir(fwSaveDir);
                    generateTestCase.setEnabled(true);
                    compileCode.setEnabled(true);
                }
                GlobalVariables.DISPLAYSRC = true;
                for (umleditor.Node x : GlobalVariables.CLASS) {
                    Thread t = new Thread(new JavaCodeWriter(false, null, fwSaveDir + ((storage.ClassNode) x).getName().getText() + ".java", ((storage.ClassNode) x)));
                    t.start();
                }
            }
        });
        compileCode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Vector<Thread> vT = new Vector<Thread>();
                for (umleditor.Node x : GlobalVariables.CLASS) {
                    Thread t = new Thread(new JavaCompilerWorker(fwSaveDir + ((storage.ClassNode) x).getName().getText() + ".java"));
                    vT.add(t);
                    t.start();
                }
                try {
                    jUML.DBG("CompileCode", "i'm sleepy");
                    Thread.sleep(10);
                    for (int i = 0; i < vT.size(); i++) {
                        if (vT.get(i).isAlive()) {
                            i = -1;
                            Thread.sleep(10);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                jUML.DBG("CompileCode", "checking for errors");
                boolean isErr = false;
                for (umleditor.Node x : GlobalVariables.CLASS) {
                    if (isCompileErr(fwSaveDir, ((storage.ClassNode) x).getName().getText())) {
                        isErr = true;
                        jUML.DBG("CompileCode", "there was an error");
                    }
                }
                if (!isErr) jUML.DBG("CompileCode", "no errors");
            }
        });
        generateTestCase.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Object[] options = { "Save", "Cancel" };
                int a = JOptionPane.showOptionDialog(null, testCase, "Generate Test Case", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon("./misc/fighter.png"), options, options[0]);
                if (a == JOptionPane.OK_OPTION) {
                    testCase.saveSrc();
                    File f = new File(fwSaveDir + "TestCase.java");
                    if (f.exists()) {
                        a = JOptionPane.showConfirmDialog(null, new JLabel("A test case already exists. Overwrite?"), "Confirm overwrite", JOptionPane.OK_CANCEL_OPTION);
                        if (a == JOptionPane.CANCEL_OPTION) return;
                    }
                    executeCode.setEnabled(true);
                    Thread t = new Thread(new JavaCodeWriter(true, testCase.getSrc(), fwSaveDir + "TestCase.java", null));
                    t.start();
                } else {
                    testCase.clearTextArea();
                }
            }
        });
        executeCode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                Thread t = new Thread(new JavaCompilerWorker(fwSaveDir + "TestCase.java"));
                t.start();
                jUML.DBG("Execute", "compiling testcase");
                while (t.isAlive()) ;
                jUML.DBG("Execute", "done compiling testcase");
                if (isCompileErr(fwSaveDir, "TestCase")) return;
                String osType = System.getProperty("os.name");
                String scriptName = null;
                String scriptText = null;
                if (osType.contains("Windows")) {
                    scriptText = "@echo off\n" + "cd " + fwSaveDir + "\n" + "java TestCase\n" + "pause\n" + "exit 0";
                    scriptName = "tmp.bat";
                } else {
                    scriptText = "#! /bin/bash\n\n" + "cd " + fwSaveDir + "\n" + "java TestCase\n" + "read -p \"Press enter to exit\"\n";
                    scriptName = "tmp.sh";
                }
                jUML.DBG("Execute", "creating " + scriptName);
                File f = new File(fwSaveDir + scriptName);
                try {
                    f.createNewFile();
                    FileWriter fout = new FileWriter(f);
                    fout.write(scriptText);
                    fout.flush();
                    fout.close();
                } catch (IOException e) {
                    jUML.DBG("Execute", "error writing " + scriptName);
                    e.printStackTrace();
                    return;
                }
                jUML.DBG("Execute", "done creating " + scriptName);
                f.setExecutable(true);
                jUML.DBG("Execute", "gogogogo");
                String exe = null;
                if (osType.contains("Windows")) {
                    exe = "cmd /c start ";
                } else {
                    exe = "xterm -e ";
                }
                jUML.DBG("ExecCode", exe + fwSaveDir + scriptName);
                try {
                    Runtime.getRuntime().exec(exe + fwSaveDir + scriptName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        tools.add(generateCode);
        tools.add(compileCode);
        tools.add(generateTestCase);
        tools.add(executeCode);
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        JMenuItem About = new JMenuItem("About");
        About.setMnemonic(KeyEvent.VK_A);
        About.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(null, "jUML: the Java UML diagrammer\n" + "Authors: Josh Haynes and Jeff Prillaman", "About this program...", JOptionPane.PLAIN_MESSAGE, new ImageIcon("./misc/vtux_small.png"));
            }
        });
        help.add(About);
        menubar.add(file);
        menubar.add(diagrams);
        menubar.add(tools);
        menubar.add(help);
        setJMenuBar(menubar);
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocation(0, 0);
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);
    }
