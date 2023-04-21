    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent me) throws Exception {
        HttpRequest hr = (HttpRequest) me.getMessage();
        final NettyHttpRequest nhr = new NettyHttpRequest(hr);
        DefaultHttpResponse resp = new DefaultHttpResponse(hr.getProtocolVersion(), HttpResponseStatus.OK);
        boolean close = !HttpHeaders.isKeepAlive(hr);
        resp.setHeader(HttpHeaders.Names.CONNECTION, close ? HttpHeaders.Values.CLOSE : HttpHeaders.Values.KEEP_ALIVE);
        resp.setHeader(HttpHeaders.Names.DATE, java.text.DateFormat.getDateInstance().format(System.currentTimeMillis()));
        RenderContext rc = new RenderContext(nhr, resp);
        String host = hr.getHeader(HttpHeaders.Names.HOST);
        Site site = this.siteManager.getSite(host);
        rc.site = site;
        rc.setDateField(HttpHeaders.Names.DATE, System.currentTimeMillis());
        try {
            PathBag f = site.getPathBag(rc);
            if (f == null) {
                rc.resp.setStatus(HttpResponseStatus.NOT_FOUND);
                f = site.getFragmentBag("/_404");
                if (f != null) {
                    f.renderPath(rc);
                }
            } else {
                long lmd = rc.getDateField(HttpHeaders.Names.IF_MODIFIED_SINCE);
                long pl = f.lastModified(rc);
                if (lmd > 0) {
                    if (pl > 0) {
                        if (pl > lmd + 1000) {
                            rc.setDateField(HttpHeaders.Names.LAST_MODIFIED, pl);
                            f.renderPath(rc);
                        } else {
                            rc.resp.setStatus(HttpResponseStatus.NOT_MODIFIED);
                        }
                    } else {
                        f.renderPath(rc);
                    }
                } else {
                    if (pl > 0) {
                        rc.setDateField(HttpHeaders.Names.LAST_MODIFIED, pl);
                    }
                    f.renderPath(rc);
                }
            }
        } catch (Throwable t) {
            rc.resp.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
            PathBag f = site.getFragmentBag("/_500");
            if (f != null) {
                f.renderPath(rc);
            } else {
                String s = Strings.throwableToString(t);
                rc.write("<pre>");
                rc.write(s);
                rc.write("</pre>");
            }
        }
        rc.finishRender();
        ChannelFuture cf = me.getChannel().write(resp);
        if (close) {
            cf.addListener(ChannelFutureListener.CLOSE);
        }
    }
