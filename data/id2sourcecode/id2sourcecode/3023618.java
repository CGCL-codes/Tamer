    public static void getProfilePic(Application application) {
        try {
            String queryparms = "access_token=" + URLEncoder.encode(((PossessedApplication) application).getAuthCode(), "UTF-8");
            URL url = null;
            try {
                url = new URL("https://graph.facebook.com/me/picture?" + queryparms);
            } catch (RuntimeException err) {
                err.printStackTrace();
            }
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            Map hf = conn.getHeaderFields();
            Set keys = hf.keySet();
            for (Object o : keys) {
                System.out.println(o);
            }
            System.out.println(conn.getURL());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
