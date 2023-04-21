    public int responsePhase2Handler(Response resp, Request req) throws IOException {
        int sts = resp.getStatusCode();
        switch(sts) {
            case 302:
                if (req.getMethod().equals("POST") || req.getMethod().equals("PUT")) {
                    Log.write(Log.MODS, "RdirM: Received status: " + sts + " " + resp.getReasonLine() + " - treating as 303");
                    sts = 303;
                }
            case 301:
            case 303:
            case 307:
                Log.write(Log.MODS, "RdirM: Handling status: " + sts + " " + resp.getReasonLine());
                if (!req.getMethod().equals("GET") && !req.getMethod().equals("HEAD") && sts != 303) {
                    Log.write(Log.MODS, "RdirM: not redirected because " + "method is neither HEAD nor GET");
                    if (sts == 301 && resp.getHeader("Location") != null) update_perm_redir_list(req, resLocHdr(resp.getHeader("Location"), req));
                    resp.setEffectiveURI(lastURI);
                    return RSP_CONTINUE;
                }
            case 305:
            case 306:
                if (sts == 305 || sts == 306) Log.write(Log.MODS, "RdirM: Handling status: " + sts + " " + resp.getReasonLine());
                if (sts == 305 && req.getConnection().getProxyHost() != null) {
                    Log.write(Log.MODS, "RdirM: 305 ignored because " + "a proxy is already in use");
                    resp.setEffectiveURI(lastURI);
                    return RSP_CONTINUE;
                }
                if (level >= 15 || resp.getHeader("Location") == null) {
                    if (level >= 15) Log.write(Log.MODS, "RdirM: not redirected because " + "of too many levels of redirection"); else Log.write(Log.MODS, "RdirM: not redirected because " + "no Location header was present");
                    resp.setEffectiveURI(lastURI);
                    return RSP_CONTINUE;
                }
                level++;
                URI loc = resLocHdr(resp.getHeader("Location"), req);
                HTTPConnection mvd;
                String nres;
                new_con = false;
                if (sts == 305) {
                    mvd = new HTTPConnection(req.getConnection().getProtocol(), req.getConnection().getHost(), req.getConnection().getPort());
                    mvd.setCurrentProxy(loc.getHost(), loc.getPort());
                    mvd.setSSLSocketFactory(req.getConnection().getSSLSocketFactory());
                    mvd.setContext(req.getConnection().getContext());
                    new_con = true;
                    nres = req.getRequestURI();
                    req.setMethod("GET");
                    req.setData(null);
                    req.setStream(null);
                } else if (sts == 306) {
                    return RSP_CONTINUE;
                } else {
                    if (req.getConnection().isCompatibleWith(loc)) {
                        mvd = req.getConnection();
                        nres = loc.getPathAndQuery();
                    } else {
                        try {
                            mvd = new HTTPConnection(loc);
                            nres = loc.getPathAndQuery();
                        } catch (Exception e) {
                            if (req.getConnection().getProxyHost() == null || !loc.getScheme().equalsIgnoreCase("ftp")) return RSP_CONTINUE;
                            mvd = new HTTPConnection("http", req.getConnection().getProxyHost(), req.getConnection().getProxyPort());
                            mvd.setCurrentProxy(null, 0);
                            nres = loc.toExternalForm();
                        }
                        mvd.setSSLSocketFactory(req.getConnection().getSSLSocketFactory());
                        mvd.setContext(req.getConnection().getContext());
                        new_con = true;
                    }
                    if (sts == 303) {
                        if (!req.getMethod().equals("HEAD")) req.setMethod("GET");
                        req.setData(null);
                        req.setStream(null);
                    } else {
                        if (req.getStream() != null) {
                            if (!HTTPConnection.deferStreamed) {
                                Log.write(Log.MODS, "RdirM: status " + sts + " not handled - request " + "has an output stream");
                                return RSP_CONTINUE;
                            }
                            saved_req = (Request) req.clone();
                            deferred_redir_list.put(req.getStream(), this);
                            req.getStream().reset();
                            resp.setRetryRequest(true);
                        }
                        if (sts == 301) {
                            try {
                                update_perm_redir_list(req, new URI(loc, nres));
                            } catch (ParseException pe) {
                                throw new Error("jgroups.http_client Internal Error: " + "unexpected exception '" + pe + "'");
                            }
                        }
                    }
                    NVPair[] hdrs = req.getHeaders();
                    for (int idx = 0; idx < hdrs.length; idx++) if (hdrs[idx].getName().equalsIgnoreCase("Referer")) {
                        HTTPConnection con = req.getConnection();
                        hdrs[idx] = new NVPair("Referer", con + req.getRequestURI());
                        break;
                    }
                }
                req.setConnection(mvd);
                req.setRequestURI(nres);
                try {
                    resp.getInputStream().close();
                } catch (IOException ioe) {
                }
                if (sts != 305 && sts != 306) {
                    try {
                        lastURI = new URI(loc, nres);
                    } catch (ParseException pe) {
                    }
                    Log.write(Log.MODS, "RdirM: request redirected to " + lastURI.toExternalForm() + " using method " + req.getMethod());
                } else {
                    Log.write(Log.MODS, "RdirM: resending request using " + "proxy " + mvd.getProxyHost() + ":" + mvd.getProxyPort());
                }
                if (req.getStream() != null) return RSP_CONTINUE; else if (new_con) return RSP_NEWCON_REQ; else return RSP_REQUEST;
            default:
                return RSP_CONTINUE;
        }
    }
