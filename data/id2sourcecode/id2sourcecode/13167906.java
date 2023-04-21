        public Source resolve(String href, String base) throws TransformerException {
            URL url;
            try {
                url = new URL(baseURI + "/" + href);
                URLConnection connection = url.openConnection();
                return new StreamSource(connection.getInputStream());
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
