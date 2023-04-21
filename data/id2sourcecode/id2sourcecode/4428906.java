    public void connect() throws IOException {
        if (!connected) {
            URL url = getJarFileURL();
            String entryName = getEntryName();
            if (getUseCaches() || force_cache) {
                SoftReference ref = (SoftReference) cache.get(url);
                CacheEntry ce = ref != null ? (CacheEntry) ref.get() : null;
                if (ce == null) {
                    ce = new CacheEntry(createJarFile(url), url.openConnection());
                    synchronized (cache) {
                        cache.put(url, new SoftReference(ce));
                    }
                }
                jarFile = ce.getJarFile();
                jarFileURLConnection = ce.getConnection();
            } else {
                jarFile = createJarFile(url);
                jarFileURLConnection = url.openConnection();
            }
            if (entryName != null) {
                jarEntry = (JarEntry) jarFile.getEntry(entryName);
                if (jarEntry == null) {
                    throw new FileNotFoundException("Entry not found: '" + entryName + "' in " + jarFile.getName());
                }
            }
            connected = true;
        }
    }
