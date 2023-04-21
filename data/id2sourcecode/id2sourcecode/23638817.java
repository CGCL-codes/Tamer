    public static Serie getSeriesInfo(int id, Context context) {
        URL url;
        Serie serie = new Serie();
        String site = context.getString(R.string.infoSerieUrl);
        String siteEnd = context.getString(R.string.infoSerieUrlEnd);
        try {
            url = new URL(site + id + siteEnd);
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
                        Element SerieDesc = (Element) entry.getElementsByTagName("Overview").item(0);
                        Element SerieImgUrl = (Element) entry.getElementsByTagName("banner").item(0);
                        Element SerieState = (Element) entry.getElementsByTagName("Status").item(0);
                        Element SerieRating = (Element) entry.getElementsByTagName("Rating").item(0);
                        serie = new Serie();
                        serie.setName(SerieName.getFirstChild().getNodeValue());
                        serie.setId(SerieId.getFirstChild().getNodeValue());
                        serie.setRate(SerieRating.getFirstChild().getNodeValue());
                        if (SerieDesc.getFirstChild() != null) serie.setDesc(SerieDesc.getFirstChild().getNodeValue());
                        if (SerieImgUrl.getFirstChild() != null) serie.setImgUrl(SerieImgUrl.getFirstChild().getNodeValue());
                        if (SerieState.getFirstChild() != null) serie.setEstado(SerieState.getFirstChild().getNodeValue(), context);
                        boolean exists = serieAlreadyExists(Integer.parseInt(serie.getId()), context);
                        serie.setFav(exists);
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
        return serie;
    }
