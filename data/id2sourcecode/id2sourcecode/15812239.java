    public boolean attemptReconnect(int userId, SocketChannel healthyChannel) {
        User targetUser = this.usersById.get(userId);
        if (targetUser != null) {
            if (targetUser.isLinkless()) {
                Util.echo("Reconnecting linkless user: '" + targetUser.getUsername() + "'.", this);
                targetUser.setChannel(healthyChannel);
                targetUser.send(uiAlert.display("You have been reconnected.") + "\n");
                targetUser.getCurrentLayer().display();
                this.userSelector.wakeup();
            } else {
                Util.echo("Outside channel attempting to hack user: '" + targetUser.getUsername() + "'. Disconnecting hacker...", this);
                Comm.writeToChannel(healthyChannel, "Someone is already logged in with these credentials. Disconnecting...\n");
                targetUser.send("Someone has just tried to log in with your username and password.\nYou should change your password to avoid being hacked.");
                try {
                    healthyChannel.close();
                } catch (IOException e) {
                    Comm.writeToChannel(healthyChannel, Comm.connectionError());
                    Util.echo("Could not close channel.", this);
                }
            }
            return true;
        }
        return false;
    }
