    public boolean readDiskFromURL(URL url) {
        try {
            System.out.println("Loading from " + url);
            InputStream reader = url.openConnection().getInputStream();
            return readDisk(reader);
        } catch (Exception e) {
            System.out.println("Error when opening url " + url + "  " + e);
        }
        return false;
    }
