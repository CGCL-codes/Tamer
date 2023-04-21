    @Override
    public boolean handleContents(Element result, RenderObjects renderer, TranslationHelpers helpers, Object tagHandlerProcessingData) {
        Object currentObject = renderer.getCurrentObject();
        DataSource documentDataSource = null;
        Element document = null;
        RenderObjects pageRenderer = null;
        InputStream in = null;
        if (currentObject instanceof WebsitePageComponent) {
            WebsitePageComponent pageComponent = (WebsitePageComponent) currentObject;
            Object page = pageComponent.getPage();
            InternalRequest request = helpers.getRequest();
            InternalPageResolver pageResolver = request.getInternalPageResolver();
            documentDataSource = pageResolver.getDocument(page.getClass(), request);
            pageRenderer = new RenderObjects(renderer, page);
        } else if (currentObject instanceof ExternalPageComponent) {
            ExternalPageComponent externalPageComponent = (ExternalPageComponent) currentObject;
            Object renderObject = externalPageComponent.getInsideContext();
            if (renderObject != null) {
                pageRenderer = new RenderObjects(renderer, renderObject);
            }
            String urlStr = externalPageComponent.getURL();
            urlStr = translateURL(urlStr, helpers.getRequest());
            try {
                URL url = new URL(urlStr);
                in = url.openStream();
            } catch (IOException e) {
                throw new TranslationException("couldn't load page component for " + "external page URL: " + urlStr, e);
            }
            documentDataSource = new InputStreamDataSource(in, "application/xhtml+xml", urlStr, ExternalPageComponent.class.getCanonicalName() + ":" + urlStr);
        } else if (currentObject instanceof CustomPageComponent) {
            CustomPageComponent customPageComponent = (CustomPageComponent) currentObject;
            Object renderObject = customPageComponent.getInsideContext();
            if (renderObject != null) {
                pageRenderer = new RenderObjects(renderer, renderObject);
            }
            document = customPageComponent.getDocument();
        } else {
            return false;
        }
        try {
            if (document == null) {
                DocumentTranslator translator = helpers.getDocumentTranslator();
                document = translator.getUntranslated(documentDataSource, XMLEntityResolving.NEVER).getRootElement();
            }
            if (in != null) {
                LightBoundUtil.closeQuietly(in);
            }
            addElements(result, document, helpers, pageRenderer);
            return true;
        } catch (IOException e) {
            throw new TranslationException("couldn't load page component for " + "page object: " + pageRenderer, e);
        }
    }
