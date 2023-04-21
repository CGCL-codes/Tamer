    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(Context.RESTART_COMMAND)) {
            nke.stopNK();
            System.exit(177);
        }
        if (e.getActionCommand().equals(Context.EXIT_COMMAND)) {
            nke.stopNK();
            System.exit(0);
        }
        if (e.getActionCommand().equals(Context.MESSAGE)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            String text = (String) user.getCliProp(User.LAST_MSG);
            try {
                Object[] objs = discRend.parseText(text);
                String message = (String) objs[0];
                Map<String, SerFile> imgmap = (Map<String, SerFile>) objs[1];
                nke.sendPlainTextMessage(user, message, imgmap);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (e.getActionCommand().equals(Context.DOUBLE_CLICK)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            DiscussionPanel dsp = uiwind.getDiscussionPanelForUser(user);
            if (dsp == null) {
                if (uiwind.removed.get(user) == null) {
                    dsp = new DiscussionPanel(user, this, uiwind);
                    dsp.putClientProperty(Context.NK_TAB, new NKTab(uiwind, dsp));
                } else {
                    dsp = uiwind.removed.get(user);
                }
                uiwind.addDiscussionPanel(dsp);
            }
            uiwind.setVisible(true);
            uimain.setVisible(false);
            dsp.requestFocus();
        }
        if (e.getActionCommand().equals(Context.SIMPLE_CLICK)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            DiscussionPanel dsp = uiwind.getDiscussionPanelForUser(user);
            if (dsp != null) {
                uiwind.jtabs.setSelectedComponent(dsp);
                dsp.requestFocus();
            }
        }
        if (e.getActionCommand().equals(Context.PASSKEY_COMMAND)) {
            String passkey = JOptionPane.showInputDialog(uiwind, "Enter new passkey : ");
            if (passkey != null) {
                Encryption encryption = new Encryption(passkey);
                nke.setNewEncription(encryption);
            }
        }
        if (e.getActionCommand().equals(Context.CONFIGURATION_COMMAND)) {
            JConfigFrame jcf = new JConfigFrame(Context.xml);
        }
        if (e.getActionCommand().equals(Context.SAVE_COMMAND)) {
            DiscussionPanel dsp = uiwind.getSelectedDiscussionPanel();
            if (dsp != null) {
                TextSave ts = new TextSave(dsp.jedit.getText());
                ts.formatAsHTML();
                ts.chooseFile("/", ".html");
                ts.executeSave();
            }
        }
        if (e.getActionCommand().equals(Context.CHANGE_ENVIRONMENT)) {
            Object[] objs = (Object[]) e.getSource();
            User user = (User) objs[0];
            user = syncViewUser(user);
            Color[] colors = (Color[]) objs[1];
            nke.sendObject(user, colors);
        }
        if (e.getActionCommand().equals(Context.SEND_FILE)) {
            Object[] objs = (Object[]) e.getSource();
            User user = (User) objs[0];
            user = syncViewUser(user);
            File[] files = (File[]) objs[1];
            Executable callback = (Executable) objs[2];
            nke.sendFiles(user, files, callback, false);
        }
        if (e.getActionCommand().equals(Context.SEND_FILE_EMBEDDED)) {
            Object[] objsr = (Object[]) e.getSource();
            User user = (User) objsr[0];
            user = syncViewUser(user);
            File file = (File) objsr[1];
            Object[] objs = discRend.prepareImageToSendEmbedded(file);
            String message = (String) objs[0];
            Map<String, SerFile> imgmap = (Map<String, SerFile>) objs[1];
            nke.sendPlainTextMessage(user, message, imgmap);
            if (file != null) {
                file.delete();
            }
        }
        if (e.getActionCommand().equals(Context.GET_FILE)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            nke.sendMessage(user, "7*", Message.GET_FILE);
        }
        if (e.getActionCommand().equals(Context.OPEN_SAVED_COMMAND)) {
            TextSave ts = new TextSave("");
            ts.chooseFile("/", null);
            String text = ts.executeRestore();
            if (text != null) {
                new JTextFrame(text);
            }
        }
        if (e.getActionCommand().equals(Context.TRANSLUCENCY_COMMAND)) {
            final JSlider jsl = new JSlider();
            jsl.setValue(100);
            jsl.addChangeListener(new ChangeListener() {

                public void stateChanged(ChangeEvent e) {
                    AWTUtilities.setWindowOpacity(uiwind, (float) ((float) jsl.getValue() / (float) 100));
                }
            });
            JOptionPane.showMessageDialog(uiwind, jsl, "Select Translucency", JOptionPane.QUESTION_MESSAGE);
        }
        if (e.getActionCommand().equals(Context.LOOKFEEL_COMMAND)) {
            new LookAndFeel(uiwind, uimain);
        }
        if (e.getActionCommand().equals(Context.FONT_CHANGED)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            Font font = (Font) user.getCliProp(User.FONT_PROPERTY);
            nke.sendObject(user, font);
        }
        if (e.getActionCommand().equals(Context.BUZZ)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            nke.sendQuickMessage(user.getSa(), Message.BUZZ);
        }
        if (e.getActionCommand().equals(Context.REMOTE_HIDE)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            nke.sendQuickMessage(user.getSa(), Message.HIDE);
        }
        if (e.getActionCommand().equals(Context.REMOTEWIND_HIDE)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            nke.sendQuickMessage(user.getSa(), Message.HIDEWIND);
        }
        if (e.getActionCommand().equals(Context.REMOTE_ERASE)) {
            User user = (User) e.getSource();
            user = syncViewUser(user);
            nke.sendQuickMessage(user.getSa(), Message.REMOTE_ERASE);
        }
        if (e.getActionCommand().equals(Context.ONLINE_CHECK)) {
            Boolean active = (Boolean) e.getSource();
        }
        if (e.getActionCommand().equals(Context.REFRESH_LIST)) {
            nke.broadcastIAmAlive();
        }
        if (e.getActionCommand().equals(Context.SET_STATUS)) {
            User us = (User) e.getSource();
            Status status = (Status) us.getCliProp(User.STATUS_TO_SEND);
            Object[] usersobj = null;
            usersobj = nke.getOnline().toArray();
            for (Object ojs : usersobj) {
                User user = (User) ojs;
                user = syncViewUser(user);
                nke.sendObject(user, status);
            }
        }
        if (e.getActionCommand().equals(Context.HELP_COMMAND)) {
            TextSave ts = new TextSave("", "html/help.html");
            String text = ts.executeRestore();
            new JTextFrame(text);
        }
        if (e.getActionCommand().equals(Context.LIST_COMMAND)) {
            if (uimain != null) {
                uimain.setVisible(true);
            }
        }
    }
