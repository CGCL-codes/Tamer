    private void findNick() {
        IRCWindowContainer container = SandIRCFrame.getInstance().getCurrentWindowContainer();
        if (container == null) {
            return;
        }
        IRCWindow window = (IRCWindow) container.getSelectedTab().getContentComponent();
        IRCDocument doc = window.getDocument();
        Channel chan = doc.getChannel();
        if (chan != null) {
            String data = field.getText();
            int postion = field.getCaretPosition();
            data = data.substring(0, postion);
            String[] tokens = data.split("\\s+");
            String completMe = tokens[tokens.length - 1];
            List<String> nicks = new ArrayList<String>(chan.getNicks());
            if (tmpNicks.containsKey(window)) {
                System.out.println("Found window");
                TabCompleteInfo info = tmpNicks.get(window);
                if ((info != null) && info.current.equals(completMe)) {
                    if ((new Date().getTime() - info.date.getTime()) < 1500) {
                        nicks = info.tmpNicks;
                        int diff = info.current.length() - info.orig.length();
                        try {
                            field.getDocument().remove(postion - diff, diff);
                            System.out.println("Set completeMe to " + info.orig);
                            completMe = info.orig;
                            field.setCaretPosition(postion - diff);
                            postion = postion - diff;
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("too old or no info");
                        tmpNicks.remove(window);
                    }
                }
            } else {
                System.out.println("Did not find window");
            }
            for (Iterator<String> it = nicks.iterator(); it.hasNext(); ) {
                String nick = it.next();
                if (nick.toLowerCase().startsWith(completMe.toLowerCase())) {
                    try {
                        System.out.println("trying to remove " + completMe + " for " + nick);
                        System.out.println(field.getText());
                        field.getDocument().remove(postion - completMe.length(), completMe.length());
                        field.getDocument().insertString(postion - completMe.length(), nick, null);
                        it.remove();
                        TabCompleteInfo info = new TabCompleteInfo();
                        info.current = nick;
                        info.orig = completMe;
                        info.date = new Date();
                        info.tmpNicks = nicks;
                        tmpNicks.put(window, info);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
            tmpNicks.remove(window);
        }
    }
