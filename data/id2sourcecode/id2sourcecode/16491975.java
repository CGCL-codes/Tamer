    public Object login(boolean showMeaning) {
        String action = "Signing";
        GridBagConstraints gridBagConstraints;
        if (!showMeaning) action = "Verify";
        String itemType = (String) getNObNode().get("level");
        String capItemType = itemType.substring(0, 1).toUpperCase() + itemType.substring(1);
        String dialogTitle = VersionInfo.Version + ": " + action + " " + capItemType + ": " + getNObNode().get("label");
        Label unamelabel = new Label("Username:");
        Label pwlabel = new Label("Password:");
        Label meaningslabel = new Label("Meanings:");
        Choice meanings = new Choice();
        for (int i = 0; i < gAllowedMeanings.length; i++) {
            meanings.addItem(gAllowedMeanings[i]);
        }
        Label commentlabel = new Label("Comments:");
        TextArea comments = new TextArea("", 5, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
        JPasswordField password = new JPasswordField(15);
        password.setEditable(true);
        JPanel pane = new JPanel();
        pane.setLayout(new java.awt.GridBagLayout());
        pane.setBorder(BorderFactory.createLineBorder(Color.gray, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        pane.add(password, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pane.add(unamelabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pane.add(pwlabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        pane.add(username, gridBagConstraints);
        if (showMeaning) {
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            pane.add(meaningslabel, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.ipadx = 125;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
            pane.add(meanings, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.gridheight = 6;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
            pane.add(commentlabel, gridBagConstraints);
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 5;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 29);
            pane.add(comments, gridBagConstraints);
        }
        User roamingUser = null;
        int choice = JOptionPane.showOptionDialog(null, pane, dialogTitle, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (choice == JOptionPane.OK_OPTION) {
            String uname = username.getText().trim();
            String pword = new String(password.getPassword());
            String comment = comments.getText().trim();
            String meaning = meanings.getSelectedItem().toString();
            roamingUser = new User();
            try {
                RoamingCredentialReader reader = new RoamingCredentialReader(uname, entrustIniFile);
                CredentialWriter writer = reader.getRoamingCredentialWriter();
                roamingUser.setCredentialWriter(writer);
                roamingUser.setConnections(entrustIniFile);
                if (uname.length() > 0 && pword.length() > 0) {
                    try {
                        roamingUser.login(reader, new SecureStringBuffer(new StringBuffer(pword)));
                    } catch (com.entrust.toolkit.exceptions.UserFatalException re) {
                        JOptionPane.showMessageDialog(null, "Error logging in: check username/password", "Entrust Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error logging in: you must enter a username and password", "Entrust Login Error", JOptionPane.ERROR_MESSAGE);
                }
                boolean response = roamingUser.isLoggedIn();
                if (response == true) {
                    mUser = roamingUser;
                    mCurrentMeaning = meaning;
                    mComment = comment;
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
        return roamingUser;
    }
