    public synchronized void processIncomingPacket() {
        if (hasInput()) {
            try {
                donkeyServer.setAlive();
                state = DonkeyProtocol.SERVER_CONNECTED;
                inPacket = new DonkeyScannedPacket(getNextPacket());
                byte[] fileHash = null;
                SharesManager shares;
                SharedFile sf = null;
                Vector sharedFiles;
                LinkedList blockList = null;
                int command = Convert.byteToInt(inPacket.getCommandId());
                switch(command) {
                    case OP_SERVERMESSAGE:
                        {
                            String message = inPacket.getServerMessage();
                            log.info(getConnectionNumber() + " Server Message: \"" + message + "\"");
                            if (!donkeyServer.isStaticDns()) {
                                donkeyServer.testForDns(message);
                            }
                            break;
                        }
                    case OP_SERVERSTATUS:
                        {
                            log.info(getConnectionNumber() + " Server status: " + inPacket.getStatus()[0] + " Users online, sharing " + inPacket.getStatus()[1] + " files...");
                            donkeyServer.setUsers((int) inPacket.getStatus()[0]);
                            donkeyServer.setFiles((int) inPacket.getStatus()[1]);
                            lastStatusMessage = System.currentTimeMillis();
                            break;
                        }
                    case OP_SERVERLIST:
                        {
                            log.fine(getConnectionNumber() + "  Server list received.");
                            Collection additionalServerList = inPacket.getServerlist();
                            if (additionalServerList != null) {
                                dContext.getServerList().addAll(additionalServerList);
                                lastServerListreceivetime = System.currentTimeMillis();
                                if (dContext.isDeadServerRemovingEnabled()) {
                                    log.fine(getConnectionNumber() + " prune list");
                                    int a = dContext.getServerList().pruneList(3600000L);
                                    log.fine(getConnectionNumber() + " " + a + " server removed from list.");
                                }
                                Iterator it = dContext.getServerList().iterator();
                                while (it.hasNext()) {
                                    DonkeyServer aDonkeyServer = (DonkeyServer) it.next();
                                    dContext.addSeverForStatusUpdate(aDonkeyServer);
                                    if (aDonkeyServer.getName().equals("no name")) {
                                        dContext.addSeverForServerInfo(aDonkeyServer);
                                    }
                                }
                            }
                            break;
                        }
                    case OP_SEARCHRESULT:
                        {
                            log.fine("Search results received.");
                            try {
                                SearchQuery currentsq = dContext.getSearchQuery();
                                String searchid = currentsq.getQuery();
                                LinkedList donkeySearch = inPacket.getSearchResults(new InetSocketAddress(getChannel().socket().getInetAddress(), getChannel().socket().getPort()), currentsq);
                                SearchManager sm = SearchManager.getInstance();
                                while (!donkeySearch.isEmpty()) {
                                    sm.addResult(searchid, (SearchResult) donkeySearch.removeFirst());
                                }
                                lastsearch = currentsq;
                                if (currentsq.getResultCount() >= currentsq.getLimit()) {
                                    sm.setQueryStatus(searchid, SearchManager.QS_End);
                                    dContext.removeSearchQuery(currentsq);
                                }
                            } catch (Exception e) {
                                log.info("trouble: hexdump from " + inPacket.premetapos + " till " + Math.min(inPacket.premetapos + 128, inPacket.packetBuffer.limit()) + " " + Convert.byteBufferToHexString(inPacket.packetBuffer, inPacket.premetapos, Math.min(inPacket.premetapos + 128, inPacket.packetBuffer.limit())));
                                log.log(Level.WARNING, "Error checking results: ", e);
                            }
                            break;
                        }
                    case OP_CALLBACKREQUESTED:
                        {
                            InetSocketAddress address = inPacket.getPushAddress();
                            log.fine("Server requests push connection to: " + address.toString());
                            DonkeyClientConnection dcc = new DonkeyClientConnection();
                            dcc.setPeerAddress(address);
                            ConnectionManager.getInstance().newConnection(dcc);
                            break;
                        }
                    case OP_CALLBACKFAIL:
                        {
                            break;
                        }
                    case OP_FOUNDSOURCES:
                        {
                            log.fine("Received download sources...");
                            inPacket.addDownloadSources();
                            break;
                        }
                    case OP_IDCHANGE:
                        {
                            long id = inPacket.getClientId();
                            log.info("ID: " + Long.toString(id));
                            if (DonkeyProtocol.isLowID(id)) {
                                log.info("We are firewalled.");
                            } else {
                                log.fine("Not firewalled.");
                            }
                            waiting = true;
                            if (dContext.isEmuleEnabled && inPacket.getPacket().getBuffer().remaining() >= 8) {
                                int flags = inPacket.getPacket().getBuffer().getInt(10);
                                donkeyServer.setTCPEmuleCompression((flags & 1) == 1);
                            }
                            dContext.setUserID(id);
                            state = DonkeyProtocol.SERVER_CONNECTED;
                            dContext.setUpdateSharedFiles(false);
                            DonkeyPacket fileOffer = DonkeyPacketFactory.fileOffer();
                            if (fileOffer != null) addOutPacket(fileOffer);
                            break;
                        }
                    case OP_SERVERINFO:
                        {
                            donkeyServer = inPacket.getServerInfo(donkeyServer);
                            log.fine("Servername:" + donkeyServer.getName() + "\ned2k: Description:" + donkeyServer.getDescription());
                            break;
                        }
                    default:
                        {
                            log.fine("Got unknown packet from server: \"" + inPacket.toString() + "\"");
                        }
                }
            } catch (CorruptPacketException cpe) {
                if (cpe.getCause() == null) {
                    log.log(Level.WARNING, getConnectionNumber() + " has problem with corrupt packet: " + cpe.getMessage() + " cause ", cpe.getCause());
                } else {
                    log.severe(getConnectionNumber() + " has problem with packet: " + cpe.getMessage());
                }
                close();
            }
            inPacket.disposePacket();
        }
    }
