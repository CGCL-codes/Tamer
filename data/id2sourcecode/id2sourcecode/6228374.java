    public Object[] connService(String wsdl, String method, Object[] parms) {
        Object[] results = null;
        try {
            URL url = new URL(wsdl);
            HttpURLConnection httpConnection;
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.connect();
            Client client = new Client(httpConnection.getInputStream(), null);
            results = client.invoke(method, parms);
        } catch (MalformedURLException e) {
            System.out.println("service访问路径有误");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("连接service失败，可能是服务器已经关闭");
            e.printStackTrace();
        } catch (Exception e) {
            String[] strError = { e.getMessage() };
            if (results == null) results = strError;
            System.out.println(e.getMessage());
        }
        return results;
    }
