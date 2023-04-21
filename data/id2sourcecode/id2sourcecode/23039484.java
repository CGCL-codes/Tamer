    private static void outputResource(URL url) {
        try {
            Reader r = new InputStreamReader(url.openStream());
            StringBuilder str = new StringBuilder();
            char[] buffer = new char[1024];
            for (int len = r.read(buffer); len != -1; len = r.read(buffer)) {
                str.append(buffer, 0, len);
            }
            System.out.println(str.toString());
        } catch (IOException e) {
            logger.warning("Error: " + e.getMessage());
        }
    }
