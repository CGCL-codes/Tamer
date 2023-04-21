    public static ServiceResponse secureInvoke(ServiceContext serviceContext) {
        String targetDomain = serviceContext.getTargetDomain();
        int port = serviceContext.getPort();
        ServiceResponse serviceResponse = null;
        EncodingTypes charset = serviceContext.getCharset();
        try {
            long ioStartTime = System.currentTimeMillis();
            HttpHost targetHost = new HttpHost(targetDomain, port, RequestConstants.HTTPS);
            HttpRequest request = buildHttpRequest(serviceContext);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getConnectionManager().getSchemeRegistry().register(new Scheme(RequestConstants.HTTPS, new CustomSSLSocketFactory(), port));
            HttpResponse response = httpclient.execute(targetHost, request);
            serviceResponse = new ServiceResponse(response, charset);
            long ioEndTime = System.currentTimeMillis();
            System.out.println("Time taken in executing REST: " + (ioEndTime - ioStartTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceResponse;
    }
