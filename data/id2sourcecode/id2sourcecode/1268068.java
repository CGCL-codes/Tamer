        @Override
        public void handle(HttpExchange exch) throws IOException {
            System.err.println("Entered handler");
            HttpPost req = new HttpPost(BACKGROUND_REQUEST_URI);
            String requestText = "", responseText = "";
            String backgroundRequestText = "", backgroundResponseText = "";
            LocationRequest lrq = new LocationRequest();
            LocationResponse lrs = new LocationResponse();
            URI locationURI = null;
            try {
                requestText = IOUtils.toString(exch.getRequestBody());
                if (exch.getPrincipal() != null) {
                    lrq.setAccessUsername(exch.getPrincipal().getName());
                    lrq.setAccessPassword(exch.getPrincipal().getRealm());
                }
                if (requestText.length() > 0) lrq.initFromHeldRequest(requestText);
                if (this.cachedRequest != null) {
                    req = this.cachedRequest;
                } else {
                    backgroundRequestText = lrq.toSkyhookRequest();
                    req.setEntity(new StringEntity(backgroundRequestText));
                    req.setHeader("Content-Type", "text/xml");
                    req.setHeader("X-ForwardedFor", exch.getRemoteAddress().getAddress().getHostAddress());
                    InetAddress remoteHost = exch.getRemoteAddress().getAddress();
                    if (!this.server.knownHost(remoteHost)) {
                        locationURI = server.addReferenceContext(remoteHost, req);
                    }
                }
                HttpResponse resp = this.client.execute((HttpUriRequest) req);
                backgroundResponseText = IOUtils.toString(resp.getEntity().getContent());
                lrs.initFromSkyhookResponse(backgroundResponseText);
                URI[] locationURIs = new URI[1];
                locationURIs[0] = locationURI;
                lrs.setLocationURIs(locationURIs);
                if (lrq.isExact() && (!lrq.getLocationTypes().contains(LocationRequest.LocationType.civic)) && (!lrq.getLocationTypes().contains(LocationRequest.LocationType.any))) lrs.setCivic(null);
                if (lrq.isExact() && (!lrq.getLocationTypes().contains(LocationRequest.LocationType.geodetic)) && (!lrq.getLocationTypes().contains(LocationRequest.LocationType.any))) lrs.setGeodetic(null);
                if (lrq.isExact() && (!lrq.getLocationTypes().contains(LocationRequest.LocationType.locationURI)) && (!lrq.getLocationTypes().contains(LocationRequest.LocationType.any))) lrs.setGeodetic(null);
                responseText = lrs.toHeldResponse(this.server.newEntity(exch.getRemoteAddress().getAddress()));
                System.err.println(requestText);
                System.err.println(backgroundRequestText);
                System.err.println(backgroundResponseText);
                System.err.println(responseText);
                exch.sendResponseHeaders(200, responseText.length());
                exch.getResponseBody().write(responseText.getBytes());
                exch.close();
            } catch (Exception e) {
                e.printStackTrace();
                exch.sendResponseHeaders(500, 0);
                exch.close();
            }
        }
