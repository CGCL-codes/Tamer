    public static void writeDBFile(String[] columnNames, Object[] data, int nRecords, String fileNameRoot) {
        try {
            DbaseFileHeader header = new DbaseFileHeader();
            header.setNumRecords(nRecords);
            for (int i = 0; i < columnNames.length; i++) {
                Object array = data[i];
                String name = columnNames[i];
                if (array instanceof double[]) {
                    header.addColumn(name, 'N', 20, 4);
                } else if (array instanceof String[]) {
                    header.addColumn(name, 'C', findMaxStringLen((String[]) array), 0);
                } else if (array instanceof int[]) {
                    header.addColumn(name, 'N', 20, 0);
                } else {
                    logger.severe("hit unknown array type, " + array.getClass().getName());
                }
            }
            File dbf = new File(fileNameRoot + ".dbf");
            FileOutputStream dbfStream = new FileOutputStream(dbf);
            FileChannel dbfChan = dbfStream.getChannel();
            DbaseFileWriter writer = new DbaseFileWriter(header, dbfChan);
            Object[] record = new Object[columnNames.length];
            for (int i = 0; i < nRecords; i++) {
                for (int j = 0; j < record.length; j++) {
                    Object array = data[j];
                    if (array instanceof double[]) {
                        record[j] = new Double(((double[]) array)[i]);
                    } else if (array instanceof String[]) {
                        record[j] = ((String[]) array)[i];
                    } else if (array instanceof int[]) {
                        record[j] = new Integer(((int[]) array)[i]);
                    } else {
                        logger.severe("hit unknown array type, " + array.getClass().getName());
                    }
                }
                writer.write(record);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
