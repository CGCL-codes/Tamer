    private void readDBF() throws IOException {
        URL url = new URL(base, baseName + ".dbf");
        dbfStream = new DataInputStream(url.openStream());
        dbfStream.skipBytes(4);
        int numRecs = readInt(dbfStream);
        short headerSize = readShort(dbfStream);
        short recordSize = readShort(dbfStream);
        dbfStream.skipBytes(20);
        int numFields = (headerSize - 33) / 32;
        for (int i = 0; i < numFields; i++) {
            String fieldName = readString(dbfStream, 11);
            byte fieldType = dbfStream.readByte();
            dbfStream.skipBytes(4);
            byte fieldLength = dbfStream.readByte();
            dbfStream.skipBytes(15);
            ShapeFieldDescriptor field = new ShapeFieldDescriptor(fieldName, fieldType, fieldLength);
            descriptors.add(field);
        }
        dbfStream.skipBytes(2);
        byte records[] = new byte[recordSize * numRecs];
        dbfStream.read(records);
        for (int i = 0; i < numRecs; i++) {
            DataRecord node = readShapeRecord(descriptors, records, i * recordSize);
            shapeRecords.add(node);
        }
        dbfStream.close();
    }
