    public static List<Serie> getUpdatesTvDBList(Context context) {
        URL url;
        Serie serie;
        List<Serie> seriesNuevas = new ArrayList<Serie>();
        String site = context.getString(R.string.getUpdates);
        String paramTimeName = context.getString(R.string.getUpdatesTimeParam);
        long epoch = DateUtils.getEpochDate(context);
        if (epoch == -1) {
            epoch = (System.currentTimeMillis() / 1000) - (86400);
        }
        String epochString = String.valueOf(epoch);
        try {
            url = new URL(site + "?" + paramTimeName + "=" + URLEncoder.encode(epochString));
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
                        Element SerieId = (Element) nl.item(i);
                        serie = new Serie();
                        serie.setId(SerieId.getFirstChild().getNodeValue());
                        seriesNuevas.add(serie);
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
        DateUtils.insertEpochDate(context, (System.currentTimeMillis() / 1000));
        return filterByOwnSeries(context, seriesNuevas);
    }
