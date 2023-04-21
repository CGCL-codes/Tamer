    private void testXMLRPC115(ClientProvider pProvider) throws Exception {
        if (pProvider instanceof SunHttpTransportProvider) {
            XmlRpcClient client = pProvider.getClient();
            client.setConfig(getConfig(pProvider));
            URL url = ((XmlRpcHttpClientConfig) client.getConfig()).getServerURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/xml");
            OutputStream ostream = conn.getOutputStream();
            Writer w = new OutputStreamWriter(ostream, "UTF-8");
            w.write("<methodCall><methodName>" + XMLRPC115Handler.class.getName() + ".ping" + "</methodName></methodCall>");
            w.close();
            InputStream istream = conn.getInputStream();
            XmlRpcResponseParser parser = new XmlRpcResponseParser((XmlRpcStreamRequestConfig) client.getClientConfig(), client.getTypeFactory());
            XMLReader xr = SAXParsers.newXMLReader();
            xr.setContentHandler(parser);
            xr.parse(new InputSource(istream));
            istream.close();
            assertTrue(parser.getResult() instanceof Object[]);
            assertEquals(0, ((Object[]) parser.getResult()).length);
        }
    }
