    private void scanConnection() {
        if (agentEnrollmentManager.getAgents().size() == 0) {
            return;
        }
        boolean added = false;
        for (EnrolledAgent agent : agentEnrollmentManager.getAgents()) {
            final int connections = agent.connections();
            if (connections >= maxConnectionsForAgent) {
                continue;
            }
            LOG.debug("trying to create new connection for " + agent.getAgent());
            try {
                final SessionHandler handler = sessionFactory.create(agent, newSessionId());
                if (handler == null) {
                    LOG.debug("cannot create");
                } else {
                    final SocketChannel channel = handler.getChannel();
                    if (channel != null) {
                        channel.configureBlocking(false);
                        channel.register(selector, SelectionKey.OP_READ);
                        assignHandler(channel, (SocketSessionHandler) handler);
                    }
                    agent.pushbackSession(handler);
                    added = true;
                }
            } catch (RpsSessionException rse) {
                LOG.warn(rse.getMessage(), rse);
            } catch (IOException ioe) {
                LOG.warn(ioe.getMessage(), ioe);
            }
        }
        if (added) {
            agentEnrollmentManager.notifyConnectedAgents();
        }
    }
