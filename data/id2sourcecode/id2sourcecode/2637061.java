    protected void createDecorator() {
        _outputStream = new java.io.ByteArrayOutputStream();
        _zipOutputStream = new java.util.zip.ZipOutputStream(_outputStream);
        try {
            _zipOutputStream.putNextEntry(new java.util.zip.ZipEntry("trace.xml"));
        } catch (java.io.IOException ex) {
            _error = true;
            _errorMsg = "Unexpected error while creating ZIP entry.";
            _errorMsg += ex.getMessage();
            return;
        }
        _decorator = new OutputStreamTracer(_zipOutputStream);
    }
