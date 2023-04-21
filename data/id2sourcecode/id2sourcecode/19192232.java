    public static void write(String filename, Scribable object) throws IOException {
        ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
        zip.putNextEntry(new ZipEntry("object"));
        ScribeOutputStream out = new ScribeOutputStream(zip);
        try {
            out.writeScribable(object);
        } catch (UnscribableNodeEncountered e) {
            throw new IOException(e.getMessage());
        }
        out.flush();
        out.close();
    }
