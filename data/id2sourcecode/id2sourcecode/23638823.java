    public static List<Serie> getRecommendations(Context context) {
        URL url;
        Serie serie;
        List<Serie> recommendations = new ArrayList<Serie>();
        String site = "http://www.seriesnotifier.com/series/seriesByRate";
        try {
            url = new URL(site);
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
                NodeList nl = docEle.getElementsByTagName("serie");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element nSerie = (Element) nl.item(i);
                        Node id = nSerie.getChildNodes().item(0);
                        Node seriename = nSerie.getChildNodes().item(1);
                        Node rate = nSerie.getChildNodes().item(2);
                        Node votes = nSerie.getChildNodes().item(3);
                        serie = new Serie();
                        serie.setId(id.getFirstChild().getNodeValue());
                        serie.setName(seriename.getFirstChild().getNodeValue());
                        serie.setRate(rate.getFirstChild().getNodeValue());
                        serie.setVotes(votes.getFirstChild().getNodeValue());
                        serie.setFav(serieAlreadyExists(Integer.parseInt(serie.getId()), context));
                        recommendations.add(serie);
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
        return recommendations;
    }
