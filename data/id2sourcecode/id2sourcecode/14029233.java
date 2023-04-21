        @Override
        public void run() {
            while (server.isRunning() && (!pingPongMode || checkPong()) && !clientContext.isClientSaidByeBye()) {
                try {
                    final String line = clientContext.getClientEndpoint().readLine();
                    if (server.isRunning()) {
                        if (line != null) {
                            clientContext.getClientEndpoint().read();
                            handleMessage(clientContext, line);
                        } else {
                            LOGGER.debug(String.format("client %s closed connection non-gracefully", clientContext.getClientInetAddress()));
                            server.disposeOfClient(clientContext);
                            break;
                        }
                    }
                } catch (final SocketException e) {
                    if (e.getMessage().equals(ERROR_SOCKET_CLOSED) || e.getMessage().equals(ERROR_RECV_FAILED) || e.getMessage().equals(ERROR_CONNECTION_RESET)) {
                        if (e.getMessage().equals(ERROR_SOCKET_CLOSED)) {
                            final String msg = "tried to read or write to closed socket (was client %s dropped due to PingPong time-out?)";
                            LOGGER.error(String.format(msg, clientContext.getClientInetAddress()));
                        } else {
                            final String msg = "socket in error state [%s] for client %s";
                            LOGGER.error(String.format(msg, e.getMessage(), clientContext.getClientInetAddress()));
                        }
                        server.disposeOfClient(clientContext);
                        break;
                    }
                    LOGGER.error(e.getMessage(), e);
                } catch (final IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
                ServerUtil.defaultSleep();
            }
        }
