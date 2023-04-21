    @SuppressWarnings("unchecked")
    public static Object executeRequest(HttpUriRequest req) throws IOException {
        HttpClient client = getClient(req.getURI().toString());
        HttpResponse response = client.execute(req);
        Object result = parseAsJSON(response);
        assert result != null : "Invariant: result can't be null.";
        return result;
    }
