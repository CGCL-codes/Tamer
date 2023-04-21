    private void loadIndexFile(File file) throws IOException {
        Properties props = new Properties();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileLock lock = raf.getChannel().lock();
        try {
            FileInputStream in = new FileInputStream(raf.getFD());
            props.load(in);
            Enumeration<?> keys = (Enumeration<?>) props.propertyNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                String value = props.getProperty(key);
                String bundle = key.trim();
                String location = value.trim();
                File bundleDir;
                if (location.startsWith("/") || file.getParentFile() == null) bundleDir = new File(location); else bundleDir = new File(file.getParentFile(), location);
                if (bundleDir.exists()) bundleToLocation.put(bundle, bundleDir);
            }
        } finally {
            lock.release();
        }
    }
