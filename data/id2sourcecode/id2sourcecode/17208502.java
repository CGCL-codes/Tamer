    public Future<Link> beginLink(InetSocketAddress address) {
        final FuturePackage<Link> fp;
        if (address == null) {
            fp = Futures.newFuturePackage();
            fp.getManager().completeFuture(new Link(null));
        } else {
            ChannelFuture cf = cbs.connect(address);
            LinkChannelHandler lch = cf.getChannel().getPipeline().get(LinkChannelHandler.class);
            fp = lch.connectfp;
            cf.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture arg0) throws Exception {
                    if (!arg0.isSuccess()) {
                        fp.getManager().cancelFuture(arg0.getCause());
                    }
                }
            });
        }
        return fp.getFuture();
    }
