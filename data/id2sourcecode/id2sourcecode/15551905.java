    public final TupleOperationStatus setValueByPrimaryKey(Object primaryKey, int col, byte[] data) throws TableException {
        if (col >= columnCount) {
            throw new TableException("setValue: Column index (" + col + ") out-of-bounds.");
        }
        byte[] rowBytes;
        try {
            if (primaryKey instanceof String) {
                rowBytes = dataProvider.getBytes(dbLocalTableDataUsingPrimaryKey, primaryKey.toString(), LockMode.READ_UNCOMMITTED);
            } else if (primaryKey instanceof Integer) {
                rowBytes = dataProvider.getBytes(dbLocalTableDataUsingPrimaryKey, (Integer) primaryKey, LockMode.READ_UNCOMMITTED);
            } else if (primaryKey instanceof Short) {
                rowBytes = dataProvider.getBytes(dbLocalTableDataUsingPrimaryKey, (Short) primaryKey, LockMode.READ_UNCOMMITTED);
            } else if (primaryKey instanceof Long) {
                rowBytes = dataProvider.getBytes(dbLocalTableDataUsingPrimaryKey, (Long) primaryKey, LockMode.READ_UNCOMMITTED);
            } else if (primaryKey instanceof Byte[]) {
                rowBytes = dataProvider.getBytes(dbLocalTableDataUsingPrimaryKey, (byte[]) primaryKey, LockMode.READ_UNCOMMITTED);
            } else {
                throw new TableException("setValue: invalid primary key");
            }
        } catch (Exception e) {
            throw new TableException("Exception: " + e.getMessage());
        }
        if (rowBytes == null) {
            rowBytes = initializeNewRow();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(rowBytes);
        DataInputStream buffer = new DataInputStream(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream rowOutputBuffer = new DataOutputStream(baos);
        int colIndex = 0;
        try {
            rowOutputBuffer.writeByte(buffer.readByte());
            rowOutputBuffer.writeByte(buffer.readByte());
            while (colIndex < columnCount) {
                colIndex = buffer.readUnsignedByte();
                rowOutputBuffer.writeByte(colIndex);
                int fieldLength = buffer.readInt();
                if (colIndex == col) {
                    rowOutputBuffer.writeInt(data.length);
                } else {
                    rowOutputBuffer.writeInt(fieldLength);
                }
                if (colIndex == col) {
                    if (fieldLength > 0) {
                        buffer.skip(fieldLength);
                    }
                    rowOutputBuffer.write(data);
                } else {
                    if (fieldLength > 0) {
                        byte[] fieldData = new byte[fieldLength];
                        buffer.read(fieldData, 0, fieldLength);
                        rowOutputBuffer.write(fieldData);
                    }
                }
                colIndex++;
            }
            rowOutputBuffer.flush();
            try {
                return setSingleRowData(primaryKey, baos.toByteArray());
            } catch (Exception e) {
                throw new TableException("DatabaseException: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new TableException("setValue[data]: Row data binary error: " + e.getMessage());
        }
    }
