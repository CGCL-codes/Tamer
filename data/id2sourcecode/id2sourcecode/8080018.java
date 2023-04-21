    public static String getInstanceMetadata(String key) throws IOException {
        int retries = 0;
        String value = null;
        while (true) {
            try {
                URL url = new URL("http://169.254.169.254/latest/meta-data/" + key);
                value = new BufferedReader(new InputStreamReader(url.openStream())).readLine();
                return value;
            } catch (IOException ex) {
                if (retries == 5) {
                    logger.debug("Problem getting instance data, retries exhausted...");
                    logger.debug("value = " + value);
                    return null;
                } else {
                    logger.debug("Problem getting instance data, retrying...");
                    try {
                        Thread.sleep((int) Math.pow(2.0, retries) * 1000);
                    } catch (InterruptedException e) {
                    }
                    retries++;
                }
            }
        }
    }
