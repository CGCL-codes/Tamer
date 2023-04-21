    public Permission getPermission() throws IOException {
        if (permission == null) {
            String decodedPath = ParseUtil.decode(url.getPath());
            if (File.separatorChar == '/') {
                permission = new FilePermission(decodedPath, "read,write");
            } else {
                permission = new FilePermission(decodedPath.replace('/', File.separatorChar), "read,write");
            }
        }
        return permission;
    }
