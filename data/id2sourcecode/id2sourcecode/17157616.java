    protected List<String> refresh(URL url) throws IOException {
        List<String> images = new ArrayList<String>(500);
        URLConnection connection = url.openConnection(proxy);
        InputStream stream = connection.getInputStream();
        Reader reader = new InputStreamReader(stream);
        BufferedReader buffer = new BufferedReader(reader);
        String line = null;
        while ((line = buffer.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                images.add(matcher.group(1));
            }
            matcher = null;
        }
        buffer.close();
        reader.close();
        stream.close();
        line = null;
        buffer = null;
        reader = null;
        stream = null;
        connection = null;
        return images;
    }
