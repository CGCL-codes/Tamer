    public void save(String filename) throws IOException {
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(filename));
        for (Enumeration e = mounts(); e.hasMoreElements(); ) {
            String name = e.nextElement().toString();
            zout.putNextEntry(new ZipEntry(name));
            Object[] content = this.get(name);
            new ObjectOutputStream(zout).writeObject(content[INDEX_DATA]);
            new ObjectOutputStream(zout).writeObject(content[INDEX_SUBS]);
            zout.closeEntry();
        }
        zout.close();
    }
