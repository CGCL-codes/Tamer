    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter(HttpParams.UP2P_REGISTER_RELAY) != null && req.getParameter(HttpParams.UP2P_RELAY_IDENTIFIER) != null) {
            String peerUrl = req.getParameter(HttpParams.UP2P_REGISTER_RELAY);
            if (!peerUrl.startsWith("http://")) {
                peerUrl = "http://" + peerUrl;
            }
            try {
                int relayIdentifier = Integer.parseInt(req.getParameter(HttpParams.UP2P_RELAY_IDENTIFIER));
                synchronized (relayUrlMap) {
                    relayUrlMap.put(relayIdentifier, peerUrl);
                }
                LOG.info("RelayServlet: Added mapping for relay identifier: " + relayIdentifier + " -> " + peerUrl);
            } catch (NumberFormatException e) {
                LOG.error("RelayServlet: Invalid relay identifier specified.");
                return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (req.getParameter(HttpParams.UP2P_RELAY_IDENTIFIER) != null) {
            int relayIdentifier;
            try {
                relayIdentifier = Integer.parseInt(req.getParameter(HttpParams.UP2P_RELAY_IDENTIFIER));
            } catch (NumberFormatException e) {
                LOG.error("RelayServlet: Provided relay identifier could not be parsed as an integer.");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String mappedUrl = relayUrlMap.get(relayIdentifier);
            if (mappedUrl == null) {
                LOG.error("RelayServlet: Got a relay request for unmapped relay identifier: " + relayIdentifier);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String communityId = req.getParameter(HttpParams.UP2P_COMMUNITY);
            String resourceId = req.getParameter(HttpParams.UP2P_RESOURCE);
            String attachName = req.getParameter(HttpParams.UP2P_FILENAME);
            if (communityId == null || resourceId == null) {
                LOG.error("RelayServlet: Relay request did not specify a valid community or resource ID.");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String urlSuffix = "/community/" + communityId + "/" + resourceId;
            if (attachName != null) {
                urlSuffix += "/" + attachName;
            }
            LOG.debug("RelayServet: Got relay request (Relay ID: " + relayIdentifier + "):\n" + req.getRequestURL() + "?" + req.getQueryString());
            LOG.debug("Serving request through URL: " + mappedUrl + urlSuffix);
            HttpClient http = new DefaultHttpClient();
            HttpGet relayRequest = new HttpGet(mappedUrl + urlSuffix);
            HttpResponse relayResponse = http.execute(relayRequest);
            if (relayResponse.getStatusLine().getStatusCode() == 200) {
                LOG.info("RelayServlet: Successful request: " + mappedUrl + urlSuffix);
                if (relayResponse.getLastHeader("Content-Type") != null) {
                    resp.setContentType(relayResponse.getLastHeader("Content-Type").getValue());
                }
                HttpEntity entity = relayResponse.getEntity();
                if (entity != null) {
                    DataOutputStream respOutStream = new DataOutputStream(resp.getOutputStream());
                    DataInputStream relayInStream = new DataInputStream(entity.getContent());
                    try {
                        while (true) {
                            respOutStream.writeByte(relayInStream.readByte());
                        }
                    } catch (EOFException e) {
                    } catch (IOException e) {
                        LOG.error("RelayServlet: IOException copying relay data.");
                    } catch (RuntimeException e) {
                        relayRequest.abort();
                    } finally {
                        relayInStream.close();
                        respOutStream.close();
                    }
                    return;
                }
            } else {
                resp.setStatus(relayResponse.getStatusLine().getStatusCode());
                LOG.error("RelayServlet: Failed request: " + mappedUrl + urlSuffix + "\nStatus: " + relayResponse.getStatusLine());
            }
        }
    }
