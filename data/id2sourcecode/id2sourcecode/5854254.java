    public HttpResponse executeShibboleth(HttpHost target, HttpRequest request) throws IOException, HttpException, InterruptedException {
        HttpResponse response = null;
        request.removeHeaders(HTTP.CONTENT_LEN);
        BasicHttpContext shibContext = new BasicHttpContext();
        HttpResponse responseIdP = super.execute(target, request, shibContext);
        LogUtils.trace(logger, "CookieStore=" + getCookieStore().toString());
        if (isIdPResponse(responseIdP)) {
            IdPResponseParser idpParser = new IdPResponseParser(responseIdP);
            try {
                response = super.execute(target, createSPRequest(idpParser), shibContext);
                if (isShibbolethSPToResourceRedirect(response, shibContext)) response = super.execute(target, request, shibContext);
            } catch (URISyntaxException e) {
                String message = "Error handling request to " + idpParser.getTarget() + ": " + e.getMessage() + "(" + e.getClass().getName() + ")";
                LogUtils.fatal(logger, message);
                throw new HttpException(message);
            }
        } else response = responseIdP;
        LogUtils.trace(logger, "CookieStore2=" + getCookieStore().toString());
        return response;
    }
