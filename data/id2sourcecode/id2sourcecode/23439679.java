        public Object getImage(String key) throws IOException {
            URL url = new URL(getBaseURL() + "icons/" + key + extensionFor(key));
            InputStream inputStream = url.openStream();
            inputStream.close();
            return url;
        }
