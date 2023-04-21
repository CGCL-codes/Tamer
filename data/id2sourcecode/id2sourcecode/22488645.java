    static void addPermissionsForURLs(URL[] urls, PermissionCollection perms, boolean forLoader) {
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            try {
                URLConnection urlConnection = url.openConnection();
                Permission p = urlConnection.getPermission();
                if (p != null) {
                    if (p instanceof FilePermission) {
                        String path = p.getName();
                        int endIndex = path.lastIndexOf(File.separatorChar);
                        if (endIndex != -1) {
                            path = path.substring(0, endIndex + 1);
                            if (path.endsWith(File.separator)) {
                                path += "-";
                            }
                            Permission p2 = new FilePermission(path, "read");
                            if (!perms.implies(p2)) {
                                perms.add(p2);
                            }
                        } else {
                            if (!perms.implies(p)) {
                                perms.add(p);
                            }
                        }
                    } else {
                        if (!perms.implies(p)) {
                            perms.add(p);
                        }
                        if (forLoader) {
                            URL hostURL = url;
                            for (URLConnection conn = urlConnection; conn instanceof JarURLConnection; ) {
                                hostURL = ((JarURLConnection) conn).getJarFileURL();
                                conn = hostURL.openConnection();
                            }
                            String host = hostURL.getHost();
                            if (host != null && p.implies(new SocketPermission(host, "resolve"))) {
                                Permission p2 = new SocketPermission(host, "connect,accept");
                                if (!perms.implies(p2)) {
                                    perms.add(p2);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
            }
        }
    }
