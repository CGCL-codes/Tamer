    public LookAndFeelPrefPanel() {
        installExtraLookAndFeels();
        UIManager.LookAndFeelInfo[] installedLnfs = UIManager.getInstalledLookAndFeels();
        LnF[] lnfs = new LnF[installedLnfs.length];
        for (int i = 0; i < lnfs.length; i++) {
            lnfs[i] = new LnF(installedLnfs[i]);
        }
        if ((System.getSecurityManager() != null) && PlatformUtils.isJavaBetterThan("1.5")) {
            int gtkIndex = -1;
            for (int i = 0; i < lnfs.length; i++) {
                if (lnfs[i].classname.equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) {
                    gtkIndex = i;
                    break;
                }
            }
            if (gtkIndex != -1) {
                LnF[] lnfs2 = new LnF[lnfs.length - 1];
                for (int i = 0; i < gtkIndex; i++) lnfs2[i] = lnfs[i];
                for (int i = gtkIndex; i < lnfs2.length; i++) lnfs2[i] = lnfs[i + 1];
                lnfs = lnfs2;
            }
        }
        this.lookAndFeels = new JList(lnfs);
        String lnfClassName = Jin.getInstance().getPrefs().getString("lookAndFeel.classname");
        for (int i = 0; i < lookAndFeels.getModel().getSize(); i++) {
            String classname = ((LnF) lookAndFeels.getModel().getElementAt(i)).classname;
            if (lnfClassName.equals(classname)) {
                lookAndFeels.setSelectedIndex(i);
                lookAndFeels.ensureIndexIsVisible(i);
                break;
            }
        }
        createUI();
    }
