    private void stripeAnchors(Node node) throws RenderingException {
        try {
            NodeList anchors = ixml().evaluate(node, "//xhtml:a[@class=\"swemas_content\"]", ns());
            for (int i = 0; i < anchors.getLength(); ++i) {
                Node it = anchors.item(i);
                it.getParentNode().removeChild(it);
            }
        } catch (XPathExpressionException e) {
            try {
                throw new RenderingException(null, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode());
            } catch (ModuleNotFoundException m) {
                throw new RenderingException(null, name(new Locale("en", "US")), ErrorCode.InternalError.getCode());
            }
        }
    }
