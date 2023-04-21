    private static ProtectionDomain createProtectionDomain(boolean isAllowNet, boolean isAllowFile) {
        CodeSource codeSrc = null;
        PermissionCollection permissionCollection = new Permissions();
        if (isAllowNet) {
            permissionCollection.add(new java.net.SocketPermission("*:1-", "accept,connect,listen,resolve"));
        }
        if (isAllowFile) {
            permissionCollection.add(new java.io.FilePermission("<<ALL FILES>>", "read,write,delete"));
        }
        ProtectionDomain protectionDomain = new ProtectionDomain(codeSrc, permissionCollection);
        return protectionDomain;
    }
