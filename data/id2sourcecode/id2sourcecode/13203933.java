    public static Component addHelpLinkToPanel(Container panel, String helpPage) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return panel;
        }
        JLabel icon = new JLabel();
        icon.setIcon(new javax.swing.ImageIcon(JMeterPluginsUtils.class.getResource("/kg/apc/jmeter/vizualizers/information.png")));
        JLabel link = new JLabel("Help on this plugin");
        link.setForeground(Color.blue);
        link.setFont(link.getFont().deriveFont(Font.PLAIN));
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        link.addMouseListener(new URIOpener(WIKI_BASE + helpPage + "?utm_source=jmeter&utm_medium=helplink&utm_campaign=" + helpPage));
        Border border = BorderFactory.createMatteBorder(0, 0, 1, 0, java.awt.Color.blue);
        link.setBorder(border);
        JLabel version = new JLabel("v" + PLUGINS_VERSION);
        version.setFont(version.getFont().deriveFont(Font.PLAIN).deriveFont(11F));
        version.setForeground(Color.GRAY);
        Container innerPanel = findComponentWithBorder((JComponent) panel, EtchedBorder.class);
        JPanel panelLink = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints;
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        panelLink.add(icon, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 3, 0);
        panelLink.add(link, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panelLink.add(version, gridBagConstraints);
        if (innerPanel != null) {
            innerPanel.add(panelLink);
        } else {
            panel.add(panelLink);
        }
        return panel;
    }
