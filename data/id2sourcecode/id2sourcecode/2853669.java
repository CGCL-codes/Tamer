    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (streamUsed != null && streamUsed == pw) {
            throw new IllegalStateException("Printwriter already obtained");
        }
        if (gzipServletOs == null) {
            gzipServletOs = new GzipServletOutputStream(response.getOutputStream());
            streamUsed = gzipServletOs;
        }
        return gzipServletOs;
    }
