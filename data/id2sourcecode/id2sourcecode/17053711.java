    public void addPermission(String path) {
        if (true) {
            super.addPermission(path);
            return;
        }
        if (path == null) return;
        if (securityManager != null) {
            Permission permission = null;
            if (path.startsWith("jndi:") || path.startsWith("jar:jndi:")) {
                super.addPermission(path);
            } else {
                if (!path.endsWith(File.separator)) {
                    permission = new FilePermission(path, "read,write");
                    addPermission(permission);
                    path = path + File.separator;
                }
                permission = new FilePermission(path + "-", "read,write");
                addPermission(permission);
            }
        }
    }
