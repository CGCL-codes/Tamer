    private void sendHttpResponse(ChannelHandlerContext aCtx, HttpRequest aReq, HttpResponse aResp) {
        if (aResp.getStatus().getCode() != 200) {
            aResp.setContent(ChannelBuffers.copiedBuffer(aResp.getStatus().toString(), CharsetUtil.UTF_8));
            setContentLength(aResp, aResp.getContent().readableBytes());
        }
        ChannelFuture lCF = aCtx.getChannel().write(aResp);
        if (!isKeepAlive(aReq) || aResp.getStatus().getCode() != 200) {
            lCF.addListener(ChannelFutureListener.CLOSE);
        }
    }
