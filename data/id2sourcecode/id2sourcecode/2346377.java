    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = this.request = (HttpRequest) e.getMessage();
        queryStringDecoder = new QueryStringDecoder(request.getUri());
        uriRequest = queryStringDecoder.getPath();
        logger.debug("Msg: " + uriRequest);
        if (uriRequest.contains("gre/") || uriRequest.contains("img/") || uriRequest.contains("res/")) {
            HttpWriteCacheEnable.writeFile(request, e.getChannel(), Configuration.configuration.httpBasePath + uriRequest, R66SESSION);
            return;
        }
        checkSession(e.getChannel());
        if (!authentHttp.isAuthenticated()) {
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
            case CancelRestart:
                responseContent.append(CancelRestart());
                break;
            case Export:
                responseContent.append(Export());
                break;
            case Hosts:
                responseContent.append(Hosts());
                break;
            case index:
                responseContent.append(index());
                break;
            case Listing:
                responseContent.append(Listing());
                break;
            case Logon:
                responseContent.append(index());
                break;
            case Rules:
                responseContent.append(Rules());
                break;
            case System:
                responseContent.append(System());
                break;
            case Transfers:
                responseContent.append(Transfers());
                break;
            default:
                responseContent.append(index());
                break;
        }
        writeResponse(e.getChannel());
    }
