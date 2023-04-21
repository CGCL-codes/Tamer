    private void initComponents() {
        buttonGroup1 = new javax.swing.ButtonGroup();
        genderGroup = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        open_book = new javax.swing.JButton();
        new_book = new javax.swing.JButton();
        button_save = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        add_category = new javax.swing.JButton();
        Add_Custom_Note_ = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        add_chapter = new javax.swing.JButton();
        Add_Global_Note_ = new javax.swing.JButton();
        Add_Character_ = new javax.swing.JButton();
        Add_Place_ = new javax.swing.JButton();
        remove_node = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        cardDeck = new javax.swing.JPanel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        category_pane = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        NewBook = new javax.swing.JMenuItem();
        LoadBook = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        PrintBook = new javax.swing.JMenuItem();
        menuConvert = new javax.swing.JMenu();
        ConvertPDF = new javax.swing.JMenuItem();
        ConvertDoc = new javax.swing.JMenuItem();
        ConvertHtml = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        menuExit = new javax.swing.JMenuItem();
        AddMenu = new javax.swing.JMenu();
        menuAddCategory = new javax.swing.JMenuItem();
        HelpMenu = new javax.swing.JMenu();
        menu_Tutorial_ = new javax.swing.JMenuItem();
        menu_HelpTopics_ = new javax.swing.JMenuItem();
        menu_AboutWub_ = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(740, 480));
        setName("Form");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jToolBar1.setRollover(true);
        jToolBar1.setName("ToolBar");
        jToolBar1.setPreferredSize(new java.awt.Dimension(585, 25));
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(JFrameMain.class);
        open_book.setText(resourceMap.getString("open_book.text"));
        open_book.setFocusable(false);
        open_book.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        open_book.setName("open_book");
        open_book.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(open_book);
        new_book.setText(resourceMap.getString("new_book.text"));
        new_book.setFocusable(false);
        new_book.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        new_book.setName("new_book");
        new_book.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(new_book);
        button_save.setText(resourceMap.getString("button_save.text"));
        button_save.setName("button_save");
        button_save.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_saveMouseClicked(evt);
            }
        });
        jToolBar1.add(button_save);
        jSeparator3.setName("jSeparator3");
        jToolBar1.add(jSeparator3);
        add_category.setText(resourceMap.getString("add_category.text"));
        add_category.setName("add_category");
        add_category.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_categoryMouseClicked(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                add_categoryMouseExited(evt);
            }
        });
        jToolBar1.add(add_category);
        Add_Custom_Note_.setText(resourceMap.getString("Add_Custom_Note_.text"));
        Add_Custom_Note_.setFocusable(false);
        Add_Custom_Note_.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Add_Custom_Note_.setName("Add_Custom_Note_");
        Add_Custom_Note_.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Add_Custom_Note_.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add_Custom_Note_ActionPerformed(evt);
            }
        });
        jToolBar1.add(Add_Custom_Note_);
        jSeparator4.setName("jSeparator4");
        jToolBar1.add(jSeparator4);
        add_chapter.setText(resourceMap.getString("add_chapter.text"));
        add_chapter.setFocusable(false);
        add_chapter.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        add_chapter.setName("add_chapter");
        add_chapter.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        add_chapter.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                add_chapterMouseClicked(evt);
            }
        });
        jToolBar1.add(add_chapter);
        Add_Global_Note_.setText(resourceMap.getString("Add_Global_Note_.text"));
        Add_Global_Note_.setFocusable(false);
        Add_Global_Note_.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Add_Global_Note_.setName("Add_Global_Note_");
        Add_Global_Note_.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Add_Global_Note_.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                Add_Global_Note_MousePressed(evt);
            }
        });
        jToolBar1.add(Add_Global_Note_);
        Add_Character_.setText(resourceMap.getString("Add_Character_.text"));
        Add_Character_.setFocusable(false);
        Add_Character_.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Add_Character_.setName("Add_Character_");
        Add_Character_.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Add_Character_.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add_Character_ActionPerformed(evt);
            }
        });
        jToolBar1.add(Add_Character_);
        Add_Place_.setText(resourceMap.getString("Add_Place_.text"));
        Add_Place_.setFocusable(false);
        Add_Place_.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Add_Place_.setName("Add_Place_");
        Add_Place_.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(Add_Place_);
        remove_node.setText(resourceMap.getString("remove_node.text"));
        remove_node.setFocusable(false);
        remove_node.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        remove_node.setName("remove_node");
        remove_node.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        remove_node.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                remove_nodeMouseClicked(evt);
            }
        });
        jToolBar1.add(remove_node);
        jSeparator5.setBackground(resourceMap.getColor("jSeparator5.background"));
        jSeparator5.setBorder(javax.swing.BorderFactory.createEtchedBorder(resourceMap.getColor("jSeparator5.border.highlightColor"), null));
        jSeparator5.setName("jSeparator5");
        jToolBar1.add(jSeparator5);
        jButton1.setIcon(resourceMap.getIcon("jButton1.icon"));
        jButton1.setText(resourceMap.getString("jButton1.text"));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(37, 30));
        jButton1.setMinimumSize(new java.awt.Dimension(37, 30));
        jButton1.setName("jButton1");
        jButton1.setPreferredSize(new java.awt.Dimension(37, 30));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jToolBar1.add(jButton1);
        jButton2.setText(resourceMap.getString("jButton2.text"));
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2");
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jToolBar1.add(jButton2);
        jSplitPane1.setDividerLocation(195);
        jSplitPane1.setDividerSize(10);
        jSplitPane1.setAutoscrolls(true);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setMaximumSize(new java.awt.Dimension(2147, 2147));
        jSplitPane1.setMinimumSize(new java.awt.Dimension(136, 26));
        jSplitPane1.setName("jSplitPane1");
        jSplitPane1.setOneTouchExpandable(true);
        jScrollPane1.setAutoscrolls(true);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(3276, 3276));
        jScrollPane1.setName("jScrollPane1");
        jTree1.setAutoscrolls(true);
        jTree1.setEditable(true);
        jTree1.setMaximumSize(new java.awt.Dimension(1000, 2000));
        jTree1.setMinimumSize(new java.awt.Dimension(50, 100));
        jTree1.setName("jTree1");
        jTree1.setPreferredSize(null);
        jTree1.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTree1);
        jSplitPane1.setLeftComponent(jScrollPane1);
        cardDeck.setBackground(resourceMap.getColor("cardDeck.background"));
        cardDeck.setForeground(resourceMap.getColor("cardDeck.foreground"));
        cardDeck.setName("cardDeck");
        cardDeck.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                cardDeckComponentResized(evt);
            }
        });
        cardDeck.setLayout(new java.awt.CardLayout());
        jLayeredPane1.setName("jLayeredPane1");
        jLayeredPane1.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                jLayeredPane1ComponentResized(evt);
            }
        });
        category_pane.setDragEnabled(true);
        category_pane.setName("category_pane");
        category_pane.setPreferredSize(new java.awt.Dimension(106, 200));
        category_pane.setBounds(0, 0, 690, 470);
        jLayeredPane1.add(category_pane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        cardDeck.add(jLayeredPane1, "card2");
        jSplitPane1.setRightComponent(cardDeck);
        jPanel1.setName("jPanel1");
        jPanel1.setPreferredSize(new java.awt.Dimension(5, 15));
        jProgressBar1.setAlignmentX(0.0F);
        jProgressBar1.setAlignmentY(0.0F);
        jProgressBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jProgressBar1.setMaximumSize(new java.awt.Dimension(32, 32));
        jProgressBar1.setName("jProgressBar1");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(649, Short.MAX_VALUE).addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jProgressBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE));
        jMenuBar1.setName("jMenuBar1");
        FileMenu.setText(resourceMap.getString("FileMenu.text"));
        FileMenu.setName("FileMenu");
        NewBook.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        NewBook.setText(resourceMap.getString("NewBook.text"));
        NewBook.setName("NewBook");
        FileMenu.add(NewBook);
        LoadBook.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        LoadBook.setText(resourceMap.getString("LoadBook.text"));
        LoadBook.setName("LoadBook");
        FileMenu.add(LoadBook);
        jSeparator1.setName("jSeparator1");
        FileMenu.add(jSeparator1);
        PrintBook.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        PrintBook.setText(resourceMap.getString("PrintBook.text"));
        PrintBook.setName("PrintBook");
        FileMenu.add(PrintBook);
        menuConvert.setText(resourceMap.getString("menuConvert.text"));
        menuConvert.setName("menuConvert");
        ConvertPDF.setText(resourceMap.getString("ConvertPDF.text"));
        ConvertPDF.setName("ConvertPDF");
        menuConvert.add(ConvertPDF);
        ConvertDoc.setText(resourceMap.getString("ConvertDoc.text"));
        ConvertDoc.setName("ConvertDoc");
        menuConvert.add(ConvertDoc);
        ConvertHtml.setText(resourceMap.getString("ConvertHtml.text"));
        ConvertHtml.setName("ConvertHtml");
        menuConvert.add(ConvertHtml);
        FileMenu.add(menuConvert);
        jSeparator2.setName("jSeparator2");
        FileMenu.add(jSeparator2);
        menuExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        menuExit.setText(resourceMap.getString("menuExit.text"));
        menuExit.setName("menuExit");
        menuExit.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuExitMousePressed(evt);
            }
        });
        FileMenu.add(menuExit);
        jMenuBar1.add(FileMenu);
        AddMenu.setText(resourceMap.getString("AddMenu.text"));
        AddMenu.setName("AddMenu");
        menuAddCategory.setText(resourceMap.getString("menuAddCategory.text"));
        menuAddCategory.setName("menuAddCategory");
        menuAddCategory.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuAddCategoryMousePressed(evt);
            }
        });
        AddMenu.add(menuAddCategory);
        jMenuBar1.add(AddMenu);
        HelpMenu.setText(resourceMap.getString("HelpMenu.text"));
        HelpMenu.setName("HelpMenu");
        menu_Tutorial_.setText(resourceMap.getString("menu_Tutorial_.text"));
        menu_Tutorial_.setName("menu_Tutorial_");
        HelpMenu.add(menu_Tutorial_);
        menu_HelpTopics_.setText(resourceMap.getString("menu_HelpTopics_.text"));
        menu_HelpTopics_.setName("menu_HelpTopics_");
        HelpMenu.add(menu_HelpTopics_);
        menu_AboutWub_.setText(resourceMap.getString("menu_AboutWub_.text"));
        menu_AboutWub_.setName("menu_AboutWub_");
        menu_AboutWub_.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                menu_AboutWub_MousePressed(evt);
            }
        });
        HelpMenu.add(menu_AboutWub_);
        jMenuBar1.add(HelpMenu);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 819, Short.MAX_VALUE).addContainerGap()).addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)));
        pack();
    }
