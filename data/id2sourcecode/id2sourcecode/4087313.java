    public static String download(String urlStr, File destFile) {
        String result = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            URL url = new URL(urlStr);
            in = new BufferedInputStream(url.openStream());
            out = new BufferedOutputStream(new FileOutputStream(destFile), 1024);
            byte[] buffer = new byte[1024];
            int num = -1;
            while ((num = in.read(buffer)) > 0) {
                out.write(buffer, 0, num);
            }
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
