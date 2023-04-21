    public static String wget(URL url, boolean post, String... post_data) {
        try {
            URLConnection urlcon = url.openConnection();
            if (post) {
                String msg = "";
                boolean key = false;
                for (String s : post_data) {
                    msg += URLEncoder.encode(s, "UTF-8");
                    if (key = !key) msg += "="; else msg += "&";
                }
                urlcon.setDoOutput(true);
                urlcon.getOutputStream().write(msg.getBytes());
            }
            InputStream urlin = urlcon.getInputStream();
            String data = "";
            int len;
            byte[] buffer = new byte[1023];
            while ((len = urlin.read(buffer)) >= 0) {
                data += new String(buffer, 0, len);
            }
            return data;
        } catch (Exception ex) {
            System.err.println("[" + url + "]:");
            ex.printStackTrace();
            return null;
        }
    }
