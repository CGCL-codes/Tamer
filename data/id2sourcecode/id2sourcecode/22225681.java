            @Override
            public boolean read() {
                Object msg = null;
                Exception exception = null;
                try {
                    msg = getChannel().receive(0);
                } catch (ChannelNotConnectedException e) {
                    return false;
                } catch (ChannelClosedException e) {
                    return false;
                } catch (TimeoutException e) {
                    exception = e;
                }
                if (exception != null) {
                    JGroupsDataSession data = (JGroupsDataSession) lookupDataSession(getGroup());
                    data.notifyExceptionListeners(new JGCSException("Could not deliver message.", exception));
                    return true;
                } else if (msg == null) return true;
                if (msg instanceof View) {
                    JGroupsControlSession control = (JGroupsControlSession) lookupControlSession(getGroup());
                    control.jgroupsViewAccepted((View) msg);
                } else if (msg instanceof SuspectEvent) {
                    JGroupsControlSession control = (JGroupsControlSession) lookupControlSession(getGroup());
                    control.jgroupsSuspect((Address) ((SuspectEvent) msg).getMember());
                } else if (msg instanceof BlockEvent) {
                    JGroupsControlSession control = (JGroupsControlSession) lookupControlSession(getGroup());
                    control.jgroupsBlock();
                } else if (msg instanceof org.jgroups.Message) {
                    JGroupsDataSession data = (JGroupsDataSession) lookupDataSession(getGroup());
                    JGroupsMessage message = new JGroupsMessage();
                    byte[] jgroupsBuffer = ((org.jgroups.Message) msg).getBuffer();
                    if (jgroupsBuffer == null) return true;
                    message.setPayload(jgroupsBuffer);
                    message.setSenderAddress((IpAddress) ((org.jgroups.Message) msg).getSrc());
                    Object cookie = data.notifyMessageListeners(message);
                    if (cookie != null) {
                        data.notifyServiceListeners(cookie, new JGroupsService("seto_total_order"));
                        data.notifyServiceListeners(cookie, new JGroupsService("regular_total_order"));
                        data.notifyServiceListeners(cookie, new JGroupsService("uniform_total_order"));
                    }
                } else {
                    JGroupsDataSession data = (JGroupsDataSession) lookupDataSession(getGroup());
                    data.notifyExceptionListeners(new JGCSException("Received unknown message type: " + msg));
                }
                return true;
            }
