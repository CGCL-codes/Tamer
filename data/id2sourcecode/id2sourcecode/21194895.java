    @Override
    public void perform(WebContext context) {
        HttpRequest request = context.getRequest();
        String channelId = request.getHeaders().get(WebContext.X_ANNONE_CHANNEL_ID);
        Channel channel = context.getServer().getChannels().get(channelId);
        if (channel == null) throw new HttpBadRequestException("Missing channel id.");
        HttpResponse response = context.getResponse();
        StringBuilder b = new StringBuilder();
        Formatter f = new Formatter(b);
        f.format("{var e = new Edit('E1');%n");
        f.format("e.setCaption('Hello, world! In the great beyond...');%n");
        f.format("e.getValue().setChoices([[1, 'Hi'], [2, 'Ciao'], [3, 'Bau']]);%n");
        f.format("$('content').insert(e);}%n");
        f.format("{var e = new Edit('E2');%n");
        f.format("e.setCaption('<strong>Open process</strong> any interested person can participate in the work, know what is being decided, and make his or her voice heard on the issue. Part of this principle is our commitment to making our documents, our WG mailing lists, our attendance lists, and our meeting minutes publicly available on the Internet.');%n");
        f.format("e.setWidth(24);%n");
        f.format("e.getValue().setType('number');%n");
        f.format("$('content').insert(e);}%n");
        response.setStatusCode(200);
        response.setContent(new HttpTextContent(b.toString(), "javascript", request.getPreferredCharset()));
    }
