    @Test
    public void staleSpecReturnedFromCacheOnError() throws Exception {
        HttpRequest request = createIgnoreCacheRequest();
        HttpRequest retriedRequest = createCacheableRequest();
        HttpResponse expiredResponse = new HttpResponseBuilder().setResponse(LOCAL_SPEC_XML.getBytes("UTF-8")).addHeader("Pragma", "no-cache").create();
        expect(pipeline.execute(request)).andReturn(expiredResponse);
        expect(pipeline.execute(retriedRequest)).andReturn(HttpResponse.notFound()).once();
        replay(pipeline);
        specFactory.getGadgetSpec(createContext(SPEC_URL, true));
        SoftExpiringCache.CachedObject<Object> inCache = specFactory.cache.getElement(SPEC_URL);
        specFactory.cache.addElement(SPEC_URL, inCache.obj, -1);
        GadgetSpec spec = specFactory.getGadgetSpec(createContext(SPEC_URL, false));
        assertEquals(ALT_LOCAL_CONTENT, spec.getView(GadgetSpec.DEFAULT_VIEW).getContent());
    }
