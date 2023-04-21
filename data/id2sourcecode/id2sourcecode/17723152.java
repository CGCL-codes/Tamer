    private byte[] receive(URL cdp) {
        HttpClient client = getClientConnection();
        byte[] retVal = null;
        HttpMethodBase method = new GetMethod(cdp.toExternalForm());
        try {
            client.executeMethod(method);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            InputStream is;
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                is = method.getResponseBodyAsStream();
                int lido;
                byte[] lcrBuffer = new byte[MAX_READ];
                while ((lido = is.read(lcrBuffer)) > 0) bos.write(lcrBuffer, 0, lido);
                retVal = bos.toByteArray();
            } else throw new CRLHttpException(Bundle.getInstance().getResourceString(this, "CRL_HTTP_RETURN_ERROR").replace("[HTTP_CODE]", String.valueOf(method.getStatusCode())).replace("[URL]", cdp.toExternalForm()));
        } catch (HttpException e) {
            throw new CRLHttpException(Bundle.getInstance().getResourceString(this, "CRL_HTTP_ERROR").replace("[URL]", cdp.toExternalForm()));
        } catch (IOException e) {
            throw new CRLHttpException(Bundle.getInstance().getResourceString(this, "CRL_HTTP_ERROR").replace("[URL]", cdp.toExternalForm()));
        }
        return retVal;
    }
