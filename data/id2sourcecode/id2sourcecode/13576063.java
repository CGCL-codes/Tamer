        Resource getResource(final String name, boolean check) {
            final URL url;
            try {
                url = new URL(base, name);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("name");
            }
            final URLConnection uc;
            try {
                if (check) {
                    URLClassPath.check(url);
                }
                uc = url.openConnection();
                InputStream in = uc.getInputStream();
            } catch (Exception e) {
                return null;
            }
            return new Resource() {

                public String getName() {
                    return name;
                }

                public URL getURL() {
                    return url;
                }

                public URL getCodeSourceURL() {
                    return base;
                }

                public InputStream getInputStream() throws IOException {
                    return uc.getInputStream();
                }

                public int getContentLength() throws IOException {
                    return uc.getContentLength();
                }
            };
        }
