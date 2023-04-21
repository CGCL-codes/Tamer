    private void doRegister(ServentRequest request, ServentResponse response) throws ServiceException, IOException {
        byte[] body = request.getBody();
        if ((body == null) || (body.length == 0)) {
            logger.warn("Request body cannot be null");
            response.sendError("Request body cannot be null");
        }
        String userUri = new String(body);
        logger.info("Deploying application " + userUri);
        URL url = new URL(userUri);
        InputStream is = url.openStream();
        servent.deploy(is);
        is.close();
        StringBuffer sb = startSB();
        sb.append("Service Registered");
        endSB(sb);
        response.getOutputStream().write(sb.toString().getBytes());
    }
