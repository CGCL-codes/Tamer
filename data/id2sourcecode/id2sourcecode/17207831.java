    @Test
    public void returnsBackendResponseOnSuccess() throws Exception {
        expect(mockBackend.execute(host, req, ctx)).andReturn(resp);
        replay(mockBackend);
        HttpResponse result = impl.execute(host, req, ctx);
        verify(mockBackend);
        assertSame(resp, result);
    }
