    private HttpURLConnection getURLConnection(FileId fileId, boolean update) throws Exception {
        HttpURLConnection urlc = null;
        urlc = (HttpURLConnection) idsURL.openConnection();
        urlc.setRequestMethod("POST");
        urlc.setDoOutput(true);
        urlc.setDoInput(true);
        urlc.setUseCaches(false);
        urlc.setAllowUserInteraction(false);
        urlc.addRequestProperty("sessionid", sessionId);
        urlc.addRequestProperty("filename", fileId.getName());
        urlc.addRequestProperty("filedirectory", fileId.getLocation());
        urlc.addRequestProperty("datastreamname", fileId.getName());
        urlc.addRequestProperty("datafileformat", getFileExtension(fileId));
        urlc.addRequestProperty("datasetid", Long.toString(fileId.getDatasetId()));
        urlc.addRequestProperty("update", update ? "True" : "False");
        urlc.addRequestProperty("content-type", "application/octet-stream");
        return urlc;
    }
