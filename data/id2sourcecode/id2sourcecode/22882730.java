    private static final void readStandardMappingTable(int idx) {
        String char_value = null, NAME, STD_value, MAC_value, WIN_value, PDF_value, raw;
        int mac_value = 0, win_value = 0, std_value = 0;
        String line = null;
        BufferedReader input_stream = null;
        try {
            glyphToChar[idx] = new Hashtable();
            input_stream = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/standard_encoding.cfg"), enc));
            if (input_stream == null) LogWriter.writeLog("Unable to open standard_encoding.cfg from jar");
            while (true) {
                line = input_stream.readLine();
                if (line == null) break;
                StringTokenizer values = new StringTokenizer(line);
                int count = values.countTokens();
                NAME = values.nextToken();
                STD_value = values.nextToken();
                MAC_value = values.nextToken();
                WIN_value = values.nextToken();
                PDF_value = values.nextToken();
                raw = values.nextToken();
                if (count == 7) {
                    char_value = Character.toString((char) Integer.parseInt(raw, 16));
                } else {
                    char_value = raw;
                }
                unicode_name_mapping_table.put(NAME, char_value);
                if ((idx == MAC) && (Character.isDigit(MAC_value.charAt(0)))) {
                    mac_value = Integer.parseInt(MAC_value, 8);
                    MAC_char_encoding_table[mac_value] = char_value;
                    unicode_char_decoding_table[MAC][mac_value] = NAME;
                    glyphToChar[MAC].put(NAME, new Integer(mac_value));
                } else if ((idx == STD) && (Character.isDigit(STD_value.charAt(0)))) {
                    std_value = Integer.parseInt(STD_value, 8);
                    STD_char_encoding_table[std_value] = char_value;
                    unicode_char_decoding_table[STD][std_value] = NAME;
                    glyphToChar[STD].put(NAME, new Integer(std_value));
                } else if ((idx == PDF) && (Character.isDigit(PDF_value.charAt(0)))) {
                    std_value = Integer.parseInt(PDF_value, 8);
                    PDF_char_encoding_table[std_value] = char_value;
                    unicode_char_decoding_table[PDF][std_value] = NAME;
                } else if ((idx == WIN) && (Character.isDigit(WIN_value.charAt(0)))) {
                    win_value = Integer.parseInt(WIN_value, 8);
                    WIN_char_encoding_table[win_value] = char_value;
                    unicode_char_decoding_table[WIN][win_value] = NAME;
                    glyphToChar[WIN].put(NAME, new Integer(win_value));
                }
            }
            if (idx == MAC) MAC_char_encoding_table[202] = " ";
            if (idx == WIN) {
                WIN_char_encoding_table[160] = " ";
                WIN_char_encoding_table[255] = "-";
                unicode_char_decoding_table[WIN][160] = "space";
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " reading lookup table for pdf  for " + idx);
        }
        if (input_stream != null) {
            try {
                input_stream.close();
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading lookup table for pdf  for abobe map");
            }
        }
    }
