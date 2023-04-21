    public static String getSeriesTvDB(String name, Context context) {
        URL url;
        String ret = "";
        String site = context.getString(R.string.getSeries);
        String paramName = context.getString(R.string.getSeriesParam);
        try {
            url = new URL(site + "?" + paramName + "=" + URLEncoder.encode(name));
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
                NodeList nl = docEle.getElementsByTagName("Series");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element entry = (Element) nl.item(i);
                        Element SerieName = (Element) entry.getElementsByTagName("SeriesName").item(0);
                        if (i == 0) {
                            ret += SerieName.getFirstChild().getNodeValue();
                        } else {
                            ret += "," + SerieName.getFirstChild().getNodeValue();
                        }
                    }
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
        }
        return ret;
    }
