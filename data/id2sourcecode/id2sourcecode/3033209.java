    private void notifyGUI(final String target, final String msg) {
        final DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        final Calendar calender = Calendar.getInstance();
        final String time = df.format(calender.getTime());
        final Channel chan = Controller.getInstance().getCWatcher().create(target);
        Controller.getInstance().getCWatcher().getChan(target).getChat().append("[" + time + "] " + msg + "\n");
        if (chan.getName().equalsIgnoreCase(Controller.getInstance().getConnector().getServer().getCurrentTarget())) {
            Controller.getInstance().getClient().getChatPane().append("[" + time + "] " + msg + "\n");
        }
        if (chan.getName().charAt(0) != '#') {
            Controller.getInstance().getClient().getChannelList().setChannelData(new Vector<String>(Controller.getInstance().getCWatcher().chanList()));
        }
    }
