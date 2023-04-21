    public synchronized File getFile(URL jarURL, String path) throws IOException {
        CacheEntry cacheEntry = getCachedJarFile(jarURL);
        File file = new File(cacheDir, cacheEntry.id + "/" + path);
        if (!file.exists()) {
            JarEntry entry = cacheEntry.jarFile.getJarEntry(path);
            if (entry == null) throw new FileNotFoundException("Jar entry '" + path + "' not found in file '" + jarURL.toString() + "'");
            if (!entry.isDirectory() && entry.getSize() == 0) {
                String dirPath = path + "/";
                JarEntry dirEntry = cacheEntry.jarFile.getJarEntry(dirPath);
                if (dirEntry != null) entry = dirEntry;
            }
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                long lastModified = entry.getTime();
                InputStream in = cacheEntry.jarFile.getInputStream(entry);
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                FileOutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                int no = 0;
                try {
                    while ((no = in.read(buffer)) != -1) out.write(buffer, 0, no);
                } finally {
                    in.close();
                    out.close();
                }
                file.setLastModified(lastModified);
            }
        }
        return file;
    }
