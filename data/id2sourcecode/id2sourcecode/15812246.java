    private void registerUsers() {
        Iterator<User> uIterator = this.usersById.values().iterator();
        while (uIterator.hasNext()) {
            User thisUser = uIterator.next();
            try {
                if (!this.userSelector.keys().contains(thisUser.getChannel().keyFor(this.userSelector)) && !thisUser.isLinkless()) {
                    thisUser.getChannel().register(this.userSelector, this.selectionInterest, thisUser);
                    Util.echo(thisUser.getUsername() + " registered with selector.", this);
                }
            } catch (ClosedChannelException e) {
                Util.echo("Cannot register a closed channel. Moving to next user.", this);
                continue;
            }
        }
    }
