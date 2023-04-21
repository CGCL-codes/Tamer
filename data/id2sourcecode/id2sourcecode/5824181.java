    private void dispatch() throws Exception {
        Channel channel = entity.getChannel();
        try {
            container.handle(request, response);
        } catch (Throwable e) {
            channel.close();
        }
    }
