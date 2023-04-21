    @Override
    public void checkPermission(Permission perm) {
        if (perm.equals(new RuntimePermission("createSecurityManager")) || perm.equals(new RuntimePermission("createClassLoader")) || perm.equals(new FilePermission(deletedFile, "delete")) || perm.equals(new FilePermission(readedFile, "read")) || perm.equals(new PropertyPermission("*", "read,write")) || perm.equals(new PropertyPermission("key", "read")) || perm.equals(new SecurityPermission("getPolicy")) || perm.equals(new FilePermission(writedFile, "write"))) {
            throw new SecurityException("Unable to create Security Manager");
        }
    }
