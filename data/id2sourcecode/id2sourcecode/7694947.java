    public static String translate(String txUrl, String directionPrefix, String direction, String srcPrefix, String srcLangString, String startOfResultTag, String endOfResultTag) throws Exception {
        try {
            URLConnection conn;
            URL url = new URL(txUrl);
            String parms = directionPrefix + URLEncoder.encode(direction, "UTF-8");
            parms += srcPrefix + URLEncoder.encode(srcLangString, "UTF-8");
            conn = url.openConnection();
            conn.setDefaultUseCaches(false);
            conn.setDoOutput(true);
            conn.setIfModifiedSince(0);
            OutputStream o = conn.getOutputStream();
            o.write(parms.getBytes());
            o.flush();
            conn.connect();
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            char[] c = new char[4096];
            int i = 1;
            String result = new String("");
            while (i != -1) {
                c[0] = c[1] = '\0';
                i = br.read(c, 0, 4096);
                if (i != -1) result += new String(c, 0, i);
            }
            String search = startOfResultTag;
            String lowerCaseResult = result.toLowerCase();
            int start = lowerCaseResult.indexOf(search.toLowerCase());
            if (start >= 0) {
                int end = lowerCaseResult.indexOf(endOfResultTag.toLowerCase(), start + 1);
                return result.substring(start + search.length(), end).trim();
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
    }
