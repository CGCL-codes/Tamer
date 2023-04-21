    private static void formpost(String formurl, ArkRequest arkreq, long rank) {
        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        Channel channel = future.awaitUninterruptibly().getChannel();
        if (!future.isSuccess()) {
            future.getCause().printStackTrace();
            bootstrap.releaseExternalResources();
            return;
        }
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, formurl);
        for (Entry<String, String> entry : headers) {
            request.setHeader(entry.getKey(), entry.getValue());
        }
        HttpPostRequestEncoder bodyRequestEncoder = null;
        try {
            bodyRequestEncoder = new HttpPostRequestEncoder(factory, request, (arkreq == ArkRequest.PostUpload));
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ErrorDataEncoderException e) {
            e.printStackTrace();
        }
        try {
            bodyRequestEncoder.addBodyAttribute(ArkArgs.LEG.name(), "111");
            bodyRequestEncoder.addBodyAttribute(ArkArgs.STO.name(), "-9151313343288442623");
            bodyRequestEncoder.addBodyAttribute(ArkArgs.DID.name(), Long.toString(rank));
            bodyRequestEncoder.addBodyAttribute(ArkArgs.CTYPE.name(), "text/plain");
            bodyRequestEncoder.addBodyAttribute(ArkArgs.META.name(), textArea);
            if (arkreq == ArkRequest.PostUpload) {
                bodyRequestEncoder.addBodyAttribute(ArkArgs.REQTYPE.name(), ArkRequest.PostUpload.name());
                bodyRequestEncoder.addBodyFileUpload(ArkArgs.FILEDOC.name(), file, "application/x-zip-compressed", false);
            } else {
                bodyRequestEncoder.addBodyAttribute(ArkArgs.REQTYPE.name(), ArkRequest.Post.name());
                bodyRequestEncoder.addBodyAttribute(ArkArgs.FILENAME.name(), file.getAbsolutePath());
            }
            bodyRequestEncoder.addBodyAttribute("Send", "Send");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (ErrorDataEncoderException e) {
            e.printStackTrace();
        }
        try {
            request = bodyRequestEncoder.finalizeRequest();
        } catch (ErrorDataEncoderException e) {
            e.printStackTrace();
        }
        channel.write(request);
        if (bodyRequestEncoder.isChunked()) {
            channel.write(bodyRequestEncoder).awaitUninterruptibly();
        }
        channel.getCloseFuture().awaitUninterruptibly();
    }
