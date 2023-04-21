    public void forwardRequest(URI targetURI, Request request, SipProvider sipProvider, ServerTransaction serverTransaction, boolean statefullForwarding) throws InvalidArgumentException, ParseException, SipException {
        statefullForwarding = statefullForwarding & (serverTransaction != null);
        URI requestURI = request.getRequestURI();
        if (log.isTraceEnabled()) log.trace("Forwarding request to " + targetURI + (statefullForwarding ? " statefully" : " statelessly"));
        Request clonedRequest = (Request) request.clone();
        if (requestURI.isSipURI()) {
            if (targetURI != null) {
                clonedRequest.setRequestURI(SipUtils.getCanonicalizedURI(targetURI));
                if (log.isTraceEnabled()) log.trace("RequestURI replaced with " + clonedRequest.getRequestURI());
            }
        } else {
            if (log.isDebugEnabled()) log.debug("Forwarding not SIP requests is currently not implemented.");
            return;
        }
        MaxForwardsHeader mf = (MaxForwardsHeader) clonedRequest.getHeader(MaxForwardsHeader.NAME);
        if (mf == null) {
            mf = headerFactory.createMaxForwardsHeader(70);
            clonedRequest.addHeader(mf);
            if (log.isTraceEnabled()) log.trace("Max-Forwards header is missing. Created and added to the cloned request.");
        } else {
            mf.setMaxForwards(mf.getMaxForwards() - 1);
            if (log.isTraceEnabled()) log.trace("Max-Forwards value decremented by one. It is now: " + mf.getMaxForwards());
        }
        ListeningPoint[] lps = sipProvider.getListeningPoints();
        SipURI sipURI = addressFactory.createSipURI(null, getHostname(sipProvider));
        sipURI.setPort(lps[0].getPort());
        Address address = addressFactory.createAddress(null, sipURI);
        RecordRouteHeader recordRouteHeader = headerFactory.createRecordRouteHeader(address);
        recordRouteHeader.setParameter("lr", null);
        clonedRequest.addFirst(recordRouteHeader);
        if (log.isTraceEnabled()) log.trace("Added Record-Route header: " + recordRouteHeader);
        if (log.isTraceEnabled()) log.trace("Postprocessing routing information...");
        ListIterator routes = clonedRequest.getHeaders(RouteHeader.NAME);
        if (routes != null && routes.hasNext()) {
            RouteHeader routeHeader = (RouteHeader) routes.next();
            Address routeAddress = routeHeader.getAddress();
            URI routeURI = routeAddress.getURI();
            if (routeURI.isSipURI() && (!((SipURI) routeURI).hasLrParam())) {
                RouteHeader routeHeaderToAdd = headerFactory.createRouteHeader(addressFactory.createAddress(clonedRequest.getRequestURI()));
                clonedRequest.addLast(routeHeaderToAdd);
                clonedRequest.setRequestURI(routeURI);
                clonedRequest.removeFirst(RouteHeader.NAME);
                if (log.isTraceEnabled()) log.trace("RequestURI placed to the end of Route headers, and first Route header " + routeURI + " was set as RequestURI");
            } else if (log.isTraceEnabled()) log.trace("First Route header " + routeHeader + " is not SIP URI or it doesn't contain lr parameter");
        } else {
            if (log.isTraceEnabled()) log.trace("No postprocess routing information to do (No routes detected).");
        }
        if (log.isTraceEnabled()) log.trace("Postprocessing finished.");
        String branchId = SipUtils.generateBranchId();
        if (operationMode == STATELESS_MODE) {
            try {
                ViaHeader topmostViaHeader = (ViaHeader) request.getHeader(ViaHeader.NAME);
                if (topmostViaHeader != null) {
                    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                    String branch = topmostViaHeader.getBranch();
                    if (branch.startsWith(SipUtils.BRANCH_MAGIC_COOKIE)) {
                        byte[] bytes = messageDigest.digest(Integer.toString(branch.hashCode()).getBytes());
                        branchId = SipUtils.toHexString(bytes);
                    } else {
                        String via = topmostViaHeader.toString().trim();
                        String toTag = ((ToHeader) request.getHeader(ToHeader.NAME)).getTag();
                        String fromTag = ((FromHeader) request.getHeader(FromHeader.NAME)).getTag();
                        String callid = ((CallIdHeader) request.getHeader(CallIdHeader.NAME)).getCallId();
                        long cseq = ((CSeqHeader) request.getHeader(CSeqHeader.NAME)).getSeqNumber();
                        String requestUri = requestURI.toString().trim();
                        byte[] bytes = messageDigest.digest((via + toTag + fromTag + callid + cseq + requestUri).getBytes());
                        branchId = SipUtils.toHexString(bytes);
                    }
                }
            } catch (NoSuchAlgorithmException ex) {
            }
        }
        ViaHeader viaHeader = headerFactory.createViaHeader(getHostname(sipProvider), lps[0].getPort(), lps[0].getTransport(), branchId);
        clonedRequest.addFirst(viaHeader);
        if (log.isTraceEnabled()) log.trace("Added Via header " + viaHeader);
        ContentLengthHeader contentLengthHeader = (ContentLengthHeader) clonedRequest.getHeader(ContentLengthHeader.NAME);
        if (contentLengthHeader == null) {
            byte[] contentData = request.getRawContent();
            contentLengthHeader = headerFactory.createContentLengthHeader(contentData == null ? 0 : contentData.length);
            clonedRequest.setContentLength(contentLengthHeader);
            if (log.isTraceEnabled()) log.trace("Added Content-Length header " + contentLengthHeader);
        } else if (log.isTraceEnabled()) log.trace("Leaving existing Content-Length header untouched.");
        if (log.isTraceEnabled()) {
            log.trace("Forwarding request " + (statefullForwarding ? "statefully" : "statelessly"));
            log.trace("Outgoing request:\n" + clonedRequest);
        }
        if (!statefullForwarding) {
            sipProvider.sendRequest(clonedRequest);
            if (log.isDebugEnabled()) log.debug("Request forwarded statelessly.");
        } else {
            ClientTransaction clientTransaction = sipProvider.getNewClientTransaction(clonedRequest);
            if (log.isTraceEnabled()) log.trace("Client transaction for request is: " + clientTransaction);
            snmpAssistant.incrementSnmpInteger(SNMP_OID_NUM_CLIENT_TRANSACTIONS);
            TransactionsMapping transactionMapping = (TransactionsMapping) serverTransaction.getApplicationData();
            if (transactionMapping == null) {
                transactionMapping = new TransactionsMapping(serverTransaction, sipProvider);
                serverTransaction.setApplicationData(transactionMapping);
            }
            transactionMapping.addClientTransaction(clientTransaction);
            clientTransaction.setApplicationData(transactionMapping);
            if (clonedRequest.getMethod().equals(Request.INVITE)) {
                Timer timer = new Timer();
                TimerCTask timerTask = new TimerCTask(clientTransaction, sipProvider, this, log);
                transactionMapping.registerTimerC(timer, clientTransaction);
                if (log.isTraceEnabled()) log.trace("Timer C created for proxied CT " + clientTransaction);
                timer.schedule(timerTask, timercPeriod);
            }
            clientTransaction.sendRequest();
            if (log.isDebugEnabled()) log.debug("Request forwarded statefully.");
        }
    }
