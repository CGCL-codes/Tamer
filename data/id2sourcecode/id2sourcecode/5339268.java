    @Override
    public void receiveInput(String input) {
        String[] tokens = input.split("\\s+");
        if (stratMap.containsKey(tokens[0])) {
            stratMap.get(tokens[0]).run(input);
        } else {
            if (tokens[0].startsWith("/")) {
                WindowUtilites.getBaseWindow().insertDefault("Unreconized Command: " + tokens[0]);
            } else {
                JTabbedPane pane = BaseWindow.getInstance().pane;
                Window window = (Window) pane.getSelectedComponent();
                if (!window.equals(WindowUtilites.getBaseWindow())) {
                    Channel chan = window.getDocument().getChannel();
                    if (chan != null) {
                        chan.say(input);
                        window.insertMsg(window.getDocument().getSession().getNick(), input);
                    } else if (window.getDocument().getNick() != null) {
                        String ourNick = window.getDocument().getSession().getNick();
                        window.insertMsg(ourNick, input);
                        window.getDocument().getSession().sayPrivate(window.getDocument().getNick(), input);
                    }
                }
            }
        }
    }
