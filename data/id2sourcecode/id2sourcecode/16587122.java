    private PermissionCollection createAppPermissions(File appRoot) {
        PermissionCollection permissions = new Permissions();
        permissions.add(new FilePermission(appRoot.getAbsolutePath() + File.separatorChar + "-", "read"));
        addAllPermissions(buildPermissionsToReadAppFiles(appRoot), permissions);
        if (Boolean.valueOf(System.getProperty("--enable_all_permissions")).booleanValue()) {
            permissions.add(new AllPermission());
            return permissions;
        }
        permissions.add(new RuntimePermission("getClassLoader"));
        permissions.add(new RuntimePermission("setContextClassLoader"));
        permissions.add(new RuntimePermission("createClassLoader"));
        permissions.add(new RuntimePermission("getProtectionDomain"));
        permissions.add(new RuntimePermission("accessDeclaredMembers"));
        permissions.add(new ReflectPermission("suppressAccessChecks"));
        permissions.add(new LoggingPermission("control", ""));
        permissions.add(new RuntimePermission("getStackTrace"));
        permissions.add(new RuntimePermission("getenv.*"));
        permissions.add(new RuntimePermission("setIO"));
        permissions.add(new PropertyPermission("*", "read,write"));
        permissions.add(new RuntimePermission("loadLibrary.keychain"));
        permissions.add(new UnresolvedPermission("javax.jdo.spi.JDOPermission", "getMetadata", null, null));
        permissions.add(new UnresolvedPermission("javax.jdo.spi.JDOPermission", "setStateManager", null, null));
        permissions.add(new UnresolvedPermission("javax.jdo.spi.JDOPermission", "manageMetadata", null, null));
        permissions.add(new UnresolvedPermission("javax.jdo.spi.JDOPermission", "closePersistenceManagerFactory", null, null));
        permissions.add(new UnresolvedPermission("groovy.security.GroovyCodeSourcePermission", "*", null, null));
        permissions.add(new FilePermission(System.getProperty("user.dir") + File.separatorChar + "-", "read"));
        permissions.add(getJreReadPermission());
        for (File f : SdkInfo.getSharedLibFiles()) {
            permissions.add(new FilePermission(f.getAbsolutePath(), "read"));
        }
        permissions.setReadOnly();
        return permissions;
    }
