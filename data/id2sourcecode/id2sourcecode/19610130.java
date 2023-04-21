    public String shorten(String url) throws IOException {
        HttpClient httpClient = HttpClient.createInstance();
        FormSubmitRequest request = httpClient.createFormSubmitRequest();
        request.setUrl("http://tnij.org/skracaj.php");
        request.addParameter("url_do_skrocenia", url);
        request.addParameter("maskowanie", "0");
        request.addParameter("ukrywanie", "1");
        try {
            HttpResponse response = httpClient.execute(request);
            String shortenLink = parseResponseBody(response.getResponseBody());
            response.close();
            return shortenLink;
        } catch (RequestCancelledException ex) {
            return null;
        } catch (TimeoutException ex) {
            return null;
        }
    }
