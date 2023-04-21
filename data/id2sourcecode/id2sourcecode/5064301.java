    public WebHttpResponse execute(HttpRequest request, WebHttpResponse response) throws Exception {
        String strHost = request.getHost();
        int port = request.getPort();
        if (port != iLastPort || strLastHost == null || strHost.regionMatches(0, strLastHost, 0, strLastHost.length()) != true || sslSocket == null || sslSocket.isClosed()) {
            if (connectToSecureServer(response, strHost, port) == false) return response;
        }
        if (httpRequestHandler.sendRequest(request, to_server) == false) {
            if (connectToSecureServer(response, strHost, port) == false) {
                response.setCode(-1);
                return response;
            } else {
                if (httpRequestHandler.sendRequest(request, to_server) == false) {
                    response.setCode(-1);
                    return response;
                }
            }
        }
        httpRequestHandler.getServerResponse(request, response, from_server, socketBufSize);
        if (response.getCode() == 0) {
            if (connectToSecureServer(response, strHost, port) == true) {
                if (httpRequestHandler.sendRequest(request, to_server) == true) {
                    httpRequestHandler.getServerResponse(request, response, from_server, socketBufSize);
                }
            }
        }
        if (response.getShouldCloseSocket() == true) strLastHost = null;
        return response;
    }
