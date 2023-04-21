    @Listener("/service/comment/get")
    public void processStream(final ServerSession remote, final ServerMessage.Mutable message) {
        final Map<String, Object> output = new HashMap<String, Object>();
        try {
            log.debug("CommentsStreamService............");
            List<CommentBean> comments = getCommentService().getCommentsbyUser(20, 0);
            log.debug("CommentsStreamService.comments size .." + comments.size());
            output.put("comments", JSONUtils.convertObjectToJsonString(comments));
        } catch (Exception e) {
            output.put("comments", ListUtils.EMPTY_LIST);
            log.fatal("cometd: username invalid " + e);
        }
        remote.deliver(getServerSession(), message.getChannel(), output, null);
    }
