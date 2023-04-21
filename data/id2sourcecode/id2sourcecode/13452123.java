    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String inputQuery, urlString, xPathString = null;
        System.out.println("Enter the Geocode Request (default is 'New York, NY'): ");
        inputQuery = input.readLine();
        if (inputQuery.equals("")) {
            inputQuery = GEOCODE_QUERY;
        }
        urlString = GEOCODE_REQUEST_PREFIX + "?address=" + URLEncoder.encode(inputQuery, "UTF-8") + "&sensor=false";
        System.out.println(urlString);
        URL url = new URL(urlString);
        System.out.println("Enter the XPath expression to evaluate the response (default is '//text()'): ");
        xPathString = input.readLine();
        if (xPathString.equals("")) {
            xPathString = XPATH_EXPRESSION;
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        Document geocoderResultDocument = null;
        try {
            conn.connect();
            InputSource geocoderResultInputSource = new InputSource(conn.getInputStream());
            geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);
        } finally {
            conn.disconnect();
        }
        NodeList nodes = process(geocoderResultDocument, xPathString);
        for (int i = 0; i < nodes.getLength(); i++) {
            String nodeString = nodes.item(i).getTextContent();
            System.out.print(nodeString);
            System.out.print("\n");
        }
    }
