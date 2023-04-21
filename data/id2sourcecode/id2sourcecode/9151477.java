    private void cacheXTVD(boolean fresh) throws Exception {
        String dataDirectory = System.getProperty("dataDirectory", ".");
        String fileName = dataDirectory + File.separator + DATA_FILE_NAME;
        if (fresh) {
            fetchGuide(fileName);
        }
        _xtvd = new Xtvd();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        Parser parser = ParserFactory.getXtvdParser(reader, _xtvd);
        parser.parseXTVD();
        reader.close();
        Map lineups = _xtvd.getLineups();
        if (lineups.size() != 1) {
            throw new Exception("FATAL: more than one cable lineup on your acct");
        }
        Lineup lineup = (Lineup) _xtvd.getLineups().values().iterator().next();
        _channel = new HashMap();
        Iterator mapiter = lineup.getMap().iterator();
        while (mapiter.hasNext()) {
            com.tms.webservices.applications.xtvd.Map m = (com.tms.webservices.applications.xtvd.Map) mapiter.next();
            Integer stationid = new Integer(m.getStation());
            if (_channel.containsKey(stationid)) {
                System.err.println("Dup channel for " + ((Station) _xtvd.getStations().get(stationid)).getCallSign() + ". Keeping " + _channel.get(stationid) + ", discarding " + m.getChannel());
            } else {
                _channel.put(stationid, m.getChannel());
            }
        }
    }
