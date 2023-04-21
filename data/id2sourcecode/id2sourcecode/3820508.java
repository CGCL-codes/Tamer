    public PdfHeightTable() {
        BufferedReader input_stream = null;
        String line = null;
        int pointer1, pointer2, value;
        int key1, key2;
        try {
            input_stream = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/font_heights.cfg"), enc));
            if (input_stream == null) {
                LogWriter.writeLog("Unable to open font_heights.cfg from jar");
            }
            while (true) {
                line = input_stream.readLine();
                if (line == null) break;
                pointer1 = line.indexOf("-");
                pointer2 = line.indexOf(":");
                key1 = Integer.parseInt(line.substring(0, pointer1).trim());
                key2 = Integer.parseInt(line.substring(pointer1 + 1, pointer2).trim());
                value = Integer.parseInt(line.substring(pointer2 + 1, line.length()).trim());
                default_height_lookup[key1][key2] = value;
            }
        } catch (Exception ee) {
            LogWriter.writeLog("Exception " + ee + " reading line from height table");
        }
        if (input_stream != null) {
            try {
                input_stream.close();
            } catch (Exception e) {
                LogWriter.writeLog("Exception " + e + " reading lookup table for pdf  for abobe map");
            }
        }
    }
