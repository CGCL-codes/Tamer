    private FileChannel createExtent(String key) {
        long start_us = -1L;
        if (logger.isDebugEnabled()) {
            logger.debug("createExtent: creating " + key);
            start_us = now_us();
        }
        File f = new File(key);
        assert !f.exists() : "createExtent: " + key + " alread exists";
        try {
            assert f.createNewFile() : "createExtent: could not create " + key;
        } catch (IOException e) {
            BUG("createExtent: createNEwFile ERROR " + e);
        }
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(f, "rw");
        } catch (IOException e) {
            BUG("createExtent: could not creat file " + e);
        }
        FileChannel fchannel = file.getChannel();
        assert open_writeable_files.put(key, fchannel) == null;
        if (logger.isDebugEnabled()) logger.debug("createExtent: created " + key + " in " + ((now_us() - start_us) / 1000.0) + "ms");
        return fchannel;
    }
