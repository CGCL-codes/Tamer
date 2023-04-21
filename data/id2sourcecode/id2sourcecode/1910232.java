                public Source resolve(String href, String base) throws TransformerException {
                    try {
                        URL url = new URL(href);
                        URLConnection conn = url.openConnection();
                        return new StreamSource(conn.getInputStream());
                    } catch (Exception exp) {
                    }
                    try {
                        URL context = new URL(base);
                        URL url = new URL(context, href);
                        URLConnection conn = url.openConnection();
                        return new StreamSource(conn.getInputStream());
                    } catch (Exception exp) {
                    }
                    return null;
                }
