    protected void transformContent(final Page.Request pageRequest, final Channel.ViewResponse viewResponse) throws WWWeeePortal.Exception {
        final List<Map.Entry<URL, Templates>> transformations = getTransformations(pageRequest);
        if (transformations == null) return;
        final Element originalContentContainerElement = viewResponse.getContentContainerElement();
        final Element originalDocumentElement = DOMUtil.getChildElement(originalContentContainerElement, null, null);
        final TransformableDocument transformableDocument = new TransformableDocument(this);
        if (originalDocumentElement != null) {
            final Element transformSourceDocumentElement;
            synchronized (DOMUtil.getDocument(originalContentContainerElement)) {
                transformSourceDocumentElement = (Element) transformableDocument.getDocument().adoptNode(originalDocumentElement);
                if (originalDocumentElement.getNamespaceURI() != null) DOMUtil.createAttribute(XMLUtil.XMLNS_ATTRIBUTE_NS_URI, null, XMLConstants.XMLNS_ATTRIBUTE, originalDocumentElement.getNamespaceURI(), transformSourceDocumentElement);
            }
            transformableDocument.getDocument().appendChild(transformSourceDocumentElement);
        }
        final Document transformResultContentDocument = DOMUtil.newDocument();
        final DOMResult transformContentResult = new DOMResult(transformResultContentDocument);
        final Map<String, Object> transformProps = new HashMap<String, Object>();
        pageRequest.getMetaProps(transformProps);
        getPortal().getMetaProps(transformProps, pageRequest.getSecurityContext(), pageRequest.getHttpHeaders());
        pageRequest.getPage().getMetaProps(pageRequest, transformProps);
        getChannel().getMetaProps(pageRequest, transformProps);
        getMetaProps(pageRequest, transformProps);
        transformableDocument.setMediaType(ConversionUtil.invokeConverter(RESTUtil.MEDIA_TYPE_MIME_TYPE_CONVERTER, viewResponse.getContentType()));
        transformableDocument.setTransformations(transformations);
        transformableDocument.setTransformationParameters(transformProps);
        try {
            transformableDocument.transform(transformContentResult, viewResponse);
        } catch (Exception e) {
            throw new ConfigManager.ConfigException(e);
        }
        DOMUtil.appendChild(originalContentContainerElement, transformResultContentDocument);
        try {
            viewResponse.setContentType(ConversionUtil.invokeConverter(RESTUtil.MIME_TYPE_MEDIA_TYPE_CONVERTER, transformableDocument.getOutputMediaType()));
        } catch (MimeTypeParseException mtpe) {
            throw new ConfigManager.ConfigException("Transformation has invalid media type", mtpe);
        } catch (UnsupportedCharsetException uce) {
            throw new ConfigManager.ConfigException("Transformation has invalid encoding", uce);
        }
        return;
    }
