    private final byte[] ascii85DecodeOLD(byte[] data) {
        int special_cases = 0;
        String line = "";
        CharArrayWriter rawValuesRead = new CharArrayWriter(data.length);
        BufferedReader mappingStream = null;
        ByteArrayInputStream bis = null;
        try {
            bis = new ByteArrayInputStream(data);
            mappingStream = new BufferedReader(new InputStreamReader(bis));
            if (mappingStream != null) {
                while (true) {
                    line = mappingStream.readLine();
                    if (line == null) break;
                    rawValuesRead.write(line);
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " reading ASCII stream ");
        }
        char[] valuesRead = rawValuesRead.toCharArray();
        if (mappingStream != null) {
            try {
                mappingStream.close();
                bis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        int data_size = valuesRead.length;
        for (int i = 0; i < data_size; i++) {
            if (valuesRead[i] == 122) special_cases++;
        }
        int output_pointer = 0;
        long value = 0;
        byte[] temp_data = new byte[data_size + 1 + (special_cases * 3)];
        int ii = 0;
        for (int i = 0; i < data_size; i++) {
            value = 0;
            int next = valuesRead[i];
            while ((next == 10) || (next == 13)) {
                i++;
                next = valuesRead[i];
            }
            if (next == 122) {
                for (int i3 = 0; i3 < 4; i3++) {
                    temp_data[output_pointer] = 0;
                    output_pointer++;
                }
            } else if ((data_size - i > 4) && (next > 32) && (next < 118)) {
                for (ii = 0; ii < 5; ii++) {
                    next = valuesRead[i];
                    while ((next == 10) || (next == 13)) {
                        i++;
                        next = valuesRead[i];
                    }
                    i++;
                    value = value + ((next - 33) * base_85_indices[ii]);
                }
                for (int i3 = 0; i3 < 4; i3++) {
                    temp_data[output_pointer] = (byte) ((value / hex_indices[i3]) & 255);
                    output_pointer++;
                }
                i--;
            }
        }
        byte[] processed_data = new byte[output_pointer];
        System.arraycopy(temp_data, 0, processed_data, 0, output_pointer);
        return processed_data;
    }
