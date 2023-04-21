    public static String shorten(String longUrl) {
        String result = new String();
        GsonGooGl gsonGooGl = new GsonGooGl(longUrl);
        try {
            URL url = new URL(URL_GOOGL_SERVICE);
            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json");
            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());
            String content = gson.toJson(gsonGooGl);
            printout.writeBytes(content);
            printout.flush();
            printout.close();
            DataInputStream input = new DataInputStream(urlConn.getInputStream());
            Scanner sc = new Scanner(input);
            while (sc.hasNext()) {
                result += sc.next();
            }
            GooGlResult gooGlResult = gson.fromJson(result, GooGlResult.class);
            return gooGlResult.getId();
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }
