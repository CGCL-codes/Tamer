    private static File copyToLocalFile(Object session, VFS vfs, String path) throws IOException {
        if (jEdit.getSettingsDirectory() == null) return null;
        String userDir = jEdit.getSettingsDirectory();
        File _resourceDir = new File(resourceDir);
        if (!_resourceDir.exists()) _resourceDir.mkdir();
        BufferedInputStream in = new BufferedInputStream(vfs._createInputStream(session, path, false, null));
        File localFile = File.createTempFile("cache", ".xml", _resourceDir);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
        byte[] buf = new byte[4096];
        int count = 0;
        while ((count = in.read(buf)) != -1) out.write(buf, 0, count);
        out.close();
        return localFile;
    }
