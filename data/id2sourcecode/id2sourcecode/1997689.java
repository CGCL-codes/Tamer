    protected User addUser(final String nick, final String name, final String realname, final String host, final Channel chan, final UserChannelPermission chanRole) {
        final User selectedUser = getUser(nick);
        User theUser;
        if (selectedUser == null) {
            theUser = new User(nick, name, realname, host, chan, chanRole);
            this.getUserList().add(theUser);
        } else {
            theUser = selectedUser;
            if (StringUtils.isNotBlank(name)) theUser.setName(name);
            if (StringUtils.isNotBlank(realname)) theUser.setRealname(realname);
            if (StringUtils.isNotBlank(host)) theUser.setHost(host);
            if (chan != null && !theUser.getChannels().containsKey(chan)) {
                theUser.getChannels().put(chan, chanRole);
            } else if (chan != null && theUser.getChannels().containsKey(chan) && theUser.getChannels().get(chan).equals(UserChannelPermission.UNKNOWN)) {
                theUser.getChannels().put(chan, chanRole);
            } else if (chan != null && chanRole != UserChannelPermission.UNKNOWN) {
                theUser.getChannels().put(chan, chanRole);
            }
        }
        return theUser;
    }
