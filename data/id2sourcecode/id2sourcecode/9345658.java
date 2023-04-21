        private Manifest createManifest() {
            URL url = this.codeSource.getLocation();
            InputStream in = null;
            try {
                if ("jar".equals(url.getProtocol())) {
                    URLConnection co = url.openConnection();
                    if (co instanceof JarURLConnection) {
                        return ((JarURLConnection) co).getManifest();
                    }
                }
                in = new URL(url, "META-INF/MANIFEST.MF").openStream();
                Manifest mf = new Manifest(in);
                in.close();
                return mf;
            } catch (IOException ex) {
                try {
                    if (in != null) in.close();
                } finally {
                    return null;
                }
            }
        }
