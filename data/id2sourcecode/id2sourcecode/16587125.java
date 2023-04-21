    private static Permission getReadPermission(URL url) {
        Permission p = null;
        try {
            URLConnection urlConnection = url.openConnection();
            p = urlConnection.getPermission();
        } catch (IOException e) {
            throw new RuntimeException("Unable to obtain the permission for " + url, e);
        }
        return new FilePermission(p.getName(), "read");
    }
