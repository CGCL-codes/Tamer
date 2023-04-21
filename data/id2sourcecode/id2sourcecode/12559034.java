    public OutputStreamZipTracer(String fileName) {
        try {
            OutputStream fos = new FileOutputStream(fileName);
            java.util.zip.ZipOutputStream zos;
            zos = new java.util.zip.ZipOutputStream(fos);
            zos.putNextEntry(new java.util.zip.ZipEntry("trace.xml"));
            fos = zos;
            setOutputStream(fos);
        } catch (IOException ex) {
            m_error = true;
            m_errorStr = ex.getMessage() + "\n" + ex.getStackTrace().toString();
        }
    }
