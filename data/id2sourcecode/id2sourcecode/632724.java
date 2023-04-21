    public void actionPerformed(ActionEvent ev) {
        Object comp = ev.getSource();
        if (comp instanceof MenuItem) {
            String command = ev.getActionCommand();
            if (command.indexOf("+b") >= 0 || command.indexOf("-b") >= 0) {
                String mask = item_under_menu;
                String addr = NickInfo.getInetAddr(item_under_menu);
                String user = NickInfo.getUser(item_under_menu);
                if (addr == null) {
                    if (user != null) mask = "*!" + user + "@*";
                } else {
                    mask = "*!*@" + addr;
                }
                Object o[] = { mask, channel.getTag() };
                MessageFormat mf = new MessageFormat(command);
                eirc.sendCommand(mf.format(o), this);
            } else {
                Object o[] = { item_under_menu, channel.getTag() };
                MessageFormat mf = new MessageFormat(command);
                if (command.indexOf("KICK") >= 0 || command.indexOf("KBAN") >= 0) eirc.openKick(command, channel.getTag(), item_under_menu, this); else if (command.indexOf("KILL") >= 0) eirc.openKill(command, item_under_menu, this); else if (command.indexOf("GHOST") >= 0) eirc.openGhostWin(command, item_under_menu, this); else eirc.sendCommand(mf.format(o), this);
            }
        } else if (comp.equals(topic)) {
            String p[] = { channel.getTag(), topic.getText() };
            eirc.sendMessage("TOPIC", p);
        } else if (comp.equals(nick_list)) {
            String item = ev.getActionCommand();
            eirc.openPrivate(item);
            eirc.showPanel(item);
        } else if (comp.equals(entry)) {
            String text = entry.getText();
            if (text.length() <= 0) return;
            if (text.charAt(0) == '/') {
                text = text.substring(1);
                if (text.trim().length() > 0) eirc.sendCommand(text, this);
            } else {
                if (text.length() > 450) text = text.substring(0, 449);
                if (color.getSelectedIndex() != 1) text = MircMessage.COLOR + String.valueOf(color.getSelectedIndex()) + " " + text;
                String[] p = { channel.getTag(), text };
                eirc.sendMessage("PRIVMSG", p);
                printMyPrivmsg(text, getChannel().get(getNick()));
            }
            entry.setText("");
        } else if (comp.equals(kill)) {
            Object[] selection = nick_list.getSelectedObjects();
            boolean selected = false;
            String s = "";
            for (int i = 0; i < selection.length; i++) {
                if (selected) s = s.concat(",");
                selected = true;
                s = s.concat(selection[i].toString());
            }
            if (selected) {
                if (comp.equals(kill)) eirc.sendCommand("IGNORE " + s, this);
            } else {
                printError(res.getString("eirc.select_nicks"));
            }
        } else if (comp.equals(close)) {
            String p[] = { channel.getTag() };
            eirc.sendMessage("PART", p);
            eirc.closeChannel(channel.getTag());
        }
    }
