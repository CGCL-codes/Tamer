    public static List<String> readFile(URL url) {
        List<String> result = new ArrayList<String>();
        String s = null;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            do {
                s = in.readLine();
                if (s != null) result.add(s);
            } while (s != null);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to read \"{0}\".  Returning null.", url);
            return null;
        } finally {
            closeQuietly(in);
        }
        return result;
    }
