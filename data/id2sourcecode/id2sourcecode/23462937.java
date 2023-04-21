    public void run() {
        final String server = StringUtil.addSlash("http://" + params.getParam("server", FreegleProperties.get("remotevalidation.server")));
        numThreads = params.getint("threads", numThreads);
        limit = params.getint("limit", limit);
        if (limit < numThreads) limit = numThreads;
        final FAccess fAccess = new FAccess_lib();
        final RemoteUtil remote = new RemoteUtil();
        remote.setServer(server);
        while (true) try {
            Vector uris = new Vector();
            final Hashtable uriIds = new Hashtable();
            String line;
            if (params.getParam("uri") != null) uris.add(FreenetUtil.tidyKey(params.getParam("uri"))); else {
                URL url = new URL(server + Constants.unvalidatedKeysScript(limit));
                DataInputStream dis;
                try {
                    dis = new DataInputStream(url.openStream());
                } catch (Exception e) {
                    System.err.println("Trying to load: " + url);
                    throw e;
                }
                while ((line = dis.readLine()) != null) {
                    Vector parts = FreenetUtil.splitLine(line);
                    uriIds.put(parts.get(1), parts.get(0));
                    uris.add(parts.get(1));
                }
                dis.close();
            }
            if (uris.isEmpty()) {
                System.out.println("No URIs to validate, sleeping 300 seconds");
                try {
                    Thread.sleep(300000);
                } catch (InterruptedException e2) {
                    return;
                }
                continue;
            }
            CharArrayWriter baos = new CharArrayWriter();
            final PrintWriter ps = new PrintWriter(baos);
            ps.println(FreegleProperties.get("internalServlets.magicLine"));
            int iUri = 0;
            while (iUri < uris.size()) {
                Vector threads = new Vector();
                for (int jUri = iUri; jUri < Math.min(iUri + numThreads, uris.size()); jUri++) {
                    final String uri = (String) uris.get(jUri);
                    System.out.println("Validating #" + jUri + " " + uri);
                    Thread t = new Thread() {

                        public void run() {
                            try {
                                FAccess.VerificationResult result = fAccess.verifyDocument(uri);
                                String message = StringUtil.replace(result.getMessage(), "java.lang.Exception: ", "");
                                System.out.println("Result for " + uri + ":");
                                System.out.println("  " + message);
                                System.out.println();
                                FreegleFieldSet fs = new FreegleFieldSet();
                                put(fs, "uri", uri);
                                put(fs, "id", (String) uriIds.get(uri));
                                put(fs, "success", result.getSuccess() ? 1 : 0);
                                if (result.getDocument() != null) {
                                    result.getDocument().analyze();
                                    String content = new String(result.getDocument().getLoadedContent());
                                    System.out.println("loaded bytes: " + content.length());
                                    Thread.sleep(3000);
                                    boolean isHTML = "text/html".equals(result.getDocument().getContentType());
                                    put(fs, "contentLength", result.getDocument().getSize());
                                    put(fs, "contentType", result.getDocument().getContentType());
                                    put(fs, "title", result.getDocument().getTitle());
                                    HashSet crawlUris = new HashSet();
                                    crawlUris.addAll(URIFinder.findURIs(content));
                                    if (!crawlUris.isEmpty()) remote.callAddScript(new Vector(crawlUris), "link " + uri);
                                    if (result.getDocument().getContentType() != null && result.getDocument().getContentType().startsWith("text/")) {
                                        put(fs, "content", content);
                                    }
                                    if (isHTML) {
                                        Set links;
                                        try {
                                            String base = uri.substring(0, uri.lastIndexOf('/') + 1);
                                            links = LinkFinder.findLinks(new String(result.getDocument().getLoadedContent()), base);
                                            System.out.println("# links found in document: " + links.size());
                                        } catch (Throwable t2) {
                                            links = new HashSet();
                                            t2.printStackTrace();
                                        }
                                        if (!links.isEmpty()) {
                                            CharArrayWriter baos2 = new CharArrayWriter();
                                            final PrintWriter ps2 = new PrintWriter(baos2);
                                            ps2.println(FreegleProperties.get("internalServlets.magicLine"));
                                            fs.writeFields(ps2);
                                            ps2.flush();
                                            fs = null;
                                            RemoteUtil.callInternalScript(server + Constants.saveValidationsScript, baos2.toString());
                                            try {
                                                remote.callAddLinksScript(new Vector(links), uri);
                                            } catch (java.net.ConnectException e) {
                                                throw e;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else if (result.getRedirect() != null) {
                                    put(fs, "redirect_uri", result.getRedirect().getURI());
                                    put(fs, "redirect_baseline", result.getRedirect().getBaseline());
                                    put(fs, "redirect_increment", result.getRedirect().getIncrement());
                                }
                                if (fs != null) {
                                    fs.writeFields(ps);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e2) {
                                }
                            }
                        }
                    };
                    t.start();
                    threads.add(t);
                }
                for (int i = 0; i < threads.size(); i++) ((Thread) threads.get(i)).join();
                iUri += numThreads;
            }
            ps.flush();
            RemoteUtil.callInternalScript(server + Constants.saveValidationsScript, baos.toString());
            if (params.getParam("uri") != null) break;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An exception occurred, sleeping 60 seconds");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e2) {
                return;
            }
        }
    }
