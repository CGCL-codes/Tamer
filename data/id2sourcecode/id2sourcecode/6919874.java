                public void operationComplete(ChannelFuture future) {
                    if (future.isSuccess()) {
                        mirrorBinaryData(bufferedMessage, pnlSentData);
                    } else {
                        pnlSystemLog.addLog("Some data tranmission error while data sending to client number: " + future.getChannel().getId() + " occure.", new MessageParameters(MessageParameters.messageType.system, MessageParameters.textStyle.error));
                    }
                }
