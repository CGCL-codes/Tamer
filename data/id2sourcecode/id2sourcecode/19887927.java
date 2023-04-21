    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(signature);
        GregorianCalendar calendar = new GregorianCalendar();
        year = (byte) (calendar.get(1) - 1900);
        month = (byte) (calendar.get(2) + 1);
        day = (byte) calendar.get(5);
        dataOutput.writeByte(year);
        dataOutput.writeByte(month);
        dataOutput.writeByte(day);
        numberOfRecords = Utils.littleEndian(numberOfRecords);
        dataOutput.writeInt(numberOfRecords);
        headerLength = findHeaderLength();
        dataOutput.writeShort(Utils.littleEndian(headerLength));
        recordLength = findRecordLength();
        dataOutput.writeShort(Utils.littleEndian(recordLength));
        dataOutput.writeShort(Utils.littleEndian(reserv1));
        dataOutput.writeByte(incompleteTransaction);
        dataOutput.writeByte(encryptionFlag);
        dataOutput.writeInt(Utils.littleEndian(freeRecordThread));
        dataOutput.writeInt(Utils.littleEndian(reserv2));
        dataOutput.writeInt(Utils.littleEndian(reserv3));
        dataOutput.writeByte(mdxFlag);
        dataOutput.writeByte(languageDriver);
        dataOutput.writeShort(Utils.littleEndian(reserv4));
        for (int i = 0; i < fieldArray.length; i++) fieldArray[i].write(dataOutput);
        dataOutput.writeByte(terminator1);
    }
