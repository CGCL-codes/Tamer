    public static List<Serie> getSeriesTvDBList(String name, Context context) {
        URL url;
        List<Serie> ret = new ArrayList<Serie>();
        Serie serie;
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
                        Element SerieId = (Element) entry.getElementsByTagName("id").item(0);
                        serie = new Serie();
                        serie.setName(SerieName.getFirstChild().getNodeValue());
                        serie.setId(SerieId.getFirstChild().getNodeValue());
                        serie.setFav(serieAlreadyExists(Integer.parseInt(serie.getId()), context));
                        ret.add(serie);
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
