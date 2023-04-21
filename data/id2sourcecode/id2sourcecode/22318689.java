    public HttpResponse makeRequest() throws IOException, TimeoutException, RequestCancelledException {
        checkState();
        try {
            request = createRequest();
            org.apache.http.HttpResponse response = httpClient.execute(request, context);
            if (checkAbortFlag()) {
                throw new RequestCancelledException();
            }
            return handleRedirectIfNeeded(response);
        } catch (SocketTimeoutException ex) {
            throw new TimeoutException();
        } catch (ConnectTimeoutException ex) {
            throw new TimeoutException();
        } catch (InterruptedIOException ex) {
            throw new RequestCancelledException();
        } catch (SocketException ex) {
            throw new RequestCancelledException();
        } catch (URISyntaxException ex) {
            throw new IOException("Invalid URL");
        }
    }
