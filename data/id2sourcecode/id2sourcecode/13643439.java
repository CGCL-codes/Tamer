    public Resource createNew(String newName, InputStream in, Long length, String contentType) throws IOException {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        if (!dotDavHelper.isTempResource(newName)) {
            dotDavHelper.createResource(path + newName, isAutoPub, user);
            dotDavHelper.setResourceContent(path + newName, in, contentType, null, java.util.Calendar.getInstance().getTime());
            com.dotmarketing.portlets.files.model.File f = dotDavHelper.loadFile(path + newName);
            FileResourceImpl fr = new FileResourceImpl(f, f.getFileName());
            return fr;
        }
        String p = folder.getPath();
        if (!p.endsWith("/")) {
            p = p + "/";
        }
        Host host = HostFactory.getHost(folder.getHostInode());
        File f = dotDavHelper.createTempFile("/" + host.getHostname() + p + newName);
        FileOutputStream fos = new FileOutputStream(f);
        byte[] buf = new byte[256];
        int read = -1;
        while ((read = in.read()) != -1) {
            fos.write(read);
        }
        TempFileResourceImpl tr = new TempFileResourceImpl(f, path + newName, isAutoPub);
        return tr;
    }
