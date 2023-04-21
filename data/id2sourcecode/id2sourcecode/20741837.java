    @Override
    public Response executePost(String url, String parameters, BinaryAttachment... binaryAttachments) throws IOException {
        if (binaryAttachments == null) binaryAttachments = new BinaryAttachment[] {};
        if (logger.isLoggable(INFO)) logger.info("Executing a POST to " + url + " with parameters " + (binaryAttachments.length > 0 ? "" : "(sent in request body): ") + urlDecode(parameters) + (binaryAttachments.length > 0 ? " and " + binaryAttachments.length + " binary attachment[s]." : ""));
        HttpURLConnection httpUrlConnection = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            httpUrlConnection = openConnection(new URL(url + (binaryAttachments.length > 0 ? "?" + parameters : "")));
            httpUrlConnection.setReadTimeout(DEFAULT_READ_TIMEOUT_IN_MS);
            customizeConnection(httpUrlConnection);
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setUseCaches(false);
            if (binaryAttachments.length > 0) {
                httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
                httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + MULTIPART_BOUNDARY);
            }
            httpUrlConnection.connect();
            outputStream = httpUrlConnection.getOutputStream();
            if (binaryAttachments.length > 0) {
                for (BinaryAttachment binaryAttachment : binaryAttachments) {
                    outputStream.write((MULTIPART_TWO_HYPHENS + MULTIPART_BOUNDARY + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + "Content-Disposition: form-data; name=\"" + createFormFieldName(binaryAttachment) + "\"; filename=\"" + binaryAttachment.getFilename() + "\"" + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).getBytes(ENCODING_CHARSET));
                    write(binaryAttachment.getData(), outputStream, MULTIPART_DEFAULT_BUFFER_SIZE);
                    outputStream.write((MULTIPART_CARRIAGE_RETURN_AND_NEWLINE + MULTIPART_TWO_HYPHENS + MULTIPART_BOUNDARY + MULTIPART_TWO_HYPHENS + MULTIPART_CARRIAGE_RETURN_AND_NEWLINE).getBytes(ENCODING_CHARSET));
                }
            } else {
                outputStream.write(parameters.getBytes(ENCODING_CHARSET));
            }
            if (logger.isLoggable(FINER)) logger.finer("Response headers: " + httpUrlConnection.getHeaderFields());
            try {
                inputStream = httpUrlConnection.getResponseCode() != HTTP_OK ? httpUrlConnection.getErrorStream() : httpUrlConnection.getInputStream();
            } catch (IOException e) {
                if (logger.isLoggable(WARNING)) logger.warning("An error occurred while POSTing to " + url + ": " + e);
            }
            return new Response(httpUrlConnection.getResponseCode(), fromInputStream(inputStream));
        } finally {
            if (binaryAttachments.length > 0) for (BinaryAttachment binaryAttachment : binaryAttachments) closeQuietly(binaryAttachment.getData());
            closeQuietly(outputStream);
            closeQuietly(httpUrlConnection);
        }
    }
