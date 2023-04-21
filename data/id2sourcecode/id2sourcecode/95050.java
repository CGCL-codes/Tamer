    public void init(Context application) {
        Group admins = new Group("admins");
        admins.addPermission("read").addPermission("write").addPermission("delete");
        AuthorizationManager.addGroup(admins);
        Group users = new Group("users");
        users.addPermission("read");
        AuthorizationManager.addGroup(users);
    }
