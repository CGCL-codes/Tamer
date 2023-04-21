    public Searchresults getSearchResults(String pText) {
        String result = "unknown";
        Searchresults results = new Searchresults();
        try {
            if (true) {
                StringBuilder b = new StringBuilder();
                b.append("http://nominatim.openstreetmap.org/search/?q=");
                b.append(URLEncoder.encode(pText, "UTF-8"));
                b.append("&format=xml&limit=30&accept-language=").append(Locale.getDefault().getLanguage());
                logger.fine("Searching for " + b.toString());
                URL url = new URL(b.toString());
                URLConnection urlConnection = url.openConnection();
                if (Tools.isAboveJava4()) {
                    urlConnection.setConnectTimeout(Resources.getInstance().getIntProperty(OSM_NOMINATIM_CONNECT_TIMEOUT_IN_MS, 10000));
                    urlConnection.setReadTimeout(Resources.getInstance().getIntProperty(OSM_NOMINATIM_READ_TIMEOUT_IN_MS, 30000));
                }
                InputStream urlStream = urlConnection.getInputStream();
                result = Tools.getFile(new InputStreamReader(urlStream));
                result = new String(result.getBytes(), "UTF-8");
                logger.fine(result + " was received for search " + pText);
            } else {
                result = XML_VERSION_1_0_ENCODING_UTF_8 + "<searchresults timestamp=\"Tue, 08 Nov 11 22:49:54 -0500\" attribution=\"Data Copyright OpenStreetMap Contributors, Some Rights Reserved. CC-BY-SA 2.0.\" querystring=\"innsbruck\" polygon=\"false\" exclude_place_ids=\"228452,25664166,26135863,25440203\" more_url=\"http://open.mapquestapi.com/nominatim/v1/search?format=xml&amp;exclude_place_ids=228452,25664166,26135863,25440203&amp;accept-language=&amp;q=innsbruck\">\n" + "  <place place_id=\"228452\" osm_type=\"node\" osm_id=\"34840064\" place_rank=\"16\" boundingbox=\"47.2554266357,47.2754304504,11.3827679062,11.4027688599\" lat=\"47.2654296\" lon=\"11.3927685\" display_name=\"Innsbruck, Bezirk Innsbruck-Stadt, Innsbruck-Stadt, Tirol, Österreich, Europe\" class=\"place\" type=\"city\" icon=\"http://open.mapquestapi.com/nominatim/v1/images/mapicons/poi_place_city.p.20.png\"/>\n" + "  <place place_id=\"25664166\" osm_type=\"way\" osm_id=\"18869490\" place_rank=\"27\" boundingbox=\"43.5348739624023,43.5354156494141,-71.1319198608398,-71.1316146850586\" lat=\"43.5351336524196\" lon=\"-71.1317853486877\" display_name=\"Innsbruck, New Durham, Strafford County, New Hampshire, United States of America\" class=\"highway\" type=\"service\"/>\n" + "  <place place_id=\"26135863\" osm_type=\"way\" osm_id=\"18777572\" place_rank=\"27\" boundingbox=\"38.6950759887695,38.6965446472168,-91.1586227416992,-91.1520233154297\" lat=\"38.6957456083531\" lon=\"-91.1552550683042\" display_name=\"Innsbruck, Warren, Aspenhoff, Warren County, Missouri, United States of America\" class=\"highway\" type=\"service\"/>\n" + "  <place place_id=\"25440203\" osm_type=\"way\" osm_id=\"18869491\" place_rank=\"27\" boundingbox=\"43.5335311889648,43.5358810424805,-71.1356735229492,-71.1316146850586\" lat=\"43.5341678362733\" lon=\"-71.1338615946084\" display_name=\"Innsbruck, New Durham, Strafford County, New Hampshire, 03855, United States of America\" class=\"highway\" type=\"service\"/>\n" + "</searchresults>";
            }
            results = (Searchresults) XmlBindingTools.getInstance().unMarshall(result);
            if (results == null) {
                logger.warning(result + " can't be parsed");
            }
        } catch (Exception e) {
            freemind.main.Resources.getInstance().logException(e);
            Place place = new Place();
            place.setDisplayName(e.toString());
            place.setOsmType("ERROR");
            Coordinate cursorPosition = getMap().getCursorPosition();
            place.setLat(cursorPosition.getLat());
            place.setLon(cursorPosition.getLon());
            results.addPlace(place);
        }
        return results;
    }
