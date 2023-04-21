    protected void addFile(String path, Document source, JarOutputStream jos) throws IOException {
        System.out.println("Add file " + path);
        if (source != null) {
            jos.putNextEntry(new ZipEntry(path));
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            try {
                xmlOutputter.output(source, jos);
            } finally {
                jos.closeEntry();
            }
        }
    }
