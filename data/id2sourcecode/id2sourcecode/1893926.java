    protected long loadFromSource(long afterThisTime) {
        long startTime = System.currentTimeMillis();
        QuoteDataSourceDescriptor quoteDataSourceDescriptor = (QuoteDataSourceDescriptor) dataSourceDescriptor;
        String symbol = quoteDataSourceDescriptor.sourceSymbol;
        List<Quote> dataPool = dataPools.get(quoteDataSourceDescriptor.sourceSymbol);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        SimpleDateFormat sdf = new SimpleDateFormat(quoteDataSourceDescriptor.dateFormat, Locale.US);
        BufferedReader dis;
        boolean EOF = false;
        try {
            URL url = new URL(quoteDataSourceDescriptor.urlString);
            InputStreamReader isr = new InputStreamReader(url.openStream());
            dis = new BufferedReader(isr);
            int iCode = 0;
            int iDate = 0;
            int iTime = 0;
            int iOpen = 0;
            int iHigh = 0;
            int iLow = 0;
            int iClose = 0;
            int iVolume = 0;
            String s = dis.readLine();
            StringTokenizer tTilte = new StringTokenizer(s, ",");
            String token = null;
            int tokenCount = tTilte.countTokens();
            for (int i = 0; i < tokenCount; i++) {
                token = tTilte.nextToken().trim().toUpperCase();
                if (token.contains("TICKER")) {
                    iCode = i;
                } else if (token.contains("YYMMDD")) {
                    iDate = i;
                } else if (token.contains("TIME")) {
                    iTime = i;
                } else if (token.contains("OPEN")) {
                    iOpen = i;
                } else if (token.contains("HIGH")) {
                    iHigh = i;
                } else if (token.contains("LOW")) {
                    iLow = i;
                } else if (token.contains("CLOSE")) {
                    iClose = i;
                } else if (token.contains("VOLUME")) {
                    iVolume = i;
                }
            }
            float open = 0;
            float high = 0;
            float low = 0;
            float close = 0;
            float volume = 0;
            count = 0;
            calendar.clear();
            while (!EOF) {
                s = dis.readLine();
                if (s == null) {
                    break;
                }
                String[] items;
                items = s.split(",");
                if (items.length < 5 || symbol.contains(items[iCode]) == false) {
                    break;
                }
                String dateStr = items[iDate] + " " + items[iTime];
                Date date = null;
                try {
                    date = sdf.parse(dateStr.trim());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    continue;
                }
                calendar.clear();
                calendar.setTime(date);
                long time = calendar.getTimeInMillis();
                if (time <= afterThisTime) {
                    continue;
                }
                open = Float.parseFloat(items[iOpen].trim());
                high = Float.parseFloat(items[iHigh].trim());
                low = Float.parseFloat(items[iLow].trim());
                close = Float.parseFloat(items[iClose].trim());
                volume = Float.parseFloat(items[iVolume].trim()) / 100f;
                Quote quote = new Quote();
                quote.time = time;
                quote.open = open;
                quote.high = high;
                quote.low = low;
                quote.close = close;
                quote.volume = volume;
                quote.amount = -1;
                dataPool.add(quote);
                if (count == 0) {
                    firstTime = time;
                }
                lastTime = time;
                setAscending((lastTime >= firstTime) ? true : false);
                count++;
            }
        } catch (EOFException e) {
            EOF = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        long newestTime = (lastTime >= firstTime) ? lastTime : firstTime;
        return newestTime;
    }
