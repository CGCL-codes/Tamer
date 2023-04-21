    public static OutputStream getOutputStream(String format, String location, String file, boolean append) throws FileNotFoundException, IOException, ZipException {
        OutputStream stream = null;
        if (format.equals(ZIP_FILE)) {
            try {
                System.out.println("WARNING: ZIP not yet fully supported.!");
                File path = new File(location);
                File parent = new File(path.getParent());
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(new URI(location)), append);
                stream = new ZipOutputStream(new BufferedOutputStream(fos));
                ZipEntry entry = new ZipEntry(file);
                ((ZipOutputStream) stream).putNextEntry(entry);
            } catch (URISyntaxException use) {
                use.printStackTrace();
                throw new ZipException("URISyntaxException: " + use.getMessage());
            }
        } else if (format.equals(DIRECTORY)) {
            if (location != null) {
                File path = new File(location + File.separator + file);
                File parent = new File(path.getParent());
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                stream = new FileOutputStream(location + File.separator + file);
            } else {
                File path = new File(file);
                File parent = new File(path.getParent());
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                stream = new FileOutputStream(file);
            }
        } else {
            throw new IOException("Format not supported for writing");
        }
        return stream;
    }
