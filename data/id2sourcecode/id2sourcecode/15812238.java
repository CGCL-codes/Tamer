    public synchronized void removeUser(int userId) {
        User quitter = this.usersById.get(userId);
        if (quitter != null) {
            quitter.getChannel().keyFor(this.userSelector).cancel();
            this.usersById.remove(userId);
            Util.echo("User quitting: '" + quitter.getUsername() + "'", this);
        }
    }
