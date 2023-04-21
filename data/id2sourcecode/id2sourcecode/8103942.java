    public void doPrivmsg(final String target, final String msg) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        Calendar calender = Calendar.getInstance();
        String time = df.format(calender.getTime());
        Channel channel = Controller.getInstance().getCWatcher().create(target);
        ActiveChannel.active(target);
        String myMsg = "[" + time + "] <" + Controller.getInstance().getConnector().getMyUser().getNick() + "> " + msg.substring(1) + "\n";
        this.sendToServer("PRIVMSG " + target + " " + msg);
        channel.getChat().append(myMsg);
        Controller.getInstance().getClient().getChatPane().append(myMsg);
        if (channel.getName().charAt(0) != '#') {
            Controller.getInstance().getClient().getChannelList().setChannelData(new Vector<String>(Controller.getInstance().getCWatcher().chanList()));
        }
    }
