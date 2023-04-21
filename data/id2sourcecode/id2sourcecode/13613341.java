    protected long loadFromSource(long afterThisTime) {
        QuoteDataSourceDescriptor quoteDataSourceDescriptor = (QuoteDataSourceDescriptor) dataSourceDescriptor;
        List<Quote> dataPool = dataPools.get(quoteDataSourceDescriptor.sourceSymbol);
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        DataInputStream dis;
        boolean EOF = false;
        try {
            URL url = new URL("file:data/Slon/data/sh/day/" + quoteDataSourceDescriptor.sourceSymbol + ".day");
            dis = new DataInputStream(url.openStream());
            count = 0;
            calendar.clear();
            while (!EOF) {
                int iDate = toProperFormat(dis.readInt());
                int year = iDate / 10000;
                int month = (iDate - year * 10000) / 100;
                int day = iDate - year * 10000 - month * 100;
                calendar.clear();
                calendar.set(year, month - 1, day);
                long time = calendar.getTimeInMillis();
                if (time <= afterThisTime) {
                    continue;
                }
                Quote quote = new Quote();
                quote.time = time;
                quote.open = toProperFormat(dis.readInt()) / 1000f;
                quote.close = toProperFormat(dis.readInt()) / 1000f;
                quote.high = toProperFormat(dis.readInt()) / 1000f;
                quote.low = toProperFormat(dis.readInt()) / 1000f;
                quote.amount = toProperFormat(dis.readInt());
                quote.volume = toProperFormat(dis.readInt());
                dis.skipBytes(12);
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
            System.out.println("Error in Reading File");
        }
        long newestTime = (lastTime >= firstTime) ? lastTime : firstTime;
        return newestTime;
    }
