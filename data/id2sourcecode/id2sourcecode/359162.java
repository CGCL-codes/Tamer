    public static String execute(String type, String method, Param[] params) {
        String url = getURL(type, method, params);
        note("url:" + url);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            if (in != null) in.close();
            String str = sb.toString();
            String target = "id=\"result\"";
            int itarget = str.indexOf(target);
            if (itarget == -1) return null;
            int igt = str.indexOf(">", itarget);
            if (igt == -1) return null;
            int ilt = str.indexOf("<", igt);
            if (ilt == -1) return null;
            return str.substring(igt + 1, ilt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
