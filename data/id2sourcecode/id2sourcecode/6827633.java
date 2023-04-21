    boolean handleHandeshake(MessageImpl request, MessageImpl response) {
        if (request.getChannel().equals((Bayeux.META_HANDSHAKE))) {
            String version = request.getVersion();
            String[] supportedConnectionTypes = request.getSupportedConnectionTypes();
            String minimumVersion = request.getMinimumVersion();
            String id = request.getId();
            if (request.getExt().containsKey("json-comment-filtered")) {
                isJsonCommentFiltered = (Boolean) request.getExt().get("json-comment-filtered");
            }
            response.setVersion("1.0");
            response.setSupportedConnectionTypes(new String[] { "long-polling", "callback-polling" });
            response.setClientId(getId());
            response.setSuccessful(true);
            if (id != null) {
                response.setId(id);
            }
            return true;
        } else {
            System.out.println("UNSUPPORTED CASE");
            return false;
        }
    }
