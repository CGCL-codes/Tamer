    private void sendJTLToLoadosophia(File targetFile) throws IOException {
        if (targetFile.length() == 0) {
            throw new IOException("Cannot send empty file to Loadosophia.org");
        }
        HttpClient uploader = new HttpClient();
        PostMethod filePost = new PostMethod(getUploaderURI());
        Part[] parts = { new StringPart("projectKey", getProject()), new StringPart("uploadToken", getUploadToken()), new FilePart("jtl_file", new FilePartSource(gzipFile(targetFile))) };
        targetFile.delete();
        informUser("Starting upload to Loadosophia.org");
        filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
        int result = uploader.executeMethod(filePost);
        if (result != HttpStatus.SC_ACCEPTED) {
            String fname = targetFile.getAbsolutePath() + ".html";
            informUser("Saving server error response to: " + fname);
            FileOutputStream fos = new FileOutputStream(fname);
            FileChannel resultFile = fos.getChannel();
            resultFile.write(ByteBuffer.wrap(filePost.getResponseBody()));
            resultFile.close();
            HttpException $e = new HttpException("Upload returned not 202 ACCEPTED status: " + result);
            throw $e;
        }
        informUser("Finished upload to Loadosophia.org successfully");
        informUser("Go to https://loadosophia.org/service/upload/ to see processing status.");
    }
