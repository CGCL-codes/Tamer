            public void channelShunned() {
                log.log(LogService.LOG_INFO, "shunned " + topic);
                bus.getChannel().disconnect();
                try {
                    bus.getChannel().connect(topic);
                } catch (ChannelClosedException e) {
                    log.log(LogService.LOG_ERROR, "Error reconnecting to " + topic, e);
                } catch (ChannelException e) {
                    log.log(LogService.LOG_ERROR, "Error reconnecting to " + topic, e);
                }
            }
