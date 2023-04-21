    public static void main(String[] args) {
        StringBuffer a = new StringBuffer();
        a.append("http://www.advanscene.com/offline/version/ADVANsCEne_NDS_S.txt");
        a.insert(7, "ool-ws.appspot.com/");
        final String aa = a.toString();
        System.out.println(a);
        try {
            URL url = new URL(aa.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            int totalSize = conn.getContentLength();
            if (conn.getResponseCode() != 200) {
                System.out.println("服务器响应错误");
                System.exit(-1);
            }
            System.out.println(totalSize);
            int currentSize = 0;
            int size;
            byte[] buf = new byte[1024];
            while ((size = bis.read(buf)) != -1) {
                currentSize += size;
                System.out.println(currentSize * 100 / totalSize + "%");
            }
            bis.close();
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
