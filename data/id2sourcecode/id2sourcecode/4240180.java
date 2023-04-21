    public String[] values() {
        String[] values = null;
        long now = System.currentTimeMillis();
        if (url != null && now > expireTime) {
            InputStream input = null;
            try {
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                input = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                Set<String> readValues = new TreeSet<String>();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("#") || line.length() == 0) continue;
                    readValues.add(line);
                }
                values = readValues.toArray(new String[0]);
                expireTime = System.currentTimeMillis() + longevity;
                logger.debug("Configuration loaded successfully from " + url);
            } catch (IOException e) {
                logger.warn("I/O Error reading from url: " + url);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        logger.warn("Error closing inputstream", e);
                    }
                }
            }
        }
        return values;
    }
