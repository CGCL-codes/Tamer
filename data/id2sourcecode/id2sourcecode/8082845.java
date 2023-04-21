    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = this.request = (HttpRequest) e.getMessage();
        queryStringDecoder = new QueryStringDecoder(request.getUri());
        uriRequest = queryStringDecoder.getPath();
        if (uriRequest.contains("gre/") || uriRequest.contains("img/") || uriRequest.contains("res/")) {
            HttpWriteCacheEnable.writeFile(request, e.getChannel(), FileBasedConfiguration.fileBasedConfiguration.httpBasePath + uriRequest, FTPSESSION);
            return;
        }
        checkSession(e.getChannel());
        if (!authentHttp.isIdentified()) {
            logger.debug("Not Authent: " + uriRequest + ":{}", authentHttp);
            checkAuthent(e);
            return;
        }
        String find = uriRequest;
        if (uriRequest.charAt(0) == '/') {
            find = uriRequest.substring(1);
        }
        find = find.substring(0, find.indexOf("."));
        REQUEST req = REQUEST.index;
        try {
            req = REQUEST.valueOf(find);
        } catch (IllegalArgumentException e1) {
            req = REQUEST.index;
            logger.debug("NotFound: " + find + ":" + uriRequest);
        }
        switch(req) {
            case index:
                responseContent.append(index());
                break;
            case Logon:
                responseContent.append(index());
                break;
            case System:
                responseContent.append(System());
                break;
            case Rule:
                responseContent.append(Rule());
                break;
            case User:
                responseContent.append(User());
                break;
            case Transfer:
                responseContent.append(Transfer());
                break;
            default:
                responseContent.append(index());
                break;
        }
        writeResponse(e.getChannel());
    }
