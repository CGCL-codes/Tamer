    public boolean writeURLContent(String url, OutputStream out, String requiredMime) throws IOException {
        HttpClient client = this.getHttpClient(CONNECTION_RETRIES, TIMEOUT_CONNECTION);
        GetMethod consulta = new GetMethod(url);
        try {
            client.executeMethod(consulta);
            Header tipoMimeHead = consulta.getResponseHeader("Content-Type");
            String tipoMimeResp = "";
            if (tipoMimeHead != null) tipoMimeResp = tipoMimeHead.getValue();
            if (consulta.getStatusCode() >= STATUS_OK_START && consulta.getStatusCode() <= STATUS_OK_END && ((requiredMime == null) || ((tipoMimeResp != null) && tipoMimeResp.contains(requiredMime)))) {
                InputStream in = consulta.getResponseBodyAsStream();
                byte[] b = new byte[4 * 1024];
                int read;
                while ((read = in.read(b)) != -1) {
                    out.write(b, 0, read);
                }
                consulta.releaseConnection();
                return true;
            } else {
                return false;
            }
        } finally {
            consulta.releaseConnection();
        }
    }
