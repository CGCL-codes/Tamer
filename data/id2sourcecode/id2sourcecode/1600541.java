    public BookDetailHandler(String id) throws Exception {
        try {
            product = new ProductFullBook();
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy?method=GetProduct&product_id=" + id);
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
            NodeList nl = dom.getElementsByTagName("product");
            Element e = (Element) nl.item(0);
            NodeList name = e.getElementsByTagName("name");
            Element line = (Element) name.item(0);
            product.name = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("sales_rank");
            line = (Element) name.item(0);
            product.rank = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("price");
            line = (Element) name.item(0);
            product.price = getCharacterDataFromElement(line);
            name = e.getElementsByTagName("image_url");
            line = (Element) name.item(0);
            product.url = getCharacterDataFromElement(line);
            name = e.getElementsByTagName("authors");
            line = (Element) name.item(0);
            product.authors = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("publisher");
            line = (Element) name.item(0);
            product.publisher = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("published_date");
            line = (Element) name.item(0);
            product.publish_date = getCharacterDataFromElement(line);
            e = (Element) nl.item(0);
            name = e.getElementsByTagName("language");
            line = (Element) name.item(0);
            product.language = getCharacterDataFromElement(line);
        } catch (Exception e) {
            throw e;
        }
    }
