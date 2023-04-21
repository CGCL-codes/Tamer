    public static void addZipEntry(ZipOutputStream out, InputStream in, String name) {
        try {
            out.putNextEntry(new ZipEntry(name));
            copyToOutput(in, out);
            in.close();
            out.closeEntry();
        } catch (Exception ex) {
            Logger.getLogger(IOHelper.class).error("Failed to add ZIP entry", ex);
        }
    }
