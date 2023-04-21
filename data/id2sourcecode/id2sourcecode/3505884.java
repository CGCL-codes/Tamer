    public MenuChannel(final JLabelChannel labelChannel, final HorizontalViewer main) {
        JMenuItem mbtChangeIcon = new javax.swing.JMenuItem(main.getLocalizer().getString("change_icon"));
        mbtChangeIcon.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                main.changeIconActionPerformed(labelChannel.getChannel());
            }
        });
        add(mbtChangeIcon);
        if (labelChannel.getIcon() != null) {
            JMenuItem mbtResetIcon = new javax.swing.JMenuItem(main.getLocalizer().getString("reset_to_default_icon"));
            mbtResetIcon.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    main.resetIconActionPerformed(labelChannel.getChannel());
                }
            });
            add(mbtResetIcon);
        }
    }
