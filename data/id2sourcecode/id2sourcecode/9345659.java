        private PermissionCollection createPermissions() {
            PermissionCollection perms = super.getPermissions(this.codeSource);
            URL url = this.codeSource.getLocation();
            Permission p;
            URLConnection urlConnection;
            try {
                urlConnection = url.openConnection();
                p = urlConnection.getPermission();
            } catch (IOException ex) {
                p = null;
                urlConnection = null;
            }
            if (p instanceof FilePermission) {
                String path = p.getName();
                if (path.endsWith(File.separator)) {
                    path += "-";
                    p = new FilePermission(path, "read");
                }
            } else if ((p == null) && (url.getProtocol().equals("file"))) {
                String path = url.getFile().replace('/', File.separatorChar);
                try {
                    path = URIs.decode(path, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    throw new AssertionError(ex);
                } catch (CharacterCodingException ex) {
                    throw new RuntimeException("unable to decode url: " + path, ex);
                } catch (URISyntaxException ex) {
                    throw new RuntimeException("malformed url: " + path, ex);
                }
                if (path.endsWith(File.separator)) path += "-";
                p = new FilePermission(path, "read");
            } else {
                URL locUrl = url;
                if (urlConnection instanceof JarURLConnection) locUrl = ((JarURLConnection) urlConnection).getJarFileURL();
                String host = locUrl.getHost();
                if (host == null) host = "localhost";
                p = new SocketPermission(host, "connect,accept");
            }
            if (p != null) {
                SecurityManager sm = System.getSecurityManager();
                if (sm != null) sm.checkPermission(p);
                perms.add(p);
            }
            return perms;
        }
