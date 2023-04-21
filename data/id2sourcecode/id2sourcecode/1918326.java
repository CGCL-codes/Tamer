    public static void main(String[] args) {
        URL url;
        try {
            url = new URL("http://www.panoramio.com/panoramio.kml?LANG=en_US.utf8&BBOX=24.713058,59.424473,24.816055,59.450659&zoom=14&max=10");
            URLConnection conn = url.openConnection();
            InputStream inStr = conn.getInputStream();
            conn.connect();
            Kml k = Kml.unmarshal(inStr);
            Document f = (Document) k.getFeature();
            List<Feature> l1 = f.getFeature();
            List<Coordinate> l2 = new ArrayList<Coordinate>();
            for (Feature p : l1) {
                Geometry g = ((Placemark) p).getGeometry();
                l2.addAll(((Point) g).getCoordinates());
            }
            System.out.println(k.getFeature().getDescription());
        } catch (MalformedURLException ex) {
            System.err.println(ex);
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to open stream to URL: " + ex);
        } catch (IOException ex) {
            System.err.println("Error reading URL content: " + ex);
        }
    }
