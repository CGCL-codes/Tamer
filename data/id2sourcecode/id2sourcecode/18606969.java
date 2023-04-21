    @Mock
    public HttpResponse execute(HttpUriRequest req) {
        URI uri = req.getURI();
        final String response;
        if ("www.jhall.demon.co.uk".equals(uri.getHost())) {
            response = "<h3>Currency Data</h3>\r\n" + "<table><tr>\r\n" + "  <td valign=top>USD</td>\r\n" + "  <td valign=top>EUR</td>\r\n" + "  <td valign=top>BRL</td>\r\n" + "  <td valign=top>CNY</td>\r\n" + "</tr></table>";
        } else {
            String[] params = uri.getQuery().split("&");
            response = formatResultContainingCurrencyConversion(params);
        }
        return new BasicHttpResponse(req.getProtocolVersion(), 200, "OK") {

            @Override
            public HttpEntity getEntity() {
                return createHttpResponse(response);
            }
        };
    }
