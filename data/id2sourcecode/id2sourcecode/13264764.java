    private void updateLocalUser() {
        int hint = HINT_NONE;
        Integer id = getStore().getID();
        PasswordAuthentication auth = getStore().getAuth();
        ChannelBean channel = getStore().getChannel();
        Role role = null;
        if (auth == null) hint = HINT_NONE; else if (channel == null) hint = HINT_LOGIN; else hint = HINT_CHANNEL;
        Image image = null;
        if (channel != null) role = channel.getRole();
        image = Util.getRoleImage(role);
        meLabel.setImage(image);
        String toolTipText;
        miReady.setEnabled(hint == HINT_CHANNEL);
        miBeRightBack.setEnabled(hint == HINT_CHANNEL);
        switch(hint) {
            case HINT_LOGIN:
                toolTipText = String.format(Strings.classmembers_tip_text_login, id, auth.getUserName());
                break;
            case HINT_CHANNEL:
                toolTipText = String.format(Strings.classmembers_tip_text_in_channel, id, auth.getUserName(), role);
                break;
            default:
                toolTipText = Strings.classmembers_tip_text_dc;
                break;
        }
        meLabel.setToolTipText(toolTipText);
        meBar.pack();
    }
