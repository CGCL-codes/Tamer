    private void handleLink(LinkData linkData) {
        Long tamanho = -1l;
        boolean hasContentLength = false;
        int maxNumberTries = 50;
        int httpStatus = -1;
        if (linkData.isLinkOk() && !(MapSites.getInstance().containsBadRequesters(linkData.getHost())) && !LinksCache.getInstance().containsLink(linkData)) {
            MapSites.getInstance().addTotalReq();
            if (!(linkData.getHost().startsWith("ftp"))) {
                try {
                    if (address == null) address = InetAddress.getByName(linkData.getHost());
                    while (rd != null && rd.ready()) {
                        rd.read();
                    }
                    if (sock == null || sock.isInputShutdown() || sock.isOutputShutdown()) {
                        if (sock != null) {
                            sock.close();
                            try {
                                sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        sock = SocketFactory.getDefault().createSocket(address, linkData.getPort());
                        sock.setSoTimeout(Constants.DEFAULT_TIMEOUT);
                        sock.setKeepAlive(true);
                        if (wr != null) {
                            wr.close();
                        }
                        wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF8"));
                        if (rd != null) {
                            rd.close();
                        }
                        rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                    }
                    wr.write("HEAD " + linkData.getPathQuery() + " HTTP/1.1\r\nHost: " + linkData.getHost() + "\r\nConnection: Keep-Alive\r\n\r\n");
                    wr.flush();
                    String leitura = null;
                    leitura = rd.readLine();
                    try {
                        if (leitura.split(" ")[1] != null) {
                            httpStatus = Integer.parseInt(leitura.split(" ")[1]);
                        }
                    } catch (NullPointerException e) {
                    } catch (NumberFormatException e) {
                    } catch (ArrayIndexOutOfBoundsException e) {
                        httpStatus = Constants.HTTP_ERROR_PROTOCOL;
                    }
                    while (leitura != null && !leitura.isEmpty()) {
                        leitura = leitura.toLowerCase().trim();
                        try {
                            if (leitura.startsWith("content-length")) {
                                hasContentLength = true;
                                leitura = leitura.split("th:", 2)[1];
                                tamanho = Long.parseLong(leitura.trim());
                            } else if (leitura.startsWith("keep-alive")) {
                                if (leitura.contains("max")) {
                                    String max = leitura.substring(leitura.indexOf("max"));
                                    if (max.contains(",")) {
                                        max = max.substring(0, max.indexOf(","));
                                    }
                                    max = max.substring(max.indexOf("=") + 1).trim();
                                    keepAliveRequests = Integer.parseInt(max);
                                }
                                if (leitura.contains("timeout")) {
                                    String timeout = leitura.substring(leitura.indexOf("timeout"));
                                    if (timeout.contains(",")) {
                                        timeout = timeout.substring(0, timeout.indexOf(","));
                                    }
                                    timeout = timeout.substring(timeout.indexOf("=") + 1).trim();
                                    waitTime = Math.min(Integer.parseInt(timeout), MAX_SOCK_WAITTIME);
                                }
                            }
                        } catch (NullPointerException e) {
                        } catch (NumberFormatException e) {
                            tamanho = -2L;
                        }
                        leitura = rd.readLine();
                    }
                    if (MapSites.getInstance().containsBadRequesters(linkData.getHost())) {
                    }
                    if (Utils.httpResponseSuccessful(httpStatus) || Utils.httpResponseRedirect(httpStatus)) tries = 0;
                } catch (UnknownHostException e) {
                    MapSites.getInstance().excludeSite(linkData.getHost());
                    httpStatus = Constants.HTTP_ERROR_DNS;
                    SimpleLog.getInstance().writeLog(6, "ERRO na aquisição do endereço do host " + linkData.getUrl() + " " + Thread.currentThread().getName());
                    SimpleLog.getInstance().writeException(e, 7);
                } catch (NoRouteToHostException e) {
                    MapSites.getInstance().excludeSite(linkData.getHost());
                    httpStatus = Constants.HTTP_ERROR_NO_ROUTE;
                    SimpleLog.getInstance().writeLog(6, "ERRO na execução da requisição HTTP com url: " + linkData.getUrl() + " " + Thread.currentThread().getName());
                    SimpleLog.getInstance().writeException(e, 7);
                } catch (ConnectException e) {
                    tries++;
                    httpStatus = Constants.HTTP_ERROR_CONNECT;
                    SimpleLog.getInstance().writeLog(6, "ERRO na conexão HTTP com host " + linkData.getUrl());
                    SimpleLog.getInstance().writeException(e, 7);
                } catch (SocketTimeoutException e) {
                    tries++;
                    httpStatus = Constants.HTTP_ERROR_TIMEOUT;
                    SimpleLog.getInstance().writeLog(6, "ERRO de timeout na conxão HTTP com host " + linkData.getHost());
                    SimpleLog.getInstance().writeException(e, 7);
                } catch (SocketException e) {
                    tries++;
                    httpStatus = Constants.HTTP_ERROR_CONNECTION_REFUSED;
                    SimpleLog.getInstance().writeLog(6, "SocketException na execução da requisição HTTP com a página " + linkData.getUrl());
                    SimpleLog.getInstance().writeException(e, 7);
                } catch (IOException e) {
                    tries++;
                    httpStatus = Constants.HTTP_ERROR_UNKNOWN;
                    SimpleLog.getInstance().writeLog(6, "IOException na execução da requisição HTTP com a página: " + linkData.getUrl());
                    SimpleLog.getInstance().writeException(e, 7);
                } finally {
                    if (tries > maxNumberTries) {
                        MapSites.getInstance().excludeSite(linkData.getHost());
                    }
                }
            }
        } else {
            httpStatus = 0;
            if ((tries > maxNumberTries)) httpStatus = 1;
            if (MapSites.getInstance().containsBadRequesters(linkData.getHost())) httpStatus += 2;
        }
        linkData.setHttpStatus(httpStatus);
        if (linkData.isLinkOk()) {
            Long srcPageWireId = linkData.getSrcPageWireId();
            Long srcHostId = AutomatedLinksCounter.getInstance().getHostFromPage(srcPageWireId);
            if (srcPageWireId == null) {
                System.out.println("ID NULL!!!????");
            }
            String extension = null;
            if (linkData.getUrl().contains(".")) {
                extension = Utils.getExtMinusParams(linkData.getUrl());
            }
            if (extension == null) {
                extension = Constants.LINKS_EXTENSION_NONE;
            }
            String keyExt = srcHostId + " " + extension;
            LinksCounter linksCounter = linkData.getLinksCounter();
            linksCounter.editaMapExt(keyExt, tamanho, hasContentLength);
            boolean local = linkData.isLocalLink();
            String domain = "";
            if (linkData.getUrl() != null) domain = Utils.getDomain(Utils.getSubdomain(Utils.getPageHost(linkData.getUrl())));
            String keyDom = srcHostId + " " + domain + " " + local;
            linksCounter.editaMapDom(keyDom, tamanho, hasContentLength);
            linkData.setSrcHostID(srcHostId);
            linkData.setDominio(domain);
            linkData.setExt(extension);
            linkData.setTamanho(tamanho);
            linksCounter.editaMapLinks(linkData);
            if (Constants.XML_EXTLIST.contains(extension)) {
                AutomatedLinksCounter.getInstance().writeLinkDownloadList(srcPageWireId, srcHostId, local, linkData.getUrl());
            }
            linksCounter.removeMarcaLinks(linkData.getIdAux());
        }
    }
