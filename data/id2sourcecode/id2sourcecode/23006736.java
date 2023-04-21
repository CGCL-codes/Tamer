    private void sendPolygons(PlanarPolygonTable ppt, String onto_class, String name) throws MalformedURLException, IOException, JDOMException {
        String querystring = this._url_base + "&type=annotate" + "&name=" + name + "&color=" + (int) (Math.random() * 0x01000000);
        double offset = (this._planes - 1) / 2.0;
        boolean first = true;
        for (Double d : ppt.getKeys()) {
            for (PlanarPolygon p : ppt.getArray(d)) {
                HttpURLConnection urlconn = (HttpURLConnection) new URL(querystring).openConnection();
                urlconn.setDoOutput(true);
                urlconn.connect();
                PrintStream ps = new PrintStream(urlconn.getOutputStream());
                double xya[][] = p.getXY();
                String out = String.format("%d\n", xya.length);
                for (double[] xy : xya) out += String.format("%g %g %g\n", xy[0], xy[1], d.doubleValue() - offset);
                if (first) {
                    first = false;
                    out += "annotations 1\n" + "0:object " + onto_class;
                    ps.println(out);
                    SAXBuilder sb = new SAXBuilder();
                    Element r = sb.build(urlconn.getInputStream()).getRootElement();
                    String id = r.getAttributeValue("id");
                    querystring = this._url_base + "&type=contour" + "&id=" + id;
                } else ps.print(out);
                ps.close();
                urlconn.disconnect();
            }
        }
    }
