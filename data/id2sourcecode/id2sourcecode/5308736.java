    @Override
    public void dispatch(HttpServletRequest request, HttpServletResponse response) {
        try {
            IXmlChannel ixml = (IXmlChannel) kernel().getChannel(IXmlChannel.class);
            Document map = ixml.open(kernel().getPath("/site/map.xml"), kernel().getPath("/schema/map.xsd"));
            Document mime_proc = ixml.open(kernel().getPath("/config/mime_processors.xml"), kernel().getPath("/schema/map.xsd"));
            String url = request.getRequestURL().toString();
            NamespaceContext ns = new NamespaceContext();
            ns.setNamespace("map", "swemas/map");
            NodeList links = ixml.evaluate(map, "/map:map/map:pair/map:key/text()", ns);
            boolean found = false;
            for (int i = 0; i < links.getLength(); ++i) {
                Node n = links.item(i);
                String str = n.getNodeValue();
                Pattern p = Pattern.compile(str);
                Matcher m = p.matcher(url);
                if (m.matches()) {
                    String mime = ixml.evaluate(n, "../../map:value/text()", ns).item(0).getNodeValue();
                    String proc = ixml.evaluate(mime_proc, "/map:map/map:pair[map:key=\"" + mime + "\"]/map:value/text()", ns).item(0).getNodeValue();
                    IHttpProcessingChannel ih = (IHttpProcessingChannel) kernel().getChannel(proc);
                    ih.process(request, response);
                    found = true;
                    break;
                }
            }
            if (!found) {
                found = false;
                for (int i = 0; i < links.getLength(); ++i) {
                    Node n = links.item(i);
                    String str = n.getNodeValue();
                    Pattern p = Pattern.compile(str);
                    Matcher m = p.matcher("error_404");
                    if (m.matches()) {
                        String proc = ixml.evaluate(n, "../../map:value/text()", ns).item(0).getNodeValue();
                        IHttpProcessingChannel ih = (IHttpProcessingChannel) kernel().getChannel(proc);
                        ih.process(request, response);
                        found = true;
                        break;
                    }
                }
                if (!found) response.sendError(404);
            }
        } catch (ModuleNotFoundException e) {
            try {
                IMessagingChannel im = (IMessagingChannel) kernel().getChannel(IMessagingChannel.class);
                response.sendError(500, e.getLocalizedMessage(im.getText(String.valueOf(e.getCode()), e.getClass().getName(), request.getLocale())));
            } catch (IOException e1) {
            } catch (ModuleNotFoundException e1) {
                try {
                    response.sendError(500, e.getMessage());
                } catch (IOException e2) {
                }
            }
        } catch (XmlException e) {
            try {
                IMessagingChannel im = (IMessagingChannel) kernel().getChannel(IMessagingChannel.class);
                response.sendError(500, e.getLocalizedMessage(im.getText(String.valueOf(e.getCode()), e.getInModuleName(), request.getLocale())));
            } catch (IOException e1) {
            } catch (ModuleNotFoundException e1) {
                try {
                    response.sendError(500, e.getMessage());
                } catch (IOException e2) {
                }
            }
        } catch (XPathExpressionException e) {
            try {
                response.sendError(500, e.getMessage());
            } catch (IOException e1) {
            }
        } catch (IOException e) {
        } catch (RenderingException e) {
            try {
                response.sendError(500, e.getMessage());
            } catch (IOException e1) {
            }
        }
    }
