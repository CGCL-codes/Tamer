    public void sendPOIGpx(Position loc, List<String> poisCheck) throws Exception {
        myloc = loc;
        checkedPois = poisCheck;
        url = null;
        double left = myloc.getY() - 0.025;
        double right = myloc.getY() + 0.025;
        double top = myloc.getX() + 0.03;
        double bottom = myloc.getX() - 0.03;
        String poiSelected = poisCheck.get(0);
        for (String string : checkedPois) {
            Log.e("CHECKED", string);
        }
        try {
            if (poiSelected.compareTo("None") == 0) {
                model.setPointsOfInterest(new Items());
            } else {
                url = new URL("http://www.informationfreeway.org/api/0.6/node[" + poiSelected + "=*][bbox=" + left + "," + bottom + "," + right + "," + top + "]");
                SAXParser pars = null;
                ParsePoiGpx gpxHandler = new ParsePoiGpx(checkedPois, ctx);
                pars = SAXParserFactory.newInstance().newSAXParser();
                pars.getXMLReader().setFeature("http://xml.org/sax/features/namespaces", true);
                pars.parse(url.openStream(), gpxHandler);
                Items pois = gpxHandler.getPOIResultsItems();
                Log.d("OSMparser", "number of POIs found :: " + pois.getLength());
                if (pois.getLength() == 0) {
                    throw new ExecutionException(new Exception());
                } else {
                    model.setPointsOfInterest(pois);
                }
            }
        } catch (IOException e) {
            throw new Exception(ctx.getString(R.string.ioException));
        } catch (ExecutionException e) {
            throw new Exception(ctx.getString(R.string.noPois));
        } catch (Exception e) {
            throw new Exception(ctx.getString(R.string.parserException));
        }
    }
