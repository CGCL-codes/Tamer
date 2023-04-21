    public boolean Write(DroidGuideEntity user_profile) {
        boolean ok = true;
        try {
            URL url = new URL("http://localhost:8080/user_profile/save");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            Field[] fields = UserProfile.class.getDeclaredFields();
            boolean first = true;
            String postdata = "";
            for (int i = 0; i < fields.length; ++i) {
                Field field = fields[i];
                String method_name = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                System.out.println(method_name);
                Method getter = UserProfile.class.getDeclaredMethod(method_name);
                Object raw_value = getter.invoke(user_profile);
                if (raw_value != null) {
                    String value = raw_value.toString();
                    if (value.length() > 0) {
                        conn.addRequestProperty(field.getName(), value);
                        if (first) {
                            postdata += "&";
                        }
                        postdata += field.getName() + "=" + URLEncoder.encode(value, "UTF-8");
                        first = false;
                    }
                }
            }
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.connect();
            System.out.println(conn.getResponseMessage());
            ok = conn.getResponseMessage().contains("OK");
            if (ok) {
                Reader reader = new InputStreamReader(conn.getInputStream());
                char[] buf = new char[1024];
                StringBuilder sb = new StringBuilder();
                while (reader.read(buf) >= 0) {
                    sb.append(buf);
                }
                String key = sb.toString();
                System.out.println(key);
                user_profile.setKey(key);
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }
