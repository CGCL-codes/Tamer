    private int convertLogToXML(final File file) {
        final EntryIterator iter = logger.getEntries();
        int entryCount = 0;
        if (iter.hasNext()) {
            ZipOutputStream os = null;
            try {
                os = new ZipOutputStream(new FileOutputStream(file));
                os.setLevel(9);
                os.putNextEntry(new ZipEntry("logs.xml"));
                final XMLSerializer serializer = new XMLSerializer(os, OUTPUT_FORMAT);
                serializer.startDocument();
                serializer.setNamespaces(true);
                serializer.startPrefixMapping("", NAMESPACE_UNKNOWN_DEVICES);
                serializer.startElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_ENTRIES, ELEMENT_NAME_ENTRIES, EMPTY_ATTRIBUTES);
                while (iter.hasNext()) {
                    final Entry entry = (Entry) iter.next();
                    serializer.startElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_ENTRY, ELEMENT_NAME_ENTRY, EMPTY_ATTRIBUTES);
                    final String name = entry.getResolvedName();
                    serializer.startElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_RESOLVED_NAME, ELEMENT_NAME_RESOLVED_NAME, EMPTY_ATTRIBUTES);
                    serializer.characters(name.toCharArray(), 0, name.length());
                    serializer.endElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_RESOLVED_NAME, ELEMENT_NAME_RESOLVED_NAME);
                    final String type = entry.getDeviceType();
                    serializer.startElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_DEVICE_TYPE, ELEMENT_NAME_DEVICE_TYPE, EMPTY_ATTRIBUTES);
                    serializer.characters(type.toCharArray(), 0, type.length());
                    serializer.endElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_DEVICE_TYPE, ELEMENT_NAME_DEVICE_TYPE);
                    if (entry instanceof HeadersEntry) {
                        serializeHeadersEntry(serializer, (HeadersEntry) entry);
                    } else {
                        serializeSimpleEntry(serializer, entry);
                    }
                    serializer.endElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_ENTRY, ELEMENT_NAME_ENTRY);
                    entryCount++;
                }
                serializer.endElement(NAMESPACE_UNKNOWN_DEVICES, ELEMENT_NAME_ENTRIES, ELEMENT_NAME_ENTRIES);
                serializer.endPrefixMapping("");
                serializer.endDocument();
            } catch (Exception e) {
                throw new EmailNotifierException(EXCEPTION_LOCALIZER.format("cannot-convert-log-entries"), e);
            } finally {
                iter.close();
                if (os != null) {
                    try {
                        os.closeEntry();
                        os.close();
                    } catch (IOException e) {
                        throw new EmailNotifierException(EXCEPTION_LOCALIZER.format("cannot-close-output-stream"), e);
                    }
                }
            }
        } else {
            iter.close();
        }
        return entryCount;
    }
