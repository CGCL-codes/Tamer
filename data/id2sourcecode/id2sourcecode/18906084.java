    public static HttpResponse execute(HttpRequest req, HttpClientConnection conn, HttpHost target, HttpRequestExecutor exec, HttpProcessor proc, HttpParams params, HttpContext ctxt) throws Exception {
        ctxt.setAttribute(ExecutionContext.HTTP_CONNECTION, conn);
        ctxt.setAttribute(ExecutionContext.HTTP_TARGET_HOST, target);
        ctxt.setAttribute(ExecutionContext.HTTP_REQUEST, req);
        req.setParams(new DefaultedHttpParams(req.getParams(), params));
        exec.preProcess(req, proc, ctxt);
        HttpResponse rsp = exec.execute(req, conn, ctxt);
        rsp.setParams(new DefaultedHttpParams(rsp.getParams(), params));
        exec.postProcess(rsp, proc, ctxt);
        return rsp;
    }
