    @Override
    public void search(StringTokenizer terms, ISearchParams parameters) throws Exception {
        String query = "";
        while (terms.hasMoreElements()) {
            if ("".equals(query)) {
                query += terms.nextElement();
            } else {
                query += "+" + terms.nextElement();
            }
        }
        query = URLEncoder.encode(query, "UTF-8");
        parameters.addParam(PARAM.QUERY, query);
        mResults = new SearchResultsImpl();
        this.mParameters = parameters;
        URL urlGSA = new URL(this.getRequestSearchURL(parameters));
        URLConnection connection = urlGSA.openConnection();
        connection.setAllowUserInteraction(false);
        connection.setDoOutput(true);
        InputSource input = new InputSource(connection.getInputStream());
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        sp.parse(input, new ResultsGSAXMLParser());
    }
