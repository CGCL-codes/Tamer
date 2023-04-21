    public String getDigest(File f) throws Exception {
        this.f = f;
        MessageDigest md = MessageDigest.getInstance(hashName);
        FileInputStream fis = new FileInputStream(f);
        byte[] block;
        while (fis.available() > 0) {
            if (fis.available() >= WarriorAnt.blockSizeInDownload * MultipleSourcesDownloadManager.blocksPerSource) block = new byte[(int) (WarriorAnt.blockSizeInDownload * MultipleSourcesDownloadManager.blocksPerSource)]; else block = new byte[fis.available()];
            fis.read(block);
            md.update(block);
            Thread.currentThread().sleep(100);
        }
        return Base16.toHexString(md.digest());
    }
