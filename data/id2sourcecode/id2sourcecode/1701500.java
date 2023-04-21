    private final byte[] asciiHexDecode(byte[] data) throws IOException {
        String line = "";
        StringBuffer value = new StringBuffer();
        StringBuffer valuesRead = new StringBuffer();
        BufferedReader mappingStream = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(data);
            mappingStream = new BufferedReader(new InputStreamReader(bis));
            if (mappingStream != null) {
                while (true) {
                    line = mappingStream.readLine();
                    if (line == null) break;
                    valuesRead.append(line);
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " reading ASCII stream ");
        }
        if (mappingStream != null) {
            try {
                mappingStream.close();
                bis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        int data_size = valuesRead.length();
        int i = 0, count = 0;
        char current = ' ';
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        while (true) {
            current = valuesRead.charAt(i);
            if (((current >= '0') & (current <= '9')) | ((current >= 'a') & (current <= 'f')) | ((current >= 'A') & (current <= 'F'))) {
                value.append(current);
                if (count == 1) {
                    bos.write(Integer.valueOf(value.toString(), 16).intValue());
                    count = 0;
                    value = new StringBuffer();
                } else count++;
            }
            if (current == '>') break;
            i++;
            if (i == data_size) break;
        }
        if (count == 1) {
            value.append('0');
            bos.write(Integer.valueOf(value.toString(), 16).intValue());
        }
        bos.close();
        return bos.toByteArray();
    }
