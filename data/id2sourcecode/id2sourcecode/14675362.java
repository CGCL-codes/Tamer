    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        RequestV2 request = null;
        RendererConfiguration renderer = null;
        String userAgentString = null;
        StringBuilder unknownHeaders = new StringBuilder();
        String separator = "";
        HttpRequest nettyRequest = this.nettyRequest = (HttpRequest) e.getMessage();
        InetSocketAddress remoteAddress = (InetSocketAddress) e.getChannel().getRemoteAddress();
        InetAddress ia = remoteAddress.getAddress();
        if (filterIp(ia)) {
            e.getChannel().close();
            logger.trace("Access denied for address " + ia + " based on IP filter");
            return;
        }
        logger.trace("Opened request handler on socket " + remoteAddress);
        PMS.get().getRegistry().disableGoToSleep();
        if (HttpMethod.GET.equals(nettyRequest.getMethod())) {
            request = new RequestV2("GET", nettyRequest.getUri().substring(1));
        } else if (HttpMethod.POST.equals(nettyRequest.getMethod())) {
            request = new RequestV2("POST", nettyRequest.getUri().substring(1));
        } else if (HttpMethod.HEAD.equals(nettyRequest.getMethod())) {
            request = new RequestV2("HEAD", nettyRequest.getUri().substring(1));
        } else {
            request = new RequestV2(nettyRequest.getMethod().getName(), nettyRequest.getUri().substring(1));
        }
        logger.trace("Request: " + nettyRequest.getProtocolVersion().getText() + " : " + request.getMethod() + " : " + request.getArgument());
        if (nettyRequest.getProtocolVersion().getMinorVersion() == 0) {
            request.setHttp10(true);
        }
        renderer = RendererConfiguration.getRendererConfigurationBySocketAddress(ia);
        if (renderer != null) {
            PMS.get().setRendererfound(renderer);
            request.setMediaRenderer(renderer);
            logger.trace("Matched media renderer \"" + renderer.getRendererName() + "\" based on address " + ia);
        }
        for (String name : nettyRequest.getHeaderNames()) {
            String headerLine = name + ": " + nettyRequest.getHeader(name);
            logger.trace("Received on socket: " + headerLine);
            if (renderer == null && headerLine != null && headerLine.toUpperCase().startsWith("USER-AGENT") && request != null) {
                userAgentString = headerLine.substring(headerLine.indexOf(":") + 1).trim();
                renderer = RendererConfiguration.getRendererConfigurationByUA(userAgentString);
                if (renderer != null) {
                    request.setMediaRenderer(renderer);
                    renderer.associateIP(ia);
                    PMS.get().setRendererfound(renderer);
                    logger.trace("Matched media renderer \"" + renderer.getRendererName() + "\" based on header \"" + headerLine + "\"");
                }
            }
            if (renderer == null && headerLine != null && request != null) {
                renderer = RendererConfiguration.getRendererConfigurationByUAAHH(headerLine);
                if (renderer != null) {
                    request.setMediaRenderer(renderer);
                    renderer.associateIP(ia);
                    PMS.get().setRendererfound(renderer);
                    logger.trace("Matched media renderer \"" + renderer.getRendererName() + "\" based on header \"" + headerLine + "\"");
                }
            }
            try {
                StringTokenizer s = new StringTokenizer(headerLine);
                String temp = s.nextToken();
                if (request != null && temp.toUpperCase().equals("SOAPACTION:")) {
                    request.setSoapaction(s.nextToken());
                } else if (headerLine.toUpperCase().indexOf("RANGE: BYTES=") > -1) {
                    String nums = headerLine.substring(headerLine.toUpperCase().indexOf("RANGE: BYTES=") + 13).trim();
                    StringTokenizer st = new StringTokenizer(nums, "-");
                    if (!nums.startsWith("-")) {
                        request.setLowRange(Long.parseLong(st.nextToken()));
                    }
                    if (!nums.startsWith("-") && !nums.endsWith("-")) {
                        request.setHighRange(Long.parseLong(st.nextToken()));
                    } else {
                        request.setHighRange(-1);
                    }
                } else if (headerLine.toLowerCase().indexOf("transfermode.dlna.org:") > -1) {
                    request.setTransferMode(headerLine.substring(headerLine.toLowerCase().indexOf("transfermode.dlna.org:") + 22).trim());
                } else if (headerLine.toLowerCase().indexOf("getcontentfeatures.dlna.org:") > -1) {
                    request.setContentFeatures(headerLine.substring(headerLine.toLowerCase().indexOf("getcontentfeatures.dlna.org:") + 28).trim());
                } else {
                    Matcher matcher = TIMERANGE_PATTERN.matcher(headerLine);
                    if (matcher.find()) {
                        String first = matcher.group(1);
                        if (first != null) {
                            request.setTimeRangeStartString(first);
                        }
                        String end = matcher.group(2);
                        if (end != null) {
                            request.setTimeRangeEndString(end);
                        }
                    } else {
                        boolean isKnown = false;
                        for (String knownHeaderString : KNOWN_HEADERS) {
                            if (headerLine.toLowerCase().startsWith(knownHeaderString.toLowerCase())) {
                                isKnown = true;
                                break;
                            }
                        }
                        if (!isKnown) {
                            unknownHeaders.append(separator + headerLine);
                            separator = ", ";
                        }
                    }
                }
            } catch (Exception ee) {
                logger.error("Error parsing HTTP headers", ee);
            }
        }
        if (request != null) {
            if (request.getMediaRenderer() == null) {
                request.setMediaRenderer(RendererConfiguration.getDefaultConf());
                logger.trace("Using default media renderer " + request.getMediaRenderer().getRendererName());
                if (userAgentString != null && !userAgentString.equals("FDSSDP")) {
                    logger.info("Media renderer was not recognized. Possible identifying HTTP headers: User-Agent: " + userAgentString + ("".equals(unknownHeaders.toString()) ? "" : ", " + unknownHeaders.toString()));
                    PMS.get().setRendererfound(request.getMediaRenderer());
                }
            } else {
                if (userAgentString != null) {
                    logger.trace("HTTP User-Agent: " + userAgentString);
                }
                logger.trace("Recognized media renderer " + request.getMediaRenderer().getRendererName());
            }
        }
        if (HttpHeaders.getContentLength(nettyRequest) > 0) {
            byte data[] = new byte[(int) HttpHeaders.getContentLength(nettyRequest)];
            ChannelBuffer content = nettyRequest.getContent();
            content.readBytes(data);
            request.setTextContent(new String(data, "UTF-8"));
        }
        if (request != null) {
            logger.trace("HTTP: " + request.getArgument() + " / " + request.getLowRange() + "-" + request.getHighRange());
        }
        writeResponse(e, request, ia);
    }
