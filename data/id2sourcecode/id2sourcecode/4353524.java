    public Object[] connService(String wsdl, String method, Object[] parms) {
        Object[] results = null;
        try {
            URL url = new URL(wsdl);
            HttpURLConnection httpConnection;
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();
            Client client = new Client(url);
            results = client.invoke(method, parms);
        } catch (MalformedURLException e) {
            System.out.println("service访问路径有误");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("连接service失败，可能是服务器已经关闭");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("错误：接口调用错误");
            e.printStackTrace();
        }
        return results;
    }
