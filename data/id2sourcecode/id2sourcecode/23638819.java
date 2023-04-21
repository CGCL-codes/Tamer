    public static List<Episode> getUpdatesService(Context context) {
        URL url;
        Episode ep;
        List<Episode> episodesNuevos = new ArrayList<Episode>();
        List<Serie> seriesDB = new ArrayList<Serie>();
        String site = context.getString(R.string.getUpdatesService);
        String paramTimeName = context.getString(R.string.getUpdatesTimeParam);
        String generateSeriesRequestString = "";
        long epoch = DateUtils.getEpochDate(context);
        if (epoch == -1) {
            epoch = (System.currentTimeMillis() / 1000) - (86400 * 30);
        }
        String epochString = String.valueOf(epoch);
        System.out.println("Obtenemos las actualizaciones de series desde el servicio");
        try {
            seriesDB = getDBSeries(context);
            generateSeriesRequestString = generateSeriesRequestString(seriesDB);
            url = new URL(site + "&" + paramTimeName + "=" + URLEncoder.encode(epochString) + "&series=" + generateSeriesRequestString);
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
                NodeList nl = docEle.getElementsByTagName("episode");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element episode = (Element) nl.item(i);
                        Node id = episode.getChildNodes().item(0);
                        Node serieid = episode.getChildNodes().item(2);
                        Node seriename = episode.getChildNodes().item(1);
                        Node seasonNumber = episode.getChildNodes().item(3);
                        Node episodeNumber = episode.getChildNodes().item(4);
                        Node date = episode.getChildNodes().item(5);
                        Node rate = episode.getChildNodes().item(6);
                        Node votes = episode.getChildNodes().item(7);
                        ep = new Episode();
                        ep.setId(id.getFirstChild().getNodeValue());
                        ep.setSerieId(serieid.getFirstChild().getNodeValue());
                        ep.setSerieName(seriename.getFirstChild().getNodeValue());
                        ep.setSeason(seasonNumber.getFirstChild().getNodeValue());
                        ep.setEpisode(episodeNumber.getFirstChild().getNodeValue());
                        int numVotes = (((Integer.parseInt(votes.getFirstChild().getNodeValue()) != 0)) ? Integer.parseInt(votes.getFirstChild().getNodeValue()) : 1);
                        ep.setRate((float) Integer.parseInt(rate.getFirstChild().getNodeValue()) / (float) numVotes);
                        ep.setDate(date.getFirstChild().getNodeValue());
                        episodesNuevos.add(ep);
                        addDBSerieUpdates(ep, context);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        DateUtils.insertEpochDate(context, (System.currentTimeMillis() / 1000));
        return episodesNuevos;
    }
