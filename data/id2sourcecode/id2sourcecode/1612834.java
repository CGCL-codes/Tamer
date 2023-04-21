    public void fillOutputData(OutputData outputData) throws TransferFailedException {
        Resource resource = outputData.getResource();
        try {
            URL url = new URL(buildUrl(resource.getName()));
            putConnection = (HttpURLConnection) url.openConnection();
            addHeaders(putConnection);
            putConnection.setRequestMethod("PUT");
            putConnection.setDoOutput(true);
            outputData.setOutputStream(putConnection.getOutputStream());
        } catch (IOException e) {
            throw new TransferFailedException("Error transferring file: " + e.getMessage(), e);
        }
    }
