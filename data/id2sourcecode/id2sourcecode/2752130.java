    public void createLayout() {
        final ScalableXHTMLPanel panel = root.panel.view;
        file.add(root.actions.open_file);
        file.add(new JSeparator());
        file.add(root.actions.export_pdf);
        file.add(new JSeparator());
        file.add(root.actions.quit);
        add(file);
        JMenu zoom = new JMenu("Zoom");
        zoom.setMnemonic('Z');
        ScaleFactor[] factors = this.initializeScales();
        ButtonGroup zoomGroup = new ButtonGroup();
        for (int i = 0; i < factors.length; i++) {
            ScaleFactor factor = factors[i];
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(new ZoomAction(panel, factor));
            if (factor.isNotZoomed()) item.setSelected(true);
            zoomGroup.add(item);
            zoom.add(item);
        }
        view.add(new JSeparator());
        view.add(zoom);
        view.add(new JSeparator());
        view.add(new JCheckBoxMenuItem(root.actions.print_preview));
        add(view);
        go.add(root.actions.forward);
        go.add(root.actions.backward);
        add(go);
        JMenu debugShow = new JMenu("Show");
        debug.add(debugShow);
        debugShow.setMnemonic('S');
        debugShow.add(new JCheckBoxMenuItem(new BoxOutlinesAction()));
        debugShow.add(new JCheckBoxMenuItem(new LineBoxOutlinesAction()));
        debugShow.add(new JCheckBoxMenuItem(new InlineBoxesAction()));
        debugShow.add(new JCheckBoxMenuItem(new FontMetricsAction()));
        JMenu anti = new JMenu("Anti Aliasing");
        ButtonGroup anti_level = new ButtonGroup();
        addLevel(anti, anti_level, "None", -1);
        addLevel(anti, anti_level, "Low", 25).setSelected(true);
        addLevel(anti, anti_level, "Medium", 12);
        addLevel(anti, anti_level, "High", 0);
        debug.add(anti);
        debug.add(new ShowDOMInspectorAction());
        debug.add(new AbstractAction("Validation Console") {

            public void actionPerformed(ActionEvent evt) {
                if (root.validation_console == null) {
                    root.validation_console = new JFrame("Validation Console");
                    JFrame frame = root.validation_console;
                    JTextArea jta = new JTextArea();
                    root.error_handler.setTextArea(jta);
                    jta.setEditable(false);
                    jta.setLineWrap(true);
                    jta.setText("Validation Console: XML Parsing Error Messages");
                    frame.getContentPane().setLayout(new BorderLayout());
                    frame.getContentPane().add(new JScrollPane(jta), "Center");
                    JButton close = new JButton("Close");
                    frame.getContentPane().add(close, "South");
                    close.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            root.validation_console.setVisible(false);
                        }
                    });
                    frame.pack();
                    frame.setSize(400, 300);
                }
                root.validation_console.setVisible(true);
            }
        });
        add(debug);
        help.add(root.actions.aboutPage);
        add(help);
    }
