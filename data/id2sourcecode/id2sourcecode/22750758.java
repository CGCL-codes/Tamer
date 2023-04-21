    public void putNextEntry(ZipEntry ze) throws IOException {
        if (entries.containsKey(ze.getName())) {
            throw new ZipException("duplicate entry: " + ze.getName());
        }
        entries.put(ze.getName(), "");
        super.putNextEntry(ze);
    }
