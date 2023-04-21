        protected InputStream loadInputStream(String resource) throws IOException {
            if (resource != null) {
                InputStream in;
                try {
                    in = new URL(resource).openStream();
                } catch (MalformedURLException e) {
                    URL url;
                    try {
                        if (servlet != null) {
                            url = servlet.getServletContext().getResource(resource);
                        } else {
                            File appBasedFile = new File(appPath, resource);
                            if (appBasedFile.exists()) {
                                url = appBasedFile.toURI().toURL();
                            } else {
                                url = null;
                            }
                        }
                    } catch (MalformedURLException e2) {
                        url = null;
                    }
                    if (url == null) {
                        File file = new File(resource);
                        if (file.exists()) {
                            in = new FileInputStream(file);
                        } else {
                            throw new IllegalArgumentException("The resource file " + resource + " is not a file and not a web resource: " + url + ".  Perhaps a leading / was forgotten?");
                        }
                    } else {
                        in = url.openStream();
                    }
                }
                return in;
            }
            return null;
        }
