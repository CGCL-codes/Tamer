    public Metadata readMetadata(InputStream inStream) throws MetadataFileException {
        Metadata meta = null;
        CsvReader reader = null;
        try {
            reader = new CsvReader(inStream);
            columnHeaders = readColumnHeaders(reader);
            meta = new Metadata(columnHeaders);
            while (true) {
                String[] items = reader.getNextValidLine();
                if (items == null) {
                    break;
                }
                if (items.length > columnHeaders.length + 1) {
                    throw new MetadataFileException("Number of metadata items exceeds number of columns declared", reader.getLineNumber());
                }
                if (items.length < 1) {
                    throw new MetadataFileException("No columns could be retrieved, id missing", reader.getLineNumber());
                }
                if (items.length < columnHeaders.length + 1) {
                    String[] newItems = new String[columnHeaders.length + 1];
                    for (int i = 0; i < items.length; i++) {
                        newItems[i] = items[i];
                    }
                    for (int i = items.length; i < newItems.length; i++) {
                        newItems[i] = "";
                    }
                    items = newItems;
                }
                String id = items[0].trim();
                String[] values = new String[columnHeaders.length];
                for (int i = 0; i < columnHeaders.length; i++) {
                    values[i] = items[i + 1].trim();
                }
                meta.addSequenceMetadata(id, values);
            }
        } catch (MetadataFileException e) {
            throw e;
        } catch (Exception e) {
            int lineNum = (reader == null) ? -1 : reader.getLineNumber();
            throw new MetadataFileException("Error reading metadata file", e, lineNum);
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                int lineNum = (reader == null) ? -1 : reader.getLineNumber();
                throw new MetadataFileException("Error closing metadata file", e, lineNum);
            }
        }
        return meta;
    }
