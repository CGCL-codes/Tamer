    private String createUserTooltip(final Object item) {
        String tooltip = "";
        final String currTarg = Controller.getInstance().getConnector().getServer().getCurrentTarget();
        if (currTarg.charAt(0) == '#') {
            final User user = Controller.getInstance().getUWatcher().getUser(item.toString());
            final Channel chan = Controller.getInstance().getCWatcher().getChan(currTarg);
            UserChannelPermission perm = user.getChannels().get(chan);
            String chanrole = "";
            switch(perm) {
                case OPERATOR:
                    chanrole = "Operator";
                    break;
                case VOICE:
                    chanrole = "Voiced";
                    break;
                default:
                    chanrole = "";
                    break;
            }
            tooltip = "<HTML>Nick: " + user.getNick() + "<br>" + "Name: " + user.getName() + "<br>" + "Host: " + user.getHost() + "<br>" + "Realname: " + user.getRealname() + "<br>" + "Channel Role: " + chanrole + "</HTML>";
        }
        return tooltip;
    }
