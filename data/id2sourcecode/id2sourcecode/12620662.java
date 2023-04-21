    public LoginHandler(String username, String password) {
        try {
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Security.groovy?method=SignIn&username=" + username + "&password=" + password);
            URLConnection urlc = url.openConnection();
            urlc.setDoOutput(false);
            urlc.setAllowUserInteraction(false);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            br.close();
            String response = sb.toString();
            if (response == null) {
                datos = null;
                return;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(response));
            Document dom = db.parse(is);
            NodeList nl = dom.getElementsByTagName("response");
            String status = ((Element) nl.item(0)).getAttributes().item(0).getTextContent();
            if (status.toString().equals("fail")) {
                nl = dom.getElementsByTagName("error");
                String code = ((Element) nl.item(0)).getAttributes().item(0).getTextContent();
                if (code.toString().equals("104")) {
                    datos.add("-1");
                }
                return;
            }
            nl = dom.getElementsByTagName("token");
            Element line = (Element) nl.item(0);
            datos.add(getCharacterDataFromElement(line));
            nl = dom.getElementsByTagName("user");
            datos.add(nl.item(0).getAttributes().getNamedItem("id").getNodeValue());
            datos.add(nl.item(0).getAttributes().getNamedItem("username").getNodeValue());
            datos.add(nl.item(0).getAttributes().getNamedItem("name").getNodeValue());
            datos.add(nl.item(0).getAttributes().getNamedItem("last_login_date").getNodeValue());
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        datos = null;
    }
