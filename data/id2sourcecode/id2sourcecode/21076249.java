    private void writeManifest(ZipOutputStream zipOut, boolean includeTaskLists) throws IOException {
        zipOut.putNextEntry(new ZipEntry(MANIFEST_FILE_NAME));
        XmlSerializer xml = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            xml = factory.newSerializer();
        } catch (XmlPullParserException xppe) {
            throw new RuntimeException("Couldn't obtain xml serializer", xppe);
        }
        xml.setOutput(zipOut, ENCODING);
        xml.startDocument(ENCODING, Boolean.TRUE);
        xml.ignorableWhitespace(NEWLINE + NEWLINE);
        xml.startTag(null, ARCHIVE_ELEM);
        xml.attribute(null, TYPE_ATTR, FILE_TYPE_ARCHIVE);
        xml.ignorableWhitespace(NEWLINE);
        writeManifestMetaData(xml);
        writeManifestFileEntry(xml, DATA_FILE_NAME, FILE_TYPE_METRICS, "1");
        writeManifestFileEntry(xml, DEFECT_FILE_NAME, FILE_TYPE_DEFECTS, "1");
        writeManifestFileEntry(xml, TIME_FILE_NAME, FILE_TYPE_TIME_LOG, "1");
        if (includeTaskLists) writeManifestFileEntry(xml, EV_FILE_NAME, FILE_TYPE_EARNED_VALUE, "1");
        if (additionalEntries != null) for (Iterator i = additionalEntries.iterator(); i.hasNext(); ) {
            ExportFileEntry file = (ExportFileEntry) i.next();
            writeManifestFileEntry(xml, file.getFilename(), file.getType(), file.getVersion());
        }
        xml.endTag(null, ARCHIVE_ELEM);
        xml.ignorableWhitespace(NEWLINE);
        xml.endDocument();
        zipOut.closeEntry();
    }
