    protected long loadFromSource(long afterThisTime) {
        long startTime = System.currentTimeMillis();
        QuoteDataSourceDescriptor quoteDataSourceDescriptor = (QuoteDataSourceDescriptor) dataSourceDescriptor;
        List<Quote> dataPool = dataPools.get(quoteDataSourceDescriptor.sourceSymbol);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        SimpleDateFormat sdf = new SimpleDateFormat(quoteDataSourceDescriptor.dateFormat, Locale.US);
        BufferedReader dis;
        boolean EOF = false;
        try {
            InputStreamReader isr;
            if (quoteDataSourceDescriptor.inputStream == null) {
                URL url = new URL(quoteDataSourceDescriptor.urlString);
                isr = new InputStreamReader(url.openStream());
            } else {
                isr = new InputStreamReader(quoteDataSourceDescriptor.inputStream);
            }
            dis = new BufferedReader(isr);
            String s = dis.readLine();
            count = 0;
            calendar.clear();
            while (!EOF) {
                s = dis.readLine();
                if (s == null) {
                    break;
                }
                StringTokenizer t = new StringTokenizer(s, ",");
                if (t.countTokens() >= 5) {
                    Date date = null;
                    try {
                        date = sdf.parse(t.nextToken().trim());
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
                    Quote quote = new Quote();
                    quote.time = time;
                    quote.open = Float.parseFloat(t.nextToken().trim());
                    quote.high = Float.parseFloat(t.nextToken().trim());
                    quote.low = Float.parseFloat(t.nextToken().trim());
                    quote.close = Float.parseFloat(t.nextToken().trim());
                    quote.volume = Float.parseFloat(t.nextToken().trim()) / 100f;
                    quote.amount = -1;
                    dataPool.add(quote);
                    if (count == 0) {
                        firstTime = time;
                    }
                    lastTime = time;
                    setAscending((lastTime >= firstTime) ? true : false);
                    count++;
                }
            }
        } catch (EOFException e) {
            EOF = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        long newestTime = (lastTime >= firstTime) ? lastTime : firstTime;
        return newestTime;
    }
