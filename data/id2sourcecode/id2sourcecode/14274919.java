    public static void postContent(String urlName) throws Exception {
        URLConnection dbpc = (new URL(urlName)).openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(dbpc.getInputStream()));
    }
