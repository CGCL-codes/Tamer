    protected PermissionCollection getPermissions(CodeSource cs) {
        PermissionCollection perms = super.getPermissions(cs);
        perms.add(new java.net.SocketPermission("localhost:1024-", "listen"));
        perms.add(new RuntimePermission("stopThread"));
        perms.add(new PropertyPermission("java.version", "read"));
        perms.add(new PropertyPermission("java.vendor", "read"));
        perms.add(new PropertyPermission("java.vendor.url", "read"));
        perms.add(new PropertyPermission("java.class.version", "read"));
        perms.add(new PropertyPermission("os.name", "read"));
        perms.add(new PropertyPermission("os.version", "read"));
        perms.add(new PropertyPermission("os.acrch", "read"));
        perms.add(new PropertyPermission("file.separator", "read"));
        perms.add(new PropertyPermission("path.separator", "read"));
        perms.add(new PropertyPermission("line.separator", "read"));
        perms.add(new PropertyPermission("java.specification.version", "read"));
        perms.add(new PropertyPermission("java.specification.vendor", "read"));
        perms.add(new PropertyPermission("java.specification.name", "read"));
        perms.add(new PropertyPermission("java.vm.specification.version", "read"));
        perms.add(new PropertyPermission("java.vm.specification.vendor", "read"));
        perms.add(new PropertyPermission("java.vm.specification.name", "read"));
        perms.add(new PropertyPermission("java.vm.version", "read"));
        perms.add(new PropertyPermission("java.vm.vendor", "read"));
        perms.add(new PropertyPermission("java.vm.name", "read"));
        URL url = Boot.getCacheManager().convertFromCachedURL(cs.getLocation());
        try {
            if (url.getProtocol().equals("file")) {
                String path = new File(url.getFile()).getAbsolutePath();
                if (path.endsWith(File.separator)) {
                    path = path + "-";
                }
                perms.add(new FilePermission(path, "read"));
                File cacheF = Boot.getCacheManager().getCachedFileForURL(url);
                path = cacheF.getAbsolutePath();
                if (path.endsWith(File.separator)) {
                    path = path + "-";
                }
                perms.add(new FilePermission(path, "read"));
            } else {
                String host = url.getHost();
                if (host == null) {
                    host = "localhost";
                }
                File cacheF = Boot.getCacheManager().getCachedFileForURL(url);
                String path = cacheF.getAbsolutePath();
                if (path.endsWith("_root_")) {
                    path = path.substring(0, path.lastIndexOf(File.separator) + 1);
                }
                if (path.endsWith(File.separator)) {
                    path = path + "-";
                }
                perms.add(new FilePermission(path, "read, write, delete"));
                StringBuffer hostURL = new StringBuffer();
                hostURL.append(url.getProtocol());
                hostURL.append("://");
                hostURL.append(host);
                if (url.getPort() > -1) {
                    hostURL.append(":");
                    hostURL.append(url.getPort());
                }
                File hostCacheFile = Boot.getCacheManager().getCachedFileForURL(new URL(hostURL.toString()));
                path = hostCacheFile.getAbsolutePath();
                if (path.endsWith("_root_")) {
                    path = path.substring(0, path.lastIndexOf(File.separator) + 1);
                }
                if (path.endsWith(File.separator)) {
                    path = path + "-";
                }
                perms.add(new FilePermission(path, "read, write, delete"));
                perms.add(new SocketPermission(host, "connect,accept"));
            }
        } catch (Exception badURL) {
            logger.log(Level.SEVERE, badURL.getMessage(), badURL);
        }
        return perms;
    }
