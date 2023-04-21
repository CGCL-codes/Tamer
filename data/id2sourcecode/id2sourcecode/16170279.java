    @Override
    public void userRoleAdded(IUser user) {
        if (user != null && user.getSystemRoles() != null) {
            for (ISystemRole systemRole : user.getSystemRoles()) {
                String channel = Channels.getAddSystemUserChannel(systemRole);
                ChannelCacheController.getChannelCache().remove(channel);
            }
        }
    }
