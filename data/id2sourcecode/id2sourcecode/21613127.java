        public FlatMapResourceBundle(URL url) {
            source = url;
            Reader src;
            try {
                URLConnection uc = url.openConnection();
                src = XmlReader.createReader(uc);
                parse(src);
                src.close();
            } catch (Exception e) {
                reportMessage("Exception caught while parsing " + url + " " + e.toString(), false);
            }
            parsingEnded();
            for (Enumeration e = lookup.keys(); e.hasMoreElements(); ) {
                String key1 = (String) e.nextElement();
                String url1 = (String) lookup.get(key1);
            }
        }
