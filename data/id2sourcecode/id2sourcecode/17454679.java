    void exportCoveritem(final XMLStreamWriter writer, final ZipOutputStream out, final CoveritemIf item, final Integer id) throws XMLStreamException, IOException {
        writer.writeStartElement(TypeConstants.XML_COVERITEM);
        writer.writeAttribute("id", id.toString());
        writer.writeAttribute("type", item.getCitype());
        String filename = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz0123456789");
        while (_usedFilenames.contains(filename)) {
            filename = RandomStringUtils.random(10, "abcdefghijklmnopqrstuvwxyz0123456789");
        }
        out.putNextEntry(new ZipEntry(filename));
        IOUtils.write(item.getCidata(), out);
        out.closeEntry();
        writer.writeAttribute("filename", filename);
        _usedFilenames.add(filename);
        writer.writeEndElement();
    }
