    private static final void loadStandardFont(int i) throws IOException {
        String line = "", next_command = "", char_name = "", char_number = "";
        BufferedReader input_stream = null;
        float width = 200;
        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        int b_x1 = 0, b_y1 = 0, b_x2 = 0, b_y2 = 0;
        {
            input_stream = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("org/jpedal/res/pdf/defaults/" + files_names[i] + ".afm"), enc));
            boolean char_mapping_table = false;
            while (true) {
                line = input_stream.readLine();
                if (line == null) break;
                if (line.startsWith("EndCharMetrics")) char_mapping_table = false;
                if (char_mapping_table == true) {
                    StringTokenizer values = new StringTokenizer(line, " ;");
                    while (values.hasMoreTokens()) {
                        next_command = values.nextToken();
                        if (next_command.equals("WX")) width = Float.parseFloat(values.nextToken()) / 1000; else if (next_command.equals("N")) char_name = values.nextToken();
                    }
                    widthTableStandard.put(files_names_bis[i] + char_name, new Float(width));
                    widthTableStandard.put(files_names[i] + char_name, new Float(width));
                }
                if (line.startsWith("StartCharMetrics")) char_mapping_table = true;
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
