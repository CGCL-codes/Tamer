    private static boolean check(URL baseURL, String name) {
        URL url = null;
        try {
            url = new URL(baseURL, name + "/front.png");
        } catch (IOException e) {
            return (false);
        }
        try {
            url.openStream();
        } catch (IOException e) {
            return (false);
        }
        return (true);
    }
