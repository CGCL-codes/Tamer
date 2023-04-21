        URL findResource(final String name, boolean check) {
            URL url;
            try {
                url = new URL(base, name);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("name");
            }
            try {
                if (check) {
                    URLClassPath.check(url);
                }
                InputStream is = url.openStream();
                is.close();
                return url;
            } catch (Exception e) {
                return null;
            }
        }
