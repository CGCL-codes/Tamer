        public Map<String, List<Tip>> parse(URL url) throws SAXException, ParserConfigurationException, IOException {
            SAXParserFactory f = SAXParserFactory.newInstance();
            SAXParser p = f.newSAXParser();
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Analytics");
            InputStream is = connection.getInputStream();
            try {
                p.parse(is, this);
            } finally {
                is.close();
            }
            return tips;
        }
