    public static Document doSpellCheck(String pText) {
        Document doc;
        try {
            long start = System.nanoTime();
            StringBuffer requestXML = new StringBuffer();
            requestXML.append("<spellrequest textalreadyclipped=\"0\"" + " ignoredups=\"1\"" + " ignoredigits=\"1\" ignoreallcaps=\"0\"><text>");
            requestXML.append(pText);
            requestXML.append("</text></spellrequest>");
            URL url = new URL(DICTIONARY_REST_API);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(requestXML.toString());
            out.close();
            InputStream in = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            long end = System.nanoTime();
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = br.read()) != -1) {
                sb.append((char) c);
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(sb.toString()));
            doc = db.parse(is);
            in.close();
            System.out.println((end - start) / 1000000);
            System.out.println(sb.toString());
            return doc;
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return null;
        }
    }
