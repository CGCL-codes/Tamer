        @Override
        protected Object doGetImage(String key) throws IOException {
            URL url;
            if (key.endsWith(".png")) {
                url = new URL(getBaseURL() + "icons/" + key);
            } else url = new URL(getBaseURL() + "icons/" + key + ".gif");
            InputStream inputStream = url.openStream();
            inputStream.close();
            return url;
        }
