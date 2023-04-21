    public void joinChannel(String name) {
        if (null == eirc.getChannel(name)) {
            String p[] = { name };
            eirc.sendMessage("join", p);
        } else {
            eirc.showPanel(name);
        }
    }
