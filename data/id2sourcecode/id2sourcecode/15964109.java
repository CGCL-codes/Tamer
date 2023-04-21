    public void createPermissionToTarget(Target groupOrUser, Directory target, boolean readAccess, boolean writeAccess) throws CreateGroupException {
        Permission p = new Permission();
        p.setReadable(readAccess);
        p.setWriteable(writeAccess);
        createPermissionToTarget(groupOrUser, target, p);
    }
