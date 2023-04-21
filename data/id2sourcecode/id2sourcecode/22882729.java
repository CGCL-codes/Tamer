    private static final void readStandardMappingTable(int key, String file_name) {
        String char_value, NAME, VAL, line = null, hexVal = null;
        int value = 0;
        BufferedReader input_stream = null;
        glyphToChar[key] = new Hashtable();
        try {
            input_stream = (file_name.equals("symbol.cfg")) ? new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/" + file_name), enc)) : new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/" + file_name), "UTF-16"));
            if (input_stream == null) {
                LogWriter.writeLog("Unable to open " + file_name + " to read standard encoding");
            }
            while (true) {
                line = input_stream.readLine();
                if (line == null) break;
                StringTokenizer values = new StringTokenizer(line);
                if ((line.indexOf("space") == -1) && (values.countTokens() > 1)) {
                    if (values.countTokens() == 3) {
                        char_value = values.nextToken();
                        NAME = values.nextToken();
                        VAL = values.nextToken();
                    } else if (values.countTokens() == 4) {
                        hexVal = values.nextToken();
                        char_value = values.nextToken();
                        NAME = values.nextToken();
                        VAL = values.nextToken();
                        char_value = Character.toString((char) Integer.parseInt(hexVal, 16));
                    } else {
                        if (values.countTokens() == 2) {
                            char_value = " ";
                            NAME = values.nextToken();
                            VAL = values.nextToken();
                        } else {
                            char_value = values.nextToken();
                            NAME = values.nextToken();
                            VAL = values.nextToken();
                        }
                    }
                    unicode_name_mapping_table.put(key + NAME, char_value);
                    glyphToChar[key].put(NAME, new Integer(Integer.parseInt(VAL)));
                    unicode_name_mapping_table.put(NAME, char_value);
                    if (Character.isDigit(VAL.charAt(0))) {
                        value = Integer.parseInt(VAL, 8);
                        if (key == ZAPF) ZAPF_char_encoding_table[value] = char_value; else if (key == SYMBOL) SYMBOL_char_encoding_table[value] = char_value; else if (key == MACEXPERT) MACEXPERT_char_encoding_table[value] = char_value;
                        unicode_char_decoding_table[key][value] = NAME;
                    }
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("Exception " + e + " reading lookup table for pdf");
        }
        if (input_stream != null) {
            try {
                input_stream.close();
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading lookup table for pdf  for abobe map");
            }
        }
    }
