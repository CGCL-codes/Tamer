    public static Persistent read(File file) throws Exception {
        Persistent ret = (Persistent) cache.get(file.getAbsolutePath());
        if (ret != null) return ret;
        ret = (Persistent) xstream.fromXML(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
        ret.file = file;
        ret.path = file.getAbsolutePath();
        ret.lock = new RandomAccessFile(file, "rw").getChannel().tryLock();
        if (ret.lock == null) throw new RuntimeException("could not acquire lock on " + ret.path);
        cache.put(ret.path, ret);
        return ret;
    }
