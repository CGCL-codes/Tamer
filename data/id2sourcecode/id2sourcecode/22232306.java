    public CoGObject[] getServices(String serviceType) {
        allService = null;
        try {
            String strQueryService = "//Service[Type='" + serviceType + "']";
            String strQuery = webmdsEndpoint + strQueryLeft + strQueryRight + strQueryService;
            URL url = new URL(strQuery);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer content = new StringBuffer();
            String str;
            while ((str = in.readLine()) != null) {
                content.append(str);
                content.append("\n");
            }
            in.close();
            toCoGObject(new String(content));
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return allService;
    }
