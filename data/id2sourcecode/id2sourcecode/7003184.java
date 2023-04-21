    private Permissions getPermissions(boolean read, boolean write, boolean exec) {
        Permissions result = new PermissionsImpl();
        result.setRead(read);
        result.setWrite(write);
        result.setExecute(exec);
        return result;
    }
