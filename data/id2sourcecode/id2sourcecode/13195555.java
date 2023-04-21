    public URLConnection openConnection(URL url) throws IOException {
        try {
            PrivilegeManager.enablePrivilege("UniversalConnectWithRedirect");
            if (isLocalURL(url)) PrivilegeManager.enablePrivilege("UniversalFileRead");
        } catch (ForbiddenTargetException e) {
            throw new IOException("connection forbidden");
        }
        return super.openConnection(url);
    }
