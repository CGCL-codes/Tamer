    public void run() throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
        System.out.println("THE HEADERS");
        System.out.println("-----------");
        for (int i = 1; ; ++i) {
            String key;
            String value;
            if ((key = urlc.getHeaderFieldKey(i)) == null) break;
            if ((value = urlc.getHeaderField(i)) == null) break;
            System.out.println("KEY: " + key);
            System.out.println("VALUE: " + value);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
        String line;
        System.out.println("THE CONTENT");
        System.out.println("-----------");
        while ((line = reader.readLine()) != null) System.out.println(line);
    }
