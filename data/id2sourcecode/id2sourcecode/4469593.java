    public void playerDisconnected(ServerUser user) {
        UserInfo userInfo = user.getUserInfo();
        if (userInfo != null) {
            System.out.println("Server::playerDisconnected: " + userInfo.getName());
        }
        Channel channel = user.getChannel();
        if (channel != null) {
            channel.playerLeft(user, "nowhere (Disconnected)");
        }
        this.users.remove(user);
        this.usersNew.remove(user);
        if (userInfo != null) {
            this.userNames.remove(userInfo.getName());
            this.usersByUserId.remove(userInfo.getUserId());
        }
    }
