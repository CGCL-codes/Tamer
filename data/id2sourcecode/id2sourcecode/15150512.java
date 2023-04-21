    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        isCurrentRequestXml = false;
        status = HttpResponseStatus.OK;
        try {
            if (DbConstant.admin.isConnected) {
                this.dbSession = new DbSession(DbConstant.admin, false);
                DbAdmin.nbHttpSession++;
                this.isPrivateDbSession = true;
            }
        } catch (GoldenGateDatabaseNoConnectionError e1) {
            logger.warn("Use default database connection");
            this.dbSession = DbConstant.admin.session;
        }
        HttpRequest request = this.request = (HttpRequest) e.getMessage();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        uriRequest = queryStringDecoder.getPath();
        logger.debug("Msg: " + uriRequest);
        if (uriRequest.contains("gre/") || uriRequest.contains("img/") || uriRequest.contains("res/")) {
            HttpWriteCacheEnable.writeFile(request, e.getChannel(), Configuration.configuration.httpBasePath + uriRequest, "XYZR66NOSESSION");
            return;
        }
        char cval = 'z';
        long nb = LIMITROW;
        if (uriRequest.equalsIgnoreCase("/active")) {
            cval = '0';
        } else if (uriRequest.equalsIgnoreCase("/error")) {
            cval = '1';
        } else if (uriRequest.equalsIgnoreCase("/done")) {
            cval = '2';
        } else if (uriRequest.equalsIgnoreCase("/all")) {
            cval = '3';
        } else if (uriRequest.equalsIgnoreCase("/status")) {
            cval = '4';
        } else if (uriRequest.equalsIgnoreCase("/statusxml")) {
            cval = '5';
            nb = 0;
            isCurrentRequestXml = true;
        }
        if (request.getMethod() == HttpMethod.GET) {
            params = queryStringDecoder.getParameters();
        } else if (request.getMethod() == HttpMethod.POST) {
            ChannelBuffer content = request.getContent();
            if (content.readable()) {
                String param = content.toString(GgStringUtils.UTF8);
                queryStringDecoder = new QueryStringDecoder("/?" + param);
            } else {
                responseContent.append(REQUEST.index.readFileUnique(this));
                writeResponse(e);
                return;
            }
            params = queryStringDecoder.getParameters();
        }
        boolean getMenu = (cval == 'z');
        boolean extraBoolean = false;
        if (!params.isEmpty()) {
            if (getMenu) {
                String info = getTrimValue(sINFO);
                if (info != null) {
                    getMenu = false;
                    cval = info.charAt(0);
                } else {
                    getMenu = true;
                }
            }
            String snb = getTrimValue(sNB);
            if (snb != null) {
                nb = Long.parseLong(snb);
            }
            String sdetail = getTrimValue(sDETAIL);
            if (sdetail != null) {
                if (Integer.parseInt(sdetail) > 0) extraBoolean = true;
            }
        }
        if (getMenu) {
            responseContent.append(REQUEST.index.readFileUnique(this));
        } else {
            switch(cval) {
                case '0':
                    active(ctx, (int) nb);
                    break;
                case '1':
                    error(ctx, (int) nb);
                    break;
                case '2':
                    done(ctx, (int) nb);
                    break;
                case '3':
                    all(ctx, (int) nb);
                    break;
                case '4':
                    status(ctx, (int) nb);
                    break;
                case '5':
                    statusxml(ctx, nb, extraBoolean);
                    break;
                default:
                    responseContent.append(REQUEST.index.readFileUnique(this));
            }
        }
        writeResponse(e);
    }
