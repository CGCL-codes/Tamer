    private void updateBundleIndex(String bundleSymbolicName, String bundleVersion, boolean clean) throws IOException {
        Properties props = new Properties();
        RandomAccessFile raf = new RandomAccessFile(bundleIndex, "rw");
        FileLock lock = raf.getChannel().lock();
        try {
            FileInputStream in = new FileInputStream(raf.getFD());
            props.load(in);
            List<String> removableKeys = new ArrayList<String>();
            if (clean) {
                Enumeration<?> keys = (Enumeration<?>) props.propertyNames();
                while (keys.hasMoreElements()) {
                    String key = (String) keys.nextElement();
                    String value = props.getProperty(key);
                    if (value == null || value.trim().equals("") || key.trim().equals("")) {
                        removableKeys.add(key);
                    } else {
                        String location = value.trim();
                        File bundleDir;
                        if (location.startsWith("/") || bundleIndex.getParentFile() == null) bundleDir = new File(location); else bundleDir = new File(bundleIndex.getParentFile(), location);
                        if (!bundleDir.exists()) removableKeys.add(key);
                    }
                }
            }
            String path = bundleDir.getAbsolutePath();
            if (bundleIndex.getParentFile() != null) {
                String parentPath = bundleIndex.getParentFile().getAbsolutePath();
                if (path.startsWith(parentPath)) {
                    path = path.substring(parentPath.length() + 1);
                }
            }
            Enumeration<?> keys = (Enumeration<?>) props.propertyNames();
            while (keys.hasMoreElements()) {
                String key = ((String) keys.nextElement()).trim();
                String value = props.getProperty(key).trim();
                if (path.equals(value) && !key.equals(bundleSymbolicName + "@" + bundleVersion)) removableKeys.add(key);
            }
            for (String key : removableKeys) props.remove(key);
            boolean changed = (removableKeys.size() > 0);
            String oldVal = props.getProperty(bundleSymbolicName + "@" + bundleVersion);
            if (oldVal == null || !path.equals(oldVal)) {
                props.setProperty(bundleSymbolicName + "@" + bundleVersion, path);
                changed = true;
            }
            if (changed) {
                FileOutputStream out = new FileOutputStream(raf.getFD());
                raf.seek(0);
                raf.setLength(0);
                props.store(out, "Bundle index file");
                getLog().info("Updating bundle index " + bundleIndex.getAbsolutePath());
            }
        } finally {
            lock.release();
        }
    }
