    @Override
    @SuppressWarnings("unchecked")
    public void messageReceived(ChannelHandlerContext channelHandlerContext, MessageEvent messageEvent) throws Exception {
        if (!(messageEvent.getMessage() instanceof CommandMessage)) {
            channelHandlerContext.sendUpstream(messageEvent);
            return;
        }
        CommandMessage<CACHE_ELEMENT> command = (CommandMessage<CACHE_ELEMENT>) messageEvent.getMessage();
        Command cmd = command.cmd;
        int cmdKeysSize = command.keys.size();
        cache.asyncEventPing();
        if (this.verbose) {
            StringBuilder log = new StringBuilder();
            log.append(cmd);
            if (command.element != null) {
                log.append(" ").append(command.element.getKeystring());
            }
            for (int i = 0; i < cmdKeysSize; i++) {
                log.append(" ").append(command.keys.get(i));
            }
            logger.info(log.toString());
        }
        Channel channel = messageEvent.getChannel();
        if (cmd == Command.GET || cmd == Command.GETS) {
            handleGets(channelHandlerContext, command, channel);
        } else if (cmd == Command.SET) {
            handleSet(channelHandlerContext, command, channel);
        } else if (cmd == Command.CAS) {
            handleCas(channelHandlerContext, command, channel);
        } else if (cmd == Command.ADD) {
            handleAdd(channelHandlerContext, command, channel);
        } else if (cmd == Command.REPLACE) {
            handleReplace(channelHandlerContext, command, channel);
        } else if (cmd == Command.APPEND) {
            handleAppend(channelHandlerContext, command, channel);
        } else if (cmd == Command.PREPEND) {
            handlePrepend(channelHandlerContext, command, channel);
        } else if (cmd == Command.INCR) {
            handleIncr(channelHandlerContext, command, channel);
        } else if (cmd == Command.DECR) {
            handleDecr(channelHandlerContext, command, channel);
        } else if (cmd == Command.DELETE) {
            handleDelete(channelHandlerContext, command, channel);
        } else if (cmd == Command.STATS) {
            handleStats(channelHandlerContext, command, cmdKeysSize, channel);
        } else if (cmd == Command.VERSION) {
            handleVersion(channelHandlerContext, command, channel);
        } else if (cmd == Command.QUIT) {
            handleQuit(channel);
        } else if (cmd == Command.FLUSH_ALL) {
            handleFlush(channelHandlerContext, command, channel);
        } else if (cmd == null) {
            handleNoOp(channelHandlerContext, command);
        } else {
            throw new UnknownCommandException("unknown command:" + cmd);
        }
    }
