    public static void main(String[] args) {
        try {
            String temp = "renyanwei";
            for (int i = 1; i < 10; i++) {
                URL url = new URL(URL);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), CHARSET);
                String str = String.format(Locale.CHINA, "login=%s&&passwd=%s&&repasswd=%s&&Prompt=%s&&answer=%s&&email=%s", temp + i, temp + i, temp + i, temp + i, URLEncoder.encode("中文", CHARSET), "ren@ren.com");
                out.write(str);
                out.flush();
                out.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                int lineNum = 1;
                while ((line = reader.readLine()) != null) {
                    ++lineNum;
                    if (lineNum == 174) System.out.println(line);
                }
            }
        } catch (Exception x) {
            System.out.println(x.toString());
        }
    }
