    public static String jsonToXML(String loc) {
        String xml = "";
        String jsonData = "";
        try {
            URL url = new URL(loc);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                jsonData += inputLine;
            }
            XMLSerializer serializer = new XMLSerializer();
            JSON json = JSONSerializer.toJSON(jsonData);
            serializer.setTypeHintsEnabled(false);
            xml = serializer.write(json);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return xml;
    }
