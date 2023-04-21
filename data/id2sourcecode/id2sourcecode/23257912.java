    public static String getRefTypes() {
        try {
            URL url = new URL(props.baseURL + props.refTypes);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            URLConnection conn = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            String toParse = "";
            while ((inputLine = reader.readLine()) != null) {
                toParse += inputLine;
            }
            System.out.print(toParse);
            InputSource is = new InputSource(conn.getInputStream());
            Document d = db.parse(is);
            Element root = d.getElementById("eveapi");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "Error";
    }
