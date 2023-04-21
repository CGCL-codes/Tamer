        public Source resolve(String href, String base) {
            File file = new File(href);
            URL url = null;
            String fullPath = null;
            Source src = null;
            try {
                if (href.startsWith("http:")) {
                    url = new URL(href);
                    src = new StreamSource(url.openStream());
                } else if (file.isAbsolute()) {
                    src = new StreamSource(new File(href));
                } else {
                    src = new StreamSource(new File(baseDir + "/" + href));
                }
            } catch (Exception e) {
                System.out.println("resolve: " + e.getMessage());
            }
            return src;
        }
