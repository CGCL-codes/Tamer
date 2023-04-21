    protected void init() {
        page = new PDFPagePanel();
        page.addKeyListener(this);
        if (doThumb) {
            split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            split.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, thumbAction);
            split.setOneTouchExpandable(true);
            thumbs = new PDFThumbPanel(null);
            thumbscroll = new JScrollPane(thumbs, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            split.setLeftComponent(thumbscroll);
            split.setRightComponent(page);
            getContentPane().add(split, BorderLayout.CENTER);
        } else {
            getContentPane().add(page, BorderLayout.CENTER);
        }
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        JButton jb;
        jb = new JButton(firstAction);
        jb.setText("");
        toolbar.add(jb);
        jb = new JButton(prevAction);
        jb.setText("");
        toolbar.add(jb);
        pageField = new JTextField("-", 3);
        pageField.setMaximumSize(new Dimension(45, 32));
        pageField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                doPageTyped();
            }
        });
        toolbar.add(pageField);
        jb = new JButton(nextAction);
        jb.setText("");
        toolbar.add(jb);
        jb = new JButton(lastAction);
        jb.setText("");
        toolbar.add(jb);
        toolbar.add(Box.createHorizontalGlue());
        fullScreenButton = new JToggleButton(fullScreenAction);
        fullScreenButton.setText("");
        toolbar.add(fullScreenButton);
        fullScreenButton.setEnabled(true);
        toolbar.add(Box.createHorizontalGlue());
        JToggleButton jtb;
        ButtonGroup bg = new ButtonGroup();
        jtb = new JToggleButton(zoomToolAction);
        jtb.setText("");
        bg.add(jtb);
        toolbar.add(jtb);
        jtb = new JToggleButton(fitInWindowAction);
        jtb.setText("");
        bg.add(jtb);
        jtb.setSelected(true);
        toolbar.add(jtb);
        toolbar.add(Box.createHorizontalGlue());
        jb = new JButton(printAction);
        jb.setText("");
        toolbar.add(jb);
        getContentPane().add(toolbar, BorderLayout.NORTH);
        JMenuBar mb = new JMenuBar();
        JMenu file = new JMenu("File");
        file.add(openAction);
        file.add(closeAction);
        file.addSeparator();
        file.add(pageSetupAction);
        file.add(printAction);
        file.addSeparator();
        file.add(quitAction);
        mb.add(file);
        JMenu view = new JMenu("View");
        JMenu zoom = new JMenu("Zoom");
        zoom.add(zoomInAction);
        zoom.add(zoomOutAction);
        zoom.add(fitInWindowAction);
        zoom.setEnabled(false);
        view.add(zoom);
        view.add(fullScreenAction);
        if (doThumb) {
            view.addSeparator();
            view.add(thumbAction);
        }
        mb.add(view);
        setJMenuBar(mb);
        setEnabling();
        pack();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - getWidth()) / 2;
        int y = (screen.height - getHeight()) / 2;
        setLocation(x, y);
        if (SwingUtilities.isEventDispatchThread()) {
            setVisible(true);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    public void run() {
                        setVisible(true);
                    }
                });
            } catch (InvocationTargetException ie) {
            } catch (InterruptedException ie) {
            }
        }
    }
