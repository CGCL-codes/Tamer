    private static String getRate(String id) {
        URL url;
        String rate = "";
        String site = "http://www.seriesnotifier.com/series/getRate";
        try {
            url = new URL(site + "?id=" + id);
            System.out.println("URL: " + url);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                DocumentBuilderFactory dbf;
                dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();
                NodeList nl = docEle.getElementsByTagName("rate");
                if (nl != null && nl.getLength() > 0) {
                    Element nSerie = (Element) nl.item(0);
                    rate = nSerie.getFirstChild().getNodeValue();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rate;
    }
