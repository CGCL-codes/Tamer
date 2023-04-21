    private void saveHeader(RecordStore headerRS, final MessageHeader header) throws RecordStoreNotOpenException, RecordStoreException, IOException, Exception {
        numHeadersInDB = -1;
        if (DEBUG) {
            DebugConsole.println("MailDB.saveHeader - start");
        }
        ;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(byteStream);
        boolean update = header.DBStatus == MessageHeader.STORED;
        if (header.getOrgLocation() == 'X') {
            header.setOrgLocation(dbName.charAt(0));
        }
        outputStream.writeChar(header.getOrgLocation());
        outputStream.writeUTF(header.getFrom());
        outputStream.writeUTF(header.getRecipients());
        outputStream.writeUTF(header.getSubject());
        if (header.getBoundary() == null) {
            header.setBoundary(header.getMessageID());
        }
        outputStream.writeUTF(header.getBoundary());
        outputStream.writeUTF(header.getMessageID());
        outputStream.writeUTF(header.getIMAPFolder());
        outputStream.writeUTF(header.getAccountID());
        outputStream.writeByte(header.messageFormat);
        outputStream.writeByte(header.readStatus);
        outputStream.writeBoolean(header.flagged);
        header.DBStatus = MessageHeader.STORED;
        outputStream.writeByte(header.DBStatus);
        outputStream.writeByte(header.sendStatus);
        outputStream.writeInt(header.getSize());
        outputStream.writeLong(header.getTime());
        if (DEBUG) {
            DebugConsole.println("MailDB.saveHeader - header fields saved");
        }
        ;
        outputStream.writeUTF(saveNullable(header.getThreadingMessageID()));
        outputStream.writeUTF(saveNullable(header.getParentID()));
        Vector parentIDs = header.getParentIDs();
        int parentsCount = parentIDs.size();
        outputStream.writeInt(parentsCount);
        for (int i = 0; i < parentsCount; ++i) {
            outputStream.writeUTF(parentIDs.elementAt(i).toString());
        }
        if (DEBUG) {
            DebugConsole.println("MailDB.saveHeader - fields for threading saved");
        }
        ;
        byte size = header.getBodyPartCount();
        if (DEBUG) System.out.println("DEBUG - MailDB.saveHeader() - number of bodyparts " + size);
        outputStream.writeByte(size);
        for (byte j = 0; j < size; j++) {
            header.getBodyPart(j).saveBodyPart(outputStream);
        }
        if (DEBUG) {
            DebugConsole.println("MailDB.saveHeader - bodypart headers saved");
        }
        ;
        outputStream.flush();
        int oldID = header.getRecordID();
        header.setRecordID(headerRS.addRecord(byteStream.toByteArray(), 0, byteStream.size()));
        if (DEBUG) {
            DebugConsole.println("MailDB.saveHeader - the record added");
        }
        ;
        if (update) {
            try {
                if (DEBUG) {
                    DebugConsole.println("MailDB.saveHeader - the record deleted (update)");
                }
                ;
                headerRS.deleteRecord(oldID);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }
        }
        outputStream.close();
        byteStream.close();
        if (DEBUG) {
            DebugConsole.printlnPersistent("MailDB.saveHeader - end");
        }
        ;
    }
