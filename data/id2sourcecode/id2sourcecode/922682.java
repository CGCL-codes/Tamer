    public void addQueue(String c) {
        for (int n = 0; n < channels.length; n++) {
            if (channels[n].equals(c.toLowerCase())) {
                queon[n] = true;
                String[] users = dbc.getChannelUsers(c);
                if (!users[0].equals("0")) {
                    for (String u : users) {
                        addUser(u, c);
                    }
                }
                return;
            }
        }
    }
