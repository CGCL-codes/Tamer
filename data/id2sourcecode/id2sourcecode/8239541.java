    private static void writeXmlElement(FileHolder holder, String tagname, int dataMode, int metadataMode, edu.columbia.filesystem.impl.InputStream2D loader) throws IOException, FileSystemException {
        File xmlfile = holder.getFile();
        String contentType = holder.getContentType();
        String fname = xmlfile.getName();
        String basename = fname;
        if (fname.lastIndexOf(".") > 0) {
            basename = fname.substring(0, fname.lastIndexOf("."));
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        Utf8StreamWriter writer = Utf8StreamWriter.getThreadLocal().setOutputStream(baos);
        writer.write("<");
        writer.write(tagname);
        writer.write(" directory=\"");
        writer.write(xmlfile.getDirectory());
        writer.write("\" path=\"");
        writer.write(xmlfile.getPath());
        writer.write("\" filename=\"");
        writer.write(fname);
        String pattern = holder.getPattern();
        if (pattern != null) {
            writer.write("\" pattern=\"");
            writer.write(pattern);
        }
        writer.write("\" basename=\"");
        writer.write(basename);
        writer.write("\" type=\"");
        writer.write(contentType);
        writer.write("\">");
        writer.close();
        loader.addBytes(baos.toByteArray());
        if ((dataMode == WITH_DATA) && (contentType != null) && (contentType.startsWith("text") || (contentType.indexOf("xml") > 0))) {
            byte[] document = holder.getDataBytes();
            int i = 0;
            while (true) {
                if ((i + 1) >= document.length) {
                    break;
                }
                if (document[i] == '<') {
                    if ((document[i + 1] != '?') && (document[i + 1] != '!')) {
                        break;
                    }
                }
                i++;
            }
            loader.addBytes(document, i, document.length - i);
        }
        if (metadataMode == WITH_METADATA) {
            loader.addBytes(holder.getMetaBytes());
        }
        baos = new ByteArrayOutputStream(32);
        writer = Utf8StreamWriter.getThreadLocal().setOutputStream(baos);
        writer.write("</");
        writer.write(tagname);
        writer.write(">");
        writer.close();
        loader.addBytes(baos.toByteArray());
    }
