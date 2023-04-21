    private String saveState(int[] memory) {
        String urls = prefixUrl;
        for (int i = 0, n = store.size(); i < n; i += 2) {
            String name = (String) store.get(i);
            int[] arr = (int[]) store.get(i + 1);
            if (i > 0) urls += "&";
            urls += name + "=" + toHex(memory, arr[0], arr[1]);
        }
        System.out.println("URL: " + urls);
        try {
            URL url = getClass().getResource(urls);
            URLConnection urlc = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) urlc;
            InputStream is = httpConnection.getInputStream();
            System.out.println("Read back:");
            int c;
            while ((c = is.read()) != -1) {
                System.out.print((char) c);
            }
            System.out.println("----------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
