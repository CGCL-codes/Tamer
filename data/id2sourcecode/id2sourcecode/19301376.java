    protected void createHeadMetaElements(final Request request, final Element headElement, final Map<String, Object> publishMetaProps) throws WWWeeePortal.Exception {
        for (Element metaLinkElement : getMetaLinkElements(request)) {
            DOMUtil.appendChild(headElement, MarkupManager.resolveMetaLinkElementContextResources(metaLinkElement, request.getUriInfo()));
        }
        for (Element metaScriptElement : getMetaScriptElements(request)) {
            DOMUtil.appendChild(headElement, MarkupManager.resolveMetaScriptElementContextResources(metaScriptElement, request.getUriInfo()));
        }
        for (Element metaMetaElement : getMetaMetaElements(request)) {
            DOMUtil.appendChild(headElement, MarkupManager.resolveMetaMetaElementContextResources(metaMetaElement, request.getUriInfo()));
        }
        for (Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>> channelViewTask : request.getChannelViewTasks()) {
            final Channel.ViewResponse channelViewResponse = getChannelViewResponse(channelViewTask.getValue());
            for (Element metaElement : CollectionUtil.mkNotNull(channelViewResponse.getMetaElements())) {
                DOMUtil.appendChild(headElement, metaElement);
            }
        }
        if (publishMetaProps != null) {
            for (String key : publishMetaProps.keySet()) {
                final Object value = publishMetaProps.get(key);
                final Element metaElement = DOMUtil.createElement(HTMLUtil.HTML_NS_URI, null, "meta", headElement);
                metaElement.setAttributeNS(null, "name", key);
                if (value != null) metaElement.setAttributeNS(null, "content", value.toString());
                metaElement.setAttributeNS(null, "scheme", "wwweee_internal");
            }
        }
        return;
    }
