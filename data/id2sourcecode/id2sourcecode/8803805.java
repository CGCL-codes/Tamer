    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
        logger.debug("channelClosed.");
        Session session = NettySession.getInstance(e.getChannel());
        if (session.getAttribute(SessionKey.LOGIN_KEY) != null) {
            NettyMessageServer.allChannelsGroup.remove(e.getChannel());
            Boolean removeMem = (Boolean) session.getAttribute(SessionKey.REMOVE_MEM_INFO);
            removeMem = (removeMem == null) ? true : removeMem;
            commonService.leaveGame(session, removeMem);
            logger.debug("Remove user session info.");
        }
    }
