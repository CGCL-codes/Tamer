    private static PermissionCollection addURLsPerms(URL[] urls, PermissionCollection perms, boolean forACC) {
        for (int i = 0; i < urls.length; ++i) {
            Permission perm = null;
            try {
                perm = urls[i].openConnection().getPermission();
            } catch (IOException ioe) {
                continue;
            }
            if (perm == null) {
                continue;
            }
            if (perm instanceof FilePermission) {
                String str = perm.getName();
                int idx = str.lastIndexOf(File.separatorChar);
                if (!str.endsWith(File.separator)) {
                    perms.add(perm);
                } else {
                    perms.add(new FilePermission(str + "-", "read"));
                }
            } else {
                perms.add(perm);
                if (forACC) {
                    String host = urls[i].getHost();
                    if (host != null) {
                        perms.add(new SocketPermission(host, "connect, accept"));
                    }
                }
            }
        }
        return perms;
    }
