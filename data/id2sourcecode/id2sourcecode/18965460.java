    public static String call(String url) throws IOException {
        BufferedReader bis = null;
        InputStream is = null;
        try {
            URLConnection connection = new URL(url).openConnection();
            is = connection.getInputStream();
            bis = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = null;
            StringBuffer result = new StringBuffer();
            while ((line = bis.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
