    public MultipartRequestWrapper(final ServletRequest request) throws IOException {
        super((HttpServletRequest) request);
        final String contentType = request.getContentType();
        if (!contentType.toLowerCase().startsWith("multipart/form-data")) {
            throw new IOException("The MIME Content-Type of the Request must be " + '"' + "multipart/form-data" + '"' + ", not " + '"' + contentType + '"' + ".");
        } else if (request.getAttribute(MultipartRequestWrapper.MPH_ATTRIBUTE) != null) {
            final MultipartRequestWrapper oldMPH = (MultipartRequestWrapper) request.getAttribute(MultipartRequestWrapper.MPH_ATTRIBUTE);
            stringParameters = oldMPH.stringParameters;
            mimeTypes = oldMPH.mimeTypes;
            tempFileNames = oldMPH.tempFileNames;
            uploadFileNames = oldMPH.uploadFileNames;
            return;
        }
        try {
            final ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            final InputStream inputServlet = request.getInputStream();
            final byte buffer[] = new byte[2048];
            int readBytes = inputServlet.read(buffer);
            while (readBytes != -1) {
                byteArray.write(buffer, 0, readBytes);
                readBytes = inputServlet.read(buffer);
            }
            inputServlet.close();
            MimeMultipart parts = new MimeMultipart(new MultipartRequestWrapperDataSource(contentType, byteArray.toByteArray()));
            byteArray.close();
            final Map<String, String[]> parameters = new HashMap<String, String[]>();
            final Map<String, String> mimes = new HashMap<String, String>();
            final Map<String, File> fileNames = new HashMap<String, File>();
            final Map<String, String> uploadNames = new HashMap<String, String>();
            String encoding = request.getCharacterEncoding();
            if (encoding == null) {
                encoding = "8859_1";
            }
            for (int loopCount = 0; loopCount < parts.getCount(); loopCount++) {
                final MimeBodyPart current = (MimeBodyPart) parts.getBodyPart(loopCount);
                final String headers = current.getHeader("Content-Disposition", "; ");
                if (headers.indexOf(" name=" + '"') == -1) {
                    throw new MessagingException("No name header found in " + "Content-Disposition field.");
                } else {
                    String namePart = headers.substring(headers.indexOf(" name=\"") + 7);
                    namePart = namePart.substring(0, namePart.indexOf('"'));
                    final String nameField = javax.mail.internet.MimeUtility.decodeText(namePart);
                    final InputStream inRaw = current.getInputStream();
                    if (headers.indexOf(" filename=" + '"') != -1) {
                        String fileName = headers.substring(headers.indexOf(" filename=" + '"') + 11);
                        fileName = fileName.substring(0, fileName.indexOf('"'));
                        if (fileName.lastIndexOf('/') != -1) {
                            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                        }
                        if (fileName.lastIndexOf('\\') != -1) {
                            fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
                        }
                        uploadNames.put(nameField, fileName);
                        if (fileNames.containsKey(nameField)) {
                            throw new IOException("Multiple parameters named " + nameField);
                        }
                        if (current.getContentType() == null) {
                            mimes.put(nameField, "text/plain");
                        } else {
                            mimes.put(nameField, current.getContentType());
                        }
                        final File tempFile = File.createTempFile("mph", ".tmp");
                        final OutputStream outStream = new FileOutputStream(tempFile, Boolean.TRUE);
                        while ((readBytes = inRaw.read(buffer)) != -1) {
                            outStream.write(buffer, 0, readBytes);
                        }
                        inRaw.close();
                        outStream.close();
                        fileNames.put(nameField, tempFile.getAbsoluteFile());
                    } else {
                        final byte[] stash = new byte[inRaw.available()];
                        inRaw.read(stash);
                        inRaw.close();
                        final Object oldParam = parameters.get(nameField);
                        if (oldParam == null) {
                            parameters.put(nameField, new String[] { new String(stash, encoding) });
                        } else {
                            final String oldParams[] = (String[]) oldParam;
                            final String newParams[] = new String[oldParams.length + 1];
                            System.arraycopy(oldParams, 0, newParams, 0, oldParams.length);
                            newParams[oldParams.length] = new String(stash, encoding);
                            parameters.put(nameField, newParams);
                        }
                    }
                }
            }
            parts = null;
            stringParameters = Collections.unmodifiableMap(parameters);
            mimeTypes = Collections.unmodifiableMap(mimes);
            tempFileNames = Collections.unmodifiableMap(fileNames);
            uploadFileNames = Collections.unmodifiableMap(uploadNames);
            request.setAttribute(MultipartRequestWrapper.MPH_ATTRIBUTE, this);
        } catch (final MessagingException errMime) {
            throw new IOException(errMime.toString());
        }
    }
