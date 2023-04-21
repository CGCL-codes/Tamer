    private static PersistentByteMap addJar(File f, PersistentByteMap b, File soFile) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        JarFile jar = new JarFile(f);
        int count = 0;
        {
            Enumeration entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry classfile = (JarEntry) entries.nextElement();
                if (classfile.getName().endsWith(".class")) count++;
            }
        }
        if (verbose) System.err.println("adding " + count + " elements from " + f + " to " + b.getFile());
        b = resizeMap(b, (b.size() + count) * 2, true);
        Enumeration entries = jar.entries();
        byte[] soFileName = soFile.getCanonicalPath().getBytes("UTF-8");
        while (entries.hasMoreElements()) {
            JarEntry classfile = (JarEntry) entries.nextElement();
            if (classfile.getName().endsWith(".class")) {
                InputStream str = jar.getInputStream(classfile);
                int length = (int) classfile.getSize();
                if (length == -1) throw new EOFException();
                byte[] data = new byte[length];
                int pos = 0;
                while (length - pos > 0) {
                    int len = str.read(data, pos, length - pos);
                    if (len == -1) throw new EOFException("Not enough data reading from: " + classfile.getName());
                    pos += len;
                }
                b.put(md.digest(data), soFileName);
            }
        }
        return b;
    }
