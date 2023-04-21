            private InputStream resolve(String location) {
                try {
                    URL url = new URL(location);
                    return url.openStream();
                } catch (MalformedURLException e) {
                } catch (IOException e) {
                }
                return null;
            }
