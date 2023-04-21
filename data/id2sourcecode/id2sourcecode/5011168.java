    private void writeXML(Document xml, String name, ZipOutputStream zip, Writer writer) throws IOException {
        zip.putNextEntry(new ZipEntry(name));
        writer.write(XMLUtil.toString(xml));
        writer.flush();
        zip.closeEntry();
    }
