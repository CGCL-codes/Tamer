    @Test(expected = GadgetException.class)
    public void badFetchThrows() throws Exception {
        HttpRequest request = createIgnoreCacheRequest();
        expect(pipeline.execute(request)).andReturn(HttpResponse.error());
        replay(pipeline);
        specFactory.getGadgetSpec(createContext(SPEC_URL, true));
    }
