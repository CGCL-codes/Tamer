    public LanguageHandler() {
        try {
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Common.groovy?method=GetLanguageList");
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
                return;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(response));
            Document dom = db.parse(is);
            NodeList nl = dom.getElementsByTagName("language");
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);
                String id = e.getAttribute("id");
                NodeList name = e.getElementsByTagName("name");
                Element line = (Element) name.item(0);
                String nameS = getCharacterDataFromElement(line);
                NodeList code = e.getElementsByTagName("code");
                line = (Element) code.item(0);
                String codeS = getCharacterDataFromElement(line);
                languages.add(new Language(id, codeS, nameS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
