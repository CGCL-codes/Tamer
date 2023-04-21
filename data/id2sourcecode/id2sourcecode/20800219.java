    private void transferFile(HttpServletResponse response, byte[] transferData) throws ServletException {
        ServletOutputStream stream = null;
        BufferedInputStream buf = null;
        try {
            stream = response.getOutputStream();
            response.setContentType(OpenTratsConstants.MIME_TYPE_PDF);
            response.addHeader("Content-Disposition", "inline; filename=HistorialCliente.pdf");
            response.setContentLength((int) transferData.length);
            ByteArrayInputStream bis = new ByteArrayInputStream(transferData);
            buf = new BufferedInputStream(bis);
            int readBytes = 0;
            while ((readBytes = buf.read()) != -1) {
                stream.write(readBytes);
            }
            stream.close();
            buf.close();
        } catch (IOException ioe) {
            throw new ServletException(ioe.getMessage());
        }
    }
