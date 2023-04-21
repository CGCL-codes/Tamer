    public ArrayList<Product> GetProducts(String txtProduct) throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://djey01.fourretout.net/Service1.asmx/GetProducts");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("txtSearch", "tele"));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost);
        InputStream is = response.getEntity().getContent();
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayBuffer baf = new ByteArrayBuffer(20);
        int current = 0;
        while ((current = bis.read()) != -1) {
            baf.append((byte) current);
        }
        String text = new String(baf.toByteArray());
        ByteArrayInputStream xmlParseInputStream = new ByteArrayInputStream(text.getBytes());
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parseur = factory.newSAXParser();
        ProductHandler productHandler = new ProductHandler();
        parseur.parse(xmlParseInputStream, productHandler);
        return productHandler.myProducts;
    }
