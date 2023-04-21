    public static String[] getProviderNames(Class<? extends Object> clazz, ClassLoader loader) throws IOException {
        assert clazz != null;
        assert loader != null;
        final LinkedList<String> names = new LinkedList<String>();
        final Enumeration<URL> urls = loader.getResources("META-INF/services/" + clazz.getName());
        while (urls.hasMoreElements()) {
            final URL url = urls.nextElement();
            InputStream urlIS = null;
            try {
                urlIS = url.openStream();
                final BufferedReader reader = new BufferedReader(new InputStreamReader(urlIS, "UTF-8"));
                String line = reader.readLine();
                while (line != null) {
                    final StringTokenizer lineTokens = new StringTokenizer(line);
                    boolean lineDone = false;
                    while (lineTokens.hasMoreTokens() && !lineDone) {
                        final String lineToken = lineTokens.nextToken();
                        if (lineToken.charAt(0) != '#') {
                            names.add(lineToken);
                        } else {
                            lineDone = true;
                        }
                    }
                    line = reader.readLine();
                }
            } finally {
                urlIS = Closeables.saveClose(urlIS);
            }
        }
        return names.toArray(new String[names.size()]);
    }
