    public static InputStream getResponseFromNcbi(String url) throws Exception {
        int tries = 0;
        while (true) {
            try {
                Thread.sleep(3000);
                return new URL(url).openStream();
            } catch (Exception e) {
                System.out.println("tries..." + tries);
                tries++;
                if (tries > 4) {
                    throw e;
                }
            }
        }
    }
