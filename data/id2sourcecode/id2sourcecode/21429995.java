    public void onMessage() throws JMSException, Exception {
        System.out.println("monitoring");
        URL url = new URL("http://frwebgate3.access.gpo.gov/cgi-bin/waisgate.cgi?WAISdocID=28468916704+0+0+0&WAISaction=retrieve");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        Map headers = connection.getHeaderFields();
        Set entries = headers.entrySet();
        Set keys = headers.keySet();
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            System.out.println("*** header " + headers.get(it.next()));
        }
        SORNLexer lexer = new SORNLexer(new BufferedInputStream(connection.getInputStream()));
        SORNParser parser = new SORNParser(lexer);
        parser.run();
        connection.disconnect();
    }
