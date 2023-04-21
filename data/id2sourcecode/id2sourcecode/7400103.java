    private int addURLToMemory(URL Data) throws IOException {
        LineNumberReader url_reader = null;
        String Line = new String("");
        boolean IgnoreHeader = true;
        boolean added_data = true;
        boolean Docontinue = true;
        log.debug(Data.getProtocol() + "://" + Data.getHost() + Data.getFile());
        int initSize = 0;
        if (Data != null) {
            url_reader = new LineNumberReader(new InputStreamReader(new BufferedInputStream(Data.openStream())));
            Line = url_reader.readLine();
            if (StockDates == null) StockDates = new Vector();
            if ((StockData == null) || (StockData.length < 6)) StockData = new Vector[6];
            if (StockData[0] == null) StockData[0] = new Vector();
            if (StockData[1] == null) StockData[1] = new Vector();
            if (StockData[2] == null) StockData[2] = new Vector();
            if (StockData[3] == null) StockData[3] = new Vector();
            if (StockData[4] == null) StockData[4] = new Vector();
            if (StockData[5] == null) StockData[5] = new Vector();
            initSize = StockData[0].size();
            while ((Line != null) && (!Line.equals("")) && (Docontinue == true)) {
                if (IgnoreHeader == true) {
                    if (Line.indexOf(ColumnNames[0]) < 0) {
                        if (Line.indexOf(",") != -1) {
                            StringTokenizer tokens = new StringTokenizer(Line, ",\n");
                            if (tokens.countTokens() >= 7) {
                                added_data = true;
                                String _data = tokens.nextToken();
                                StockDates.addElement(_data);
                                StockData[0].add(Double.valueOf(tokens.nextToken()));
                                StockData[1].add(Double.valueOf(tokens.nextToken()));
                                StockData[2].add(Double.valueOf(tokens.nextToken()));
                                StockData[3].add(Double.valueOf(tokens.nextToken()));
                                StockData[4].add(Double.valueOf(tokens.nextToken()));
                                StockData[5].add(Double.valueOf(tokens.nextToken()));
                            }
                        } else Docontinue = false;
                    }
                }
                Line = url_reader.readLine();
            }
        }
        int addedData = StockData[0].size() - initSize;
        return addedData;
    }
