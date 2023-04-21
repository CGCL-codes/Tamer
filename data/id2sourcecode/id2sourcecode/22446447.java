    public boolean transferServerJar(String symbolicName, URL url) throws Exception {
        File f = new File(url.getFile());
        if (!f.exists()) return false;
        byte[] data = new byte[(int) f.length()];
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(url.openStream());
            bis.read(data);
            SafiServerRemoteManager.getInstance().transfer(f.getName(), symbolicName, data);
            return true;
        } finally {
            if (bis != null) try {
                bis.close();
            } catch (Exception e) {
            }
        }
    }
