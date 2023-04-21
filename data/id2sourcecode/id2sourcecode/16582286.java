    public void transform(ZipEntry entry, String name, InputStream is, ZipOutputStream zos) throws IOException {
        ZipEntry entry2 = new ZipEntry(entry.getName());
        entry2.setComment(entry.getComment());
        entry2.setExtra(entry.getExtra());
        entry2.setTime(entry.getTime());
        zos.putNextEntry(entry2);
        if (entry.isDirectory()) return;
        try {
            this.delegate.transform(name, is, zos);
        } catch (IOException ioe) {
            IOException ioe2 = new IOException(name + ": " + ioe.getLocalizedMessage());
            ioe2.initCause(ioe);
            this.exceptionHandler.handle(ioe2);
        } catch (RuntimeException re) {
            this.exceptionHandler.handle(new RuntimeException(name + ": " + re.getLocalizedMessage(), re));
        }
    }
