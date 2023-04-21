    private static void loadUrlStream(List<Class<? extends Annotation>> annotations, URL url) throws IOException {
        InputStream stream = url.openStream();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                annotations.add(loadClass(line));
            }
        } finally {
            stream.close();
        }
    }
