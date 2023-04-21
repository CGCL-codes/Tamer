    private LogState readState(PublicKey pkey) {
        long start_us = now_us();
        boolean cached = false;
        LogState state = null;
        if (state_map.containsKey(pkey)) {
            state = state_map.get(pkey);
            cached = true;
        } else {
            String key_prefix = root_dir + "/" + printPkey(pkey);
            String key = key_prefix + ".ext";
            File f = new File(key);
            if (f.exists()) {
                RandomAccessFile file = null;
                try {
                    file = new RandomAccessFile(f, "rw");
                } catch (IOException e) {
                    BUG("readState: error opening file " + key + ". " + e);
                }
                FileChannel fchannel = file.getChannel();
                open_writeable_files.put(key, fchannel);
                state = readState(fchannel);
                assert state_map.put(pkey, state) == null;
            }
        }
        if (logger.isInfoEnabled()) logger.debug("readState: read state from " + (cached ? "cache " : "log ") + state + " in " + ((now_us() - start_us) / 1000.0) + "ms");
        return state;
    }
