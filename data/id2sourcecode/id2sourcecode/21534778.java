    public VAWelcomePanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        JPanel pnMain = new JPanel();
        pnMain.setBorder(new CompoundBorder(new EtchedBorder(), new EmptyBorder(new Insets(5, 5, 5, 5))));
        pnMain.setLayout(new GridLayout(2, 1));
        JPanel pnHaut = new JPanel();
        pnHaut.setLayout(new BorderLayout());
        JPanel pnTitle = new JPanel();
        pnTitle.setOpaque(true);
        pnTitle.setBackground(pnMain.getBackground().brighter());
        pnTitle.setLayout(new BorderLayout());
        pnTitle.setBorder(new EmptyBorder(new Insets(10, 2, 10, 2)));
        JLabel lbTitle;
        if (VAGlobals.APP_VERSION != null) lbTitle = new JLabel(VAGlobals.APP_NAME + " " + VAGlobals.APP_VERSION); else lbTitle = new JLabel(VAGlobals.APP_NAME + " (no version)");
        lbTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lbTitle.setForeground(Color.red);
        Font f = lbTitle.getFont().deriveFont(Font.BOLD, (float) 20.);
        lbTitle.setFont(f);
        lbTitle.setOpaque(false);
        pnTitle.add(BorderLayout.NORTH, lbTitle);
        JLabel lbTitle2 = new JLabel(VAGlobals.OPERATION == VAGlobals.INSTALL ? VAGlobals.i18n("UI_Installation") : VAGlobals.OPERATION == VAGlobals.UPDATE ? VAGlobals.i18n("UI_Update") : VAGlobals.OPERATION == VAGlobals.UNINSTALL ? VAGlobals.i18n("UI_Uninstallation") : VAGlobals.i18n("UI_UnknownOperation"));
        lbTitle2.setHorizontalAlignment(SwingConstants.CENTER);
        lbTitle2.setForeground(Color.red);
        f = lbTitle2.getFont().deriveFont(Font.BOLD);
        lbTitle2.setFont(f);
        lbTitle2.setOpaque(false);
        pnTitle.add(BorderLayout.SOUTH, lbTitle2);
        JPanel pnAbout = new JPanel();
        pnAbout.setLayout(new BorderLayout());
        InputStream imgStream = VAGlobals.BASE_CLASS.getResourceAsStream("resources/vailogo.gif");
        JButton btAbout;
        if (imgStream != null) {
            ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            try {
                byte[] buf = new byte[1024];
                int read = imgStream.read(buf, 0, buf.length);
                while (read > 0) {
                    dataStream.write(buf, 0, read);
                    read = imgStream.read(buf, 0, buf.length);
                }
                imgStream.close();
                vaiLogo_ = new ImageIcon(dataStream.toByteArray());
                dataStream.close();
            } catch (IOException ex) {
            }
        }
        if (vaiLogo_ == null) btAbout = new JButton(VAGlobals.i18n("VAWelcomePanel_About") + " " + VAGlobals.NAME); else btAbout = new JButton(vaiLogo_);
        btAbout.addActionListener(this);
        pnAbout.add(BorderLayout.SOUTH, btAbout);
        pnHaut.add(BorderLayout.NORTH, pnTitle);
        pnHaut.add(BorderLayout.SOUTH, new JLabel(VAGlobals.i18n("UI_ClickNextToStart")));
        pnMain.add(pnHaut);
        pnMain.add(pnAbout);
        JComponent pnImage = VAImagePanel.IMAGE_PANEL;
        add(pnImage);
        add(pnMain);
    }
