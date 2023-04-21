    protected void performHttpRequest(final ClientHttpRequest request) throws IOException {
        final ClientHttpResponse response = request.execute();
        final HttpStatus statusCode = response.getStatusCode();
        if (Series.SUCCESSFUL.equals(statusCode.series()) == false) {
            final String message = String.format("Error performing update request. Received HTTP status code %d %s with message: %s", statusCode.value(), statusCode.name(), response.getStatusText());
            throw new UpdateException(message);
        }
    }
