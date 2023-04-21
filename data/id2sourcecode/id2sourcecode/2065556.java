            protected URLConnection openConnection(URL _url) throws IOException {
                URL url = _url;
                String n = url.getFile();
                int p = n.indexOf("!/");
                if (p < 0) return new URL(n).openConnection();
                String u = n.substring(0, p);
                if (!n.startsWith("file:")) {
                    try {
                        VfsCache cache = VfsLib.getCache();
                        if (cache != null) {
                            File f = cache.getFile(new URL(u), VfsCache.CHECK_AND_DOWNLOAD);
                            if (f != null) {
                                n = f.toURL() + n.substring(p);
                                url = new URL("tar:" + n);
                            }
                        }
                    } catch (MalformedURLException ex) {
                        throw new IOException("not an URL: " + u);
                    }
                }
                if (!n.startsWith("file:")) throw new IOException("not a local tar: " + u);
                return new Connection(url);
            }
