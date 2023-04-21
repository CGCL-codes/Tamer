    public static void expand(Writer writer, URL url, Type model) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                writer.write(expand(line, model));
            }
            reader.close();
        } catch (Exception e) {
            new CoreProxy().logError("Failed to read URL: '" + url + "'", e);
        }
    }
