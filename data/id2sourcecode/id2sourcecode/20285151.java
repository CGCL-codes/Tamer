    public VAClassLoader(File jarfile, Long offset) {
        super();
        offset_ = offset.longValue();
        jarfile_ = jarfile;
        JarInputStream jar = null;
        try {
            cache_ = new Hashtable();
            printDebug("VAClassLoader: loading classes from " + jarfile.getName() + " (offset " + offset_ + ")...");
            FileInputStream stream = new FileInputStream(jarfile_);
            stream.skip(offset_);
            jar = new JarInputStream(stream);
            ZipEntry entry = jar.getNextEntry();
            byte[] data = null;
            while (entry != null) {
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    byte[] buffer = new byte[2048];
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    while (true) {
                        int read = jar.read(buffer);
                        if (read == -1) break;
                        bos.write(buffer, 0, read);
                    }
                    data = bos.toByteArray();
                    bos.close();
                    jar.closeEntry();
                    String className = entryName.replace('/', '.');
                    printDebug("  className=" + className + " size=" + data.length);
                    byte[] toCache = new byte[data.length];
                    System.arraycopy(data, 0, toCache, 0, data.length);
                    cache_.put(className, data);
                }
                entry = jar.getNextEntry();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            printDebug("  closing jarFile.");
            if (jar != null) try {
                jar.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
