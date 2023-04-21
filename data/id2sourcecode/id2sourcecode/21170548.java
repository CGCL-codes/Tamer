    public static void main(String[] args) {
        try {
            URL url = new URL("http://wwwtest.hlcl.com/feeds/en/news/rate_amendments.rss");
            URLConnection conn = url.openConnection();
            Map<String, List<String>> headerFields = conn.getHeaderFields();
            for (String name : headerFields.keySet()) {
                System.out.println(name + ":" + headerFields.get(name).get(0));
            }
            InputStream in = conn.getInputStream();
            int c;
            while ((c = in.read()) > 0) {
                System.out.print((char) c);
            }
            in.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
