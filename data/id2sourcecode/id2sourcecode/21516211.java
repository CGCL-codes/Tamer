    public synchronized OutputStream getOutputStream() throws IOException {
        try {
            if (!doOutput) {
                throw new ProtocolException("cannot write to a URLConnection" + " if doOutput=false - call setDoOutput(true)");
            }
            if (method.equals("GET")) {
                method = "POST";
            }
            if (!"POST".equals(method) && !"PUT".equals(method) && "http".equals(url.getProtocol())) {
                throw new ProtocolException("HTTP method " + method + " doesn't support output");
            }
            if (inputStream != null) {
                throw new ProtocolException("Cannot write output after reading input.");
            }
            if (!checkReuseConnection()) connect();
            ps = (PrintStream) http.getOutputStream();
            if (poster == null) poster = new PosterOutputStream();
            return poster;
        } catch (RuntimeException e) {
            disconnectInternal();
            throw e;
        } catch (IOException e) {
            disconnectInternal();
            throw e;
        }
    }
