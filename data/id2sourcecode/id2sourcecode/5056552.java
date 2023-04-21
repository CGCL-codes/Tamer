    private FileChannelStagelet createFileStagelet(File file, RequestHeader request, StageletStack stack) throws IOException {
        String contentType = mimeMap.getMimeType(file);
        FileChannelStagelet stage = new FileChannelStagelet(new FileInputStream(file).getChannel(), stack, request.isPersistentConnection(), request.getVerb() == Verb.HEAD);
        if (contentType == null) stage.getHeaders().setContentTypeAppOctet(); else stage.getHeaders().setContentType(contentType);
        return stage;
    }
