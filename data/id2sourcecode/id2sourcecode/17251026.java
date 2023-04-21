    @Override
    public void messageReceived(final ChannelHandlerContext channelHandlerContext, final MessageEvent messageEvent) throws Exception {
        if (!(messageEvent.getMessage() instanceof CommandMessage)) {
            channelHandlerContext.sendUpstream(messageEvent);
            return;
        }
        final CommandMessage command = (CommandMessage) messageEvent.getMessage();
        final Op cmd = command.op;
        final int cmdKeysSize = command.keys == null ? 0 : command.keys.size();
        if (this.verbose) {
            final StringBuilder log = new StringBuilder();
            log.append(cmd);
            if (command.element != null) {
                log.append(" ").append(command.element.getKey());
            }
            for (int i = 0; i < cmdKeysSize; i++) {
                log.append(" ").append(command.keys.get(i));
            }
            logger.info(log.toString());
        }
        final Channel channel = messageEvent.getChannel();
        if (cmd == null) {
            handleNoOp(channelHandlerContext, command, channel);
        } else {
            switch(cmd) {
                case GET:
                case GETS:
                    handleGets(channelHandlerContext, command, channel);
                    break;
                case APPEND:
                    handleAppend(channelHandlerContext, command, channel);
                    break;
                case PREPEND:
                    handlePrepend(channelHandlerContext, command, channel);
                    break;
                case DELETE:
                    handleDelete(channelHandlerContext, command, channel);
                    break;
                case DECR:
                    handleDecr(channelHandlerContext, command, channel);
                    break;
                case INCR:
                    handleIncr(channelHandlerContext, command, channel);
                    break;
                case REPLACE:
                    handleReplace(channelHandlerContext, command, channel);
                    break;
                case ADD:
                    handleAdd(channelHandlerContext, command, channel);
                    break;
                case SET:
                    handleSet(channelHandlerContext, command, channel);
                    break;
                case CAS:
                    handleCas(channelHandlerContext, command, channel);
                    break;
                case STATS:
                    handleStats(channelHandlerContext, command, cmdKeysSize, channel);
                    break;
                case VERSION:
                    handleVersion(channelHandlerContext, command, channel);
                    break;
                case QUIT:
                    handleQuit(channel);
                    break;
                case FLUSH_ALL:
                    handleFlush(channelHandlerContext, command, channel);
                    break;
                case VERBOSITY:
                    handleVerbosity(channelHandlerContext, command, channel);
                    break;
                default:
                    throw new UnknownCommandException("unknown command");
            }
        }
    }
