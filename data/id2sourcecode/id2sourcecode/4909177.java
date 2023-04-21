    void init() {
        Thread backgroundTask_ = new Thread() {

            public void run() {
                synchronized (Dterm.this) {
                    chooser = new JFileChooser();
                    javax.swing.filechooser.FileFilter filter_ = new javax.swing.filechooser.FileFilter() {

                        public boolean accept(File f_) {
                            return f_.isDirectory() || f_.getName().endsWith(".tcl");
                        }

                        public String getDescription() {
                            return "TCL Script (*.tcl)";
                        }
                    };
                    chooser.setFileFilter(filter_);
                    Dterm.this.notify();
                }
            }
        };
        synchronized (Dterm.this) {
            backgroundTask_.setPriority(Thread.MIN_PRIORITY);
            backgroundTask_.start();
            frame = new JFrame(title);
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e_) {
                    write(Shell.COMMAND_EXIT + "\n");
                    exit();
                }

                public void windowActivated(WindowEvent e_) {
                    isfocused = true;
                }

                public void windowDeactivated(WindowEvent e_) {
                    isfocused = false;
                }
            });
            mBar = new JMenuBar();
            mFile = new JMenu("File");
            mFile.setMnemonic(KeyEvent.VK_F);
            JMenuItem mi_ = new JMenuItem("Open...");
            mi_.setMnemonic(KeyEvent.VK_O);
            mi_.addActionListener(this);
            mFile.add(mi_);
            mFile.addSeparator();
            mi_ = new JMenuItem("Save");
            mi_.setMnemonic(KeyEvent.VK_S);
            mi_.addActionListener(this);
            mFile.add(mi_);
            mi_ = new JMenuItem("Save As...");
            mi_.setMnemonic(KeyEvent.VK_V);
            mi_.addActionListener(this);
            mFile.add(mi_);
            mi_ = new JMenuItem("Append To...");
            mi_.setMnemonic(KeyEvent.VK_A);
            mi_.addActionListener(this);
            mFile.add(mi_);
            mFile.addSeparator();
            mi_ = new JMenuItem("Exit");
            mi_.setMnemonic(KeyEvent.VK_X);
            mi_.addActionListener(this);
            mFile.add(mi_);
            mBar.add(mFile);
            mEdit = new JMenu("Edit");
            mEdit.setMnemonic(KeyEvent.VK_E);
            lineWrap = new JMenuItem("Set Line Wrap");
            lineWrap.setMnemonic(KeyEvent.VK_L);
            lineWrap.addActionListener(this);
            mEdit.add(lineWrap);
            outputEnable = new JMenuItem("Disable Terminal Display");
            outputEnable.setMnemonic(KeyEvent.VK_T);
            outputEnable.addActionListener(this);
            mEdit.add(outputEnable);
            mi_ = new JMenuItem("Copy");
            mi_.setMnemonic(KeyEvent.VK_C);
            mi_.addActionListener(this);
            mEdit.add(mi_);
            mi_ = new JMenuItem("Paste");
            mi_.setMnemonic(KeyEvent.VK_P);
            mi_.addActionListener(this);
            mEdit.add(mi_);
            mBar.add(mEdit);
            ta = new JTextArea(ScrHeight, ScrWidth);
            ta.setFont(new Font("Courier", Font.PLAIN, 12));
            tf = new JTextArea(1, ScrWidth);
            tf.setBorder(BorderFactory.createLineBorder(Color.gray));
            scrollPane = new JScrollPane(ta, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            frame.getContentPane().add(tf, BorderLayout.SOUTH);
            if (isTerminalDisplayEnabled()) frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
            frame.setJMenuBar(mBar);
            frame.pack();
            ta.addKeyListener(tt);
            tf.addKeyListener(tt);
            tf.requestFocus();
        }
    }
