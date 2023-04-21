    public void serialize(XMLStreamWriter writer, boolean all, boolean end) throws XMLStreamException {
        writer.writeStartElement(mFile.isDirectory() ? "dir" : "file");
        writer.writeAttribute("name", getName());
        writer.writeAttribute("path", getPath());
        if (all) {
            writer.writeAttribute("length", String.valueOf(mFile.length()));
            writer.writeAttribute("type", mFile.isDirectory() ? "dir" : "file");
            writer.writeAttribute("readable", mFile.canRead() ? "true" : "false");
            writer.writeAttribute("writable", mFile.canWrite() ? "true" : "false");
            writer.writeAttribute("executable", mFile.canExecute() ? "true" : "false");
            writer.writeAttribute("mtime", Util.formatXSDateTime(mFile.lastModified()));
        }
        if (end) writer.writeEndElement();
    }
