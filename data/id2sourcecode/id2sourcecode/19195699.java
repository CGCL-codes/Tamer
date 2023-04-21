    public static void main(String[] args) {
        Collection<String> c = new ArrayList<String>();
        c.add("ABC");
        String urlString = "http://jp.youtube.com/watch?v=VsJBGWl79xU";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        HttpURLConnection con = null;
        BufferedReader bis = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "");
            con.setInstanceFollowRedirects(true);
            InputStream is = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "Shift_JIS");
            bis = new BufferedReader(isr);
            String line = null;
            System.out.println("Contents");
            while ((line = bis.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
