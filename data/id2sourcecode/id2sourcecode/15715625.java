    public void coverjpg(OutputStream out, String id) throws Exception {
        out.write("\r\n".getBytes());
        Release release = MusicBrainz.getRelease(id);
        if (release.asin != null) {
            String url = "http://images.amazon.com/images/P/" + release.asin + ".01._SCLZZZZZZZ_PU_PU-5_.jpg";
            HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
            try {
                Connect.connect(conn.getInputStream(), out, 1024);
            } catch (Exception e) {
                sendResource(out, "dummy.jpg");
            }
        } else {
            sendResource(out, "dummy.jpg");
        }
    }
