    private static void loadAdobeMap() {
        BufferedReader input_stream = null;
        if (adobeMap == null) {
            try {
                adobeMap = new Hashtable();
                input_stream = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/glyphlist.cfg"), enc));
                if (input_stream == null) LogWriter.writeLog("Unable to open sglyphlist.cfg from jar");
                while (true) {
                    String line = input_stream.readLine();
                    if (line == null) break;
                    if ((!line.startsWith("#")) && (line.indexOf(";") != -1)) {
                        StringTokenizer vals = new StringTokenizer(line, ";");
                        String key = vals.nextToken();
                        String operand = vals.nextToken();
                        int space = operand.indexOf(" ");
                        if (space != -1) operand = operand.substring(0, space);
                        int opVal = Integer.parseInt(operand, 16);
                        adobeMap.put(key, new Integer(opVal));
                        unicode_name_mapping_table.put(key, Character.toString((char) opVal));
                    }
                }
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading lookup table for pdf  for abobe map");
                e.printStackTrace();
            }
        }
        if (input_stream != null) {
            try {
                input_stream.close();
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading lookup table for pdf  for abobe map");
            }
        }
    }
