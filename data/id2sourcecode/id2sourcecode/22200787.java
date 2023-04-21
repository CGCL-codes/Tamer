    private Object[] computeDigests(File f) throws Exception {
        this.f = f;
        FileInputStream fis = new FileInputStream(f);
        byte[] block;
        long index = 0;
        this.propertyChangeSupport.firePropertyChange("fileIndexingInProgress", "[ANts2k] " + f.getName(), new Integer(0));
        this.digests = new Object[(int) Math.ceil(f.length() / ((WarriorAnt.blockSizeInDownload * MultipleSourcesDownloadManager.blocksPerSource) * 1.0))];
        int counter = 0;
        while (fis.available() > 0) {
            MessageDigest md = MessageDigest.getInstance(hashName);
            if (fis.available() >= WarriorAnt.blockSizeInDownload * MultipleSourcesDownloadManager.blocksPerSource) block = new byte[(int) (WarriorAnt.blockSizeInDownload * MultipleSourcesDownloadManager.blocksPerSource)]; else block = new byte[fis.available()];
            index += fis.read(block);
            md.update(block);
            this.digests[counter++] = md.digest();
            int progress = (int) Math.floor(((index * 1.0) / f.length()) * 100);
            this.propertyChangeSupport.firePropertyChange("fileIndexingInProgress", "[ANts2k] " + f.getName(), new Integer(progress));
            Thread.currentThread().sleep(100);
        }
        return this.digests;
    }
