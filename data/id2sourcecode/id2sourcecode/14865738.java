    public StringBuffer transmit(String input) throws Exception {
        if (!(headList != null && headList.size() > 0) || "".equals(urlString)) {
            return null;
        }
        StringBuffer returnMessage = new StringBuffer();
        final String boundary = String.valueOf(System.currentTimeMillis());
        URL url = null;
        URLConnection conn = null;
        BufferedReader br = null;
        DataOutputStream dos = null;
        try {
            url = new URL(baseURL, "/" + projectName + urlString);
            conn = url.openConnection();
            ((HttpURLConnection) conn).setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Cookie", (String) headList.get(0));
            conn.setRequestProperty("Content-Type", "multipart/form-data, boundary=" + "---------------------------" + boundary);
            if (input != null) {
                String auth = "Basic " + new sun.misc.BASE64Encoder().encode(input.getBytes());
                conn.setRequestProperty("Authorization", auth);
            }
            dos = new DataOutputStream(conn.getOutputStream());
            dos.write((starter + boundary + returnChar).getBytes());
            for (int i = 0; i < txtList.size(); i++) {
                HtmlFormText htmltext = (HtmlFormText) txtList.get(i);
                dos.write(htmltext.getTranslated());
                if (i + 1 < txtList.size()) {
                    dos.write((starter + boundary + returnChar).getBytes());
                } else if (fileList.size() > 0) {
                    dos.write((starter + boundary + returnChar).getBytes());
                }
            }
            for (int i = 0; i < fileList.size(); i++) {
                HtmlFormFile htmlfile = (HtmlFormFile) fileList.get(i);
                dos.write(htmlfile.getTranslated());
                if (i + 1 < fileList.size()) {
                    dos.write((starter + boundary + returnChar).getBytes());
                }
            }
            dos.write((starter + boundary + "--" + returnChar).getBytes());
            dos.flush();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String tempstr;
            int line = 0;
            while (null != ((tempstr = br.readLine()))) {
                returnMessage.append("\n" + tempstr);
            }
            txtList.clear();
            fileList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (Exception e) {
            }
            try {
                br.close();
            } catch (Exception e) {
            }
        }
        return returnMessage;
    }
