    public void testDoGetHttpError() throws Exception {
        setupGet();
        expect(pipeline.execute(internalRequest)).andReturn(HttpResponse.notFound());
        replay();
        servlet.doGet(request, recorder);
        assertResponseOk(HttpResponse.SC_NOT_FOUND, "");
    }
