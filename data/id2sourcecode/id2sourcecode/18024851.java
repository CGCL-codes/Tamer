    public void testDoPostHttpError() throws Exception {
        setupPost();
        expect(pipeline.execute(internalRequest)).andReturn(HttpResponse.notFound());
        replay();
        servlet.doGet(request, recorder);
        assertResponseOk(HttpResponse.SC_NOT_FOUND, "");
    }
