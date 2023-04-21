    public void transform(ZipEntry entry, String name, InputStream is, ZipOutputStream zos) throws IOException {
        ZipEntry entry2 = new ZipEntry(entry.getName());
        entry2.setComment(entry.getComment());
        entry2.setExtra(entry.getExtra());
        entry2.setTime(entry.getTime());
        zos.putNextEntry(entry2);
        if (entry.isDirectory()) return;
        this.delegate.transform(name, is, zos);
    }
