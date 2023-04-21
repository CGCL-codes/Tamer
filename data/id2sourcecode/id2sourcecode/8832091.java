    public static Hashtable readCacheFile(File cachefile, long mtime, String canonicalJarfile) {
        Py.writeDebug("packageManager", "reading cache, '" + canonicalJarfile + "'");
        try {
            DataInputStream istream = new DataInputStream(new BufferedInputStream(new FileInputStream(cachefile)));
            String old_jarfile = istream.readUTF();
            long old_mtime = istream.readLong();
            if ((!old_jarfile.equals(canonicalJarfile)) || (old_mtime != mtime)) {
                Py.writeComment("packageManager", "invalid cache file: " + cachefile + ", " + canonicalJarfile + ":" + old_jarfile + ", " + mtime + ":" + old_mtime);
                cachefile.delete();
                return null;
            }
            Hashtable packs = new Hashtable();
            try {
                while (true) {
                    String packageName = istream.readUTF();
                    String classes = istream.readUTF();
                    packs.put(packageName, classes);
                }
            } catch (EOFException eof) {
                ;
            }
            istream.close();
            return packs;
        } catch (IOException ioe) {
            if (cachefile.exists()) cachefile.delete();
            return null;
        }
    }
