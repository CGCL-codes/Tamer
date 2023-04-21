    protected UploadResult executeRequestAndParseResponse(FileUploadRequest request, File uploadedFile) throws ConnectionException, RequestCancelledException, ChangesOnServerException {
        HttpResponse response = executeRequest(request);
        UploadResult uploadResult = parseResponseBody(response.getResponseBody(), uploadedFile);
        closeReponse(response);
        return uploadResult;
    }
