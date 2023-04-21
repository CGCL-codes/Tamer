    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws RenderingException {
        Node text = render(null, request).get(0).getChildNodes().item(0);
        if (text != null) {
            try {
                response.getOutputStream().print(text.getNodeValue());
            } catch (DOMException e) {
                try {
                    throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode());
                } catch (ModuleNotFoundException m) {
                    throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InternalError.getCode());
                }
            } catch (IOException e) {
                try {
                    throw new RenderingException(e, name(((ILocaleProvidingChannel) kernel().getChannel(ILocaleProvidingChannel.class)).getCurrentLocale()), ErrorCode.InternalError.getCode());
                } catch (ModuleNotFoundException m) {
                    throw new RenderingException(e, name(new Locale("en", "US")), ErrorCode.InternalError.getCode());
                }
            }
        }
    }
