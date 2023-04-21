    protected void printAfterParameterCheck(URL templateURL, URL dataURL, OutputFormat outputFormat, URL destURL) throws PrintException {
        log.debug("Printing... " + templateURL);
        InputStream inputStream = null;
        Reader dataReader = null;
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            byte[] t = (byte[]) getFromCache(templateURL);
            inputStream = new ByteArrayInputStream(t);
            try {
                reader = ITextHelper.createPdfReader(inputStream);
            } catch (PrintException e) {
                throw new RuntimeException("template not found " + templateURL, e);
            }
            if (log.isDebugEnabled()) {
                log.debug("create PDFReader for" + templateURL);
            }
            URLConnection urlConnection = null;
            try {
                urlConnection = destURL.openConnection();
                urlConnection.setDoOutput(true);
                stamper = ITextHelper.createPdfStamper(reader, urlConnection.getOutputStream(), getAttributes());
            } catch (IOException e) {
                throw new RuntimeException("destURL not found " + destURL, e);
            }
            if (log.isDebugEnabled()) {
                log.debug("create PDFStamper for" + templateURL);
            }
            try {
                ITextHelper.mergeFormFields(stamper, templateURL, dataURL);
            } catch (IOException e) {
                throw new RuntimeException("merge fields ", e);
            }
            if (log.isDebugEnabled()) {
                log.debug("merge Formfields");
            }
        } finally {
            ITextHelper.close(stamper);
            IOUtil.close(dataReader);
            IOUtil.close(inputStream);
            ITextHelper.close(reader);
        }
    }
