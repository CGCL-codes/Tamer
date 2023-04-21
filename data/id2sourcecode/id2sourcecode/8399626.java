    public ExSyncDocument(String filename) throws SAXException {
        this();
        writeMessage("Trying to read exSync document " + filename);
        try {
            org.exmaralda.partitureditor.exSync.sax.ExSyncDocumentSaxReader reader = new org.exmaralda.partitureditor.exSync.sax.ExSyncDocumentSaxReader();
            ExSyncDocument d = reader.readFromFile(filename);
            writeMessage("Document read.");
            for (int pos = 0; pos < d.size(); pos++) {
                this.addTrack(d.getTrackAt(pos));
                writeMessage("Track " + d.getTrackAt(pos).name + " added.");
            }
        } catch (SAXException se) {
            writeMessage("Document could not be read. ");
            writeMessage("Error message : " + se.getMessage());
            throw se;
        }
    }
