        public ServerInfo call() throws Exception {
            QueryAgent agent = new QueryAgent();
            server.setLastChecked(new Date());
            server.setStats(new ServerStats());
            try {
                agent.connect(server.getHostname());
                QueryInfo info = agent.getInfo();
                server.getStats().update(info);
                server.setVersion(info.getVersion());
                server.setLastOnline(server.getLastChecked());
                if (server.getChannels() == null) {
                    server.setChannels(info.getChannels());
                } else {
                    server.getChannels().clear();
                    server.getChannels().addAll(info.getChannels());
                }
                server.setPlayers(info.getPlayers());
                if (server.getStats().getActivePlayerCount() > server.getMaxActivePlayerCount()) {
                    server.setMaxActivePlayerCount(server.getStats().getActivePlayerCount());
                    server.setMaxActivePlayerDate(server.getLastChecked());
                }
                if (server.getStats().getPlayerCount() > server.getMaxPlayerCount()) {
                    server.setMaxPlayerCount(server.getStats().getPlayerCount());
                    server.setMaxPlayerDate(server.getLastChecked());
                }
                if (server.getStats().getActivePlayerCount() > 0) {
                    server.setLastActive(server.getLastChecked());
                }
                if (server.getStats().getPlayerCount() > 0) {
                    server.setLastPopulated(server.getLastPopulated());
                }
                server.setSpectate(NetworkUtils.isPortOpen(server.getHostname(), 31458));
            } catch (Exception e) {
                log.fine("Unable to check the server " + server.getHostname() + " : " + e.getMessage());
            } finally {
                agent.disconnect();
            }
            return server;
        }
