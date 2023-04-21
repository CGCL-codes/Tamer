    public void testDoGetHttpError() throws Exception {
        setupBasic();
        expect(pipeline.execute(internalRequest)).andReturn(HttpResponse.notFound());
        replay();
        servlet.doGet(request, recorder);
        assertResponseOk(HttpResponse.SC_NOT_FOUND, "");
    }
