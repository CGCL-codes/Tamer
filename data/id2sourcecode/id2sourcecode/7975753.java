    public static Tuple<GazeteerPlace, Collection<GazeteerPlace>> readGeoParserResult(String recordContent, boolean getGazeteerIds, int topResultsToGet) {
        if (TESTING) {
            HashSet<GazeteerPlace> ret = new HashSet<GazeteerPlace>();
            ret.add(new GazeteerPlace("teste", "teste", new ArrayList<GazeteerFeature>()));
            return new Tuple<GazeteerPlace, Collection<GazeteerPlace>>(new GazeteerPlace("", "", new ArrayList<GazeteerFeature>()), ret);
        }
        int retries = 0;
        while (retries < 3) {
            try {
                ArrayList<GazeteerPlace> ret = new ArrayList<GazeteerPlace>();
                String reqPre = "<?xml version=\"1.0\"?>\r\n" + (getGazeteerIds ? "<GetFeature" : "<GetParsing") + " xmlns=\"http://www.opengis.net/gp\" xmlns:wfs=\"http://www.opengis.net/wfs\"" + " xmlns:xsi=\"http://www.w3.org/2000/10/XMLSchema-instance\"" + " xsi:schemaLocation=\"http://www.opengis.net/gp ../gp/GetFeatureRequest.xsd http://www.opengis.net/wfs ../wfs/GetFeatureRequest.xsd\"\r\n" + " wfs:outputFormat=\"GML2\">" + "<wfs:Query wfs:TypeName=\"PlaceName\" />" + "<Resource mine=\"text/plain\">" + "<Contents></Contents>" + "</Resource>" + (getGazeteerIds ? "</GetFeature>" : "</GetParsing>");
                Document doc = DocumentHelper.parseText(reqPre);
                doc.getRootElement().element("Resource").element("Contents").setText(recordContent);
                URL url = new URL(geoParserBaseUrl + "?request=" + URLEncoder.encode(doc.asXML(), "ISO8859-1"));
                InputStream urlStream = url.openStream();
                InputStreamReader reader = new InputStreamReader(urlStream, "UTF-8");
                BufferedReader buffered = new BufferedReader(reader);
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = buffered.readLine()) != null) {
                    sb.append(line);
                }
                buffered.close();
                reader.close();
                urlStream.close();
                Document d = DocumentHelper.parseText(sb.toString());
                HashSet<String> places = new HashSet<String>();
                if (d.getRootElement().element("EntryCollection") != null) {
                    for (Iterator<Element> it = d.getRootElement().element("EntryCollection").elementIterator("PlaceName"); it.hasNext(); ) {
                        Element plcEl = it.next();
                        String val = plcEl.elementTextTrim("TermName");
                        if (!val.equals("") && !places.contains(val)) {
                            places.add(val);
                            String entryID = plcEl.attributeValue("entryID");
                            GazeteerPlace plc = new GazeteerPlace(entryID, val, d, topResultsToGet);
                            ret.add(plc);
                        }
                    }
                }
                GazeteerPlace recordPlace = null;
                Element genEl = d.getRootElement().element("General");
                if (genEl != null) {
                    Element geoScopeEl = genEl.element("GeographicScope");
                    if (geoScopeEl != null) {
                        GazeteerFeature feature = new GazeteerFeature(geoScopeEl.attributeValue("id"));
                        feature.setCoordinates(GazeteerPlace.getCoordinates(geoScopeEl));
                        recordPlace = new GazeteerPlace(geoScopeEl.attributeValue("name"), null, feature);
                        recordPlace.mainFeature = feature;
                    }
                }
                errorCountConsecutive = 0;
                return new Tuple<GazeteerPlace, Collection<GazeteerPlace>>(recordPlace, ret);
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("Erro geoparsing do registo " + recordContent + "! " + e.getMessage(), e);
                System.out.println("Erro ao pesquisar a lista de termos para o registo " + recordContent + "! " + e.getMessage());
                retries++;
            }
        }
        System.out.println("Too many retries. Giving up.");
        errorCountConsecutive++;
        if (errorCountConsecutive >= 20) {
            System.out.println("Geoparser Crashed.");
            System.exit(0);
        }
        return null;
    }
