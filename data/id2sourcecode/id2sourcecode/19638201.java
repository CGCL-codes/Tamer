    private void send100Continue(MessageEvent me) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
        me.getChannel().write(response);
    }
