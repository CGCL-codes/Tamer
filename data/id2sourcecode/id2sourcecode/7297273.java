    public void BuildAndSendQuery() throws IOException {
        String sURL;
        sURL = GetDBUrl();
        sURL = sURL + sQueryTemplate;
        if (sQueryName != "") {
            sURL = sURL + "&QueryName=" + sQueryName;
        }
        sURL = sURL + "&sWhere=";
        if (sWhere != "") {
            sURL = sURL + URLEncoder.encode("where ") + sWhere;
        }
        if (sOrderBy != "") {
            sURL = sURL + sOrderBy;
        }
        if (sParameters != "") {
            sURL = sURL + "&" + sParameters;
        }
        if (DoTraceSQL()) {
            System.out.println("loading url - " + sURL);
        }
        try {
            url = new URL(sURL);
            connection = url.openConnection();
        } catch (MalformedURLException e) {
            System.out.println("url not found " + sURL);
        }
        ;
        in = new DataInputStream(connection.getInputStream());
        ReadLine();
    }
