    protected HttpResponse forwardRequest(HttpRequest request, HttpResponse response, HttpContext context) {
        this.httpproc = procBuilder.build();
        LOG.trace(">> Request URI: " + request.getRequestLine().getUri());
        Object loop = context.getAttribute(CHECK_INFINITE_LOOP);
        if (loop == null) {
            context.setAttribute(CHECK_INFINITE_LOOP, Boolean.TRUE);
        } else {
            throw new ServiceUnavailableException("reverseUrl is infinite loop.");
        }
        Socket outsocket = null;
        ReverseUrl reverseUrl = serviceUrl.getReverseUrl();
        try {
            if (reverseUrl == null) {
                throw new ServiceUnavailableException("reverseUrl is null.");
            }
            context.setAttribute("reverseUrl", reverseUrl);
            ReverseUtils.setXForwardedFor(request, context);
            outsocket = socketFactory.createSocket();
            InetAddress remoteAddress = InetAddress.getByName(reverseUrl.getTargetAddress().getHostName());
            InetSocketAddress remote = new InetSocketAddress(remoteAddress, reverseUrl.getTargetAddress().getPort());
            socketFactory.connectSocket(outsocket, remote, null, builder.buildParams());
            DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
            conn.bind(outsocket, builder.buildParams());
            if (LOG.isTraceEnabled()) {
                LOG.trace("Outgoing connection to " + outsocket.getInetAddress());
                LOG.trace("request: " + request);
            }
            ReverseHttpRequest targetRequest = null;
            if (request instanceof HttpEntityEnclosingRequest) {
                targetRequest = new ReverseHttpEntityEnclosingRequest(request, context, reverseUrl);
            } else {
                URL url = reverseUrl.getReverseUrl(request.getRequestLine().getUri());
                if (url == null) {
                    throw new NotFoundException("url is null.");
                }
                BasicRequestLine line = new BasicRequestLine(request.getRequestLine().getMethod(), url.toString(), request.getRequestLine().getProtocolVersion());
                targetRequest = new ReverseHttpRequest(line, reverseUrl);
                targetRequest.setRequest(request, context);
            }
            reverseUrl.countUp();
            ReverseUtils.setReverseProxyAuthorization(targetRequest, context, proxyAuthorizationHeader);
            try {
                httpexecutor.preProcess(targetRequest, httpproc, context);
                HttpResponse targetResponse = httpexecutor.execute(targetRequest, conn, context);
                httpexecutor.postProcess(targetResponse, httpproc, context);
                return targetResponse;
            } finally {
                reverseUrl.countDown();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(">> " + reverseUrl.getReverse() + ", connections=" + reverseUrl.getActiveConnections());
                }
            }
        } catch (SocketException e) {
            throw new ServiceUnavailableException(BasicHttpStatus.SC_GATEWAY_TIMEOUT.getReasonPhrase() + " URL=" + reverseUrl.getReverse());
        } catch (RuntimeException e) {
            handleException(request, response, e);
            return response;
        } catch (Exception e) {
            handleException(request, response, e);
            return response;
        }
    }
