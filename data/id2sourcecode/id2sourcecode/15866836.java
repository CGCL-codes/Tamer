    static void flushFilePrefsImpl(File file, Properties prefs) throws IOException {
        BufferedWriter out = null;
        FileLock lock = null;
        try {
            FileOutputStream ostream = new FileOutputStream(file);
            out = new BufferedWriter(new OutputStreamWriter(ostream, "UTF-8"));
            FileChannel channel = ostream.getChannel();
            lock = channel.lock();
            out.write(HEADER);
            out.newLine();
            out.write(FILE_PREFS);
            out.newLine();
            if (prefs.size() == 0) {
                exportEntries(EMPTY_SARRAY, EMPTY_SARRAY, out);
            } else {
                String[] keys = prefs.keySet().toArray(new String[prefs.size()]);
                int length = keys.length;
                String[] values = new String[length];
                for (int i = 0; i < length; i++) {
                    values[i] = prefs.getProperty(keys[i]);
                }
                exportEntries(keys, values, out);
            }
            out.flush();
        } finally {
            releaseQuietly(lock);
            closeQuietly(out);
        }
    }
