    public void updateModes(String s) {
        Channel ch = eirc.getChannel(s);
        if (ch != null) {
            User user = ch.get(eirc.getNick());
            boolean hop = eirc.canOverride();
            boolean op = eirc.canOverride();
            if (user != null) {
                hop |= (user.isHalfOp() || user.isOp());
                op |= user.isOp();
            }
            moder.setEnabled(hop);
            invit.setEnabled(hop);
            secret.setEnabled(op);
            nickchange.setEnabled(op);
            limit.setEnabled(op);
            key.setEnabled(hop);
            moder.setState(ch.isModerated());
            secret.setState(ch.isSecret());
            invit.setState(ch.isInvitOnly());
            nickchange.setState(!ch.canNick());
            key.setText(ch.getKey());
            limit.setText(ch.getLimit() < 0 ? "" : String.valueOf(ch.getLimit()));
        }
    }
