    public void write(Element element) throws IOException {
        out.putNextEntry(new ZipEntry("e" + counter++));
        super.write(element);
        out.closeEntry();
        out.flush();
    }
