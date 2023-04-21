    public final void service(final ChannelHandlerContext chc, final MessageEvent evt, final byte[] post) throws Exception {
        org.jboss.netty.handler.codec.http.HttpRequest request = (org.jboss.netty.handler.codec.http.HttpRequest) evt.getMessage();
        if (log.isDebugEnabled()) {
            String uri = request.getUri();
            log.debug("处理请求[method:{},uri:{}]", request.getMethod(), uri);
        }
        TimeSpan ts = new TimeSpan().start();
        Response resp = new HttpResponse(evt.getChannel(), chc);
        chc.setAttachment(resp);
        Invoke invoke = context.getInvoke();
        try {
            Request req = HttpRequest.create(request, post, evt.getChannel());
            context.setRequest(req);
            String path = req.getPath();
            ActionInfo action = context.getAction(path);
            if (null == action) {
                if (!disposeStaticFile(path, resp)) {
                    resp.sendError(HttpResponseStatus.NOT_FOUND);
                    log.debug("文件未找到path:{}", path);
                }
            } else {
                WebRequestEvent webEvt = context.fireRequestStartEvent(req);
                try {
                    Object result = invoke.invoke(path, context.getIocFactory(), req, resp);
                    ViewHandler viewHandler = action.getViewHandler();
                    viewHandler.doView(action, result, req, resp);
                } finally {
                    context.fireRequestEndEvent(webEvt);
                }
            }
        } catch (FileNotFoundException e) {
            resp.sendError(HttpResponseStatus.NOT_FOUND);
            log.error("文件没找到" + e.getMessage(), e);
        } catch (Exception e) {
            resp.sendError(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            log.error("服务器错误", e);
        }
        context.clearRequest();
        resp.flushAndClose();
        log.debug("请求处理结束,耗时[{}]", ts.end());
    }
