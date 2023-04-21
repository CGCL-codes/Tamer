    public Channel getChannel(int i) {
        String name = (String) channels.get(i);
        if (name.charAt(0) == '@') {
            name = name.substring(1);
        } else if (name.charAt(0) == '+') {
            name = name.substring(1);
        }
        return connection.resolveChannel(name);
    }
