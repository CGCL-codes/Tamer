    public static String computeDigest(File target, MessageDigest md, ProgressObserver obs) throws IOException {
        md.reset();
        byte[] buffer = new byte[DIGEST_BUFFER_SIZE];
        int read;
        if (target.getPath().endsWith(".jar")) {
            JarFile jar = new JarFile(target);
            try {
                List<JarEntry> entries = Collections.list(jar.entries());
                Collections.sort(entries, ENTRY_COMP);
                int eidx = 0;
                for (JarEntry entry : entries) {
                    if (entry.getName().startsWith("META-INF")) {
                        updateProgress(obs, eidx, entries.size());
                        continue;
                    }
                    InputStream in = null;
                    try {
                        in = jar.getInputStream(entry);
                        while ((read = in.read(buffer)) != -1) {
                            md.update(buffer, 0, read);
                        }
                    } finally {
                        StreamUtil.close(in);
                    }
                    updateProgress(obs, eidx, entries.size());
                }
            } finally {
                try {
                    jar.close();
                } catch (IOException ioe) {
                    log.warning("Error closing jar [path=" + target + ", error=" + ioe + "].");
                }
            }
        } else {
            long totalSize = target.length(), position = 0L;
            FileInputStream fin = null;
            try {
                fin = new FileInputStream(target);
                while ((read = fin.read(buffer)) != -1) {
                    md.update(buffer, 0, read);
                    position += read;
                    updateProgress(obs, position, totalSize);
                }
            } finally {
                StreamUtil.close(fin);
            }
        }
        return StringUtil.hexlate(md.digest());
    }
