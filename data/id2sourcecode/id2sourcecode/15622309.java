    public Response executePost(Request request) throws IOException {
        Response response = new Response();
        URLConnection conn = destiny.openConnection();
        conn.setDoOutput(true);
        DataOutputStream out = null;
        BufferedReader reader = null;
        try {
            putCommonsHeaders(request);
            if (0 < request.getBody().getParameters().size()) {
                request.addHeader("Content-Type", "application/x-www-form-urlencoded");
            }
            String boundary = createBoundary();
            if (0 < request.getBody().getBinaryParameters().size()) {
                request.addHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
            }
            buildHeader(request, conn);
            out = new DataOutputStream(conn.getOutputStream());
            if (request.getHeader("Content-Type").startsWith("multipart/form-data")) {
                for (RequestParameter binParam : request.getBody().getBinaryParameters()) {
                    DataInputStream input = null;
                    String filename = null, filetype = null;
                    if (binParam.getValue() instanceof java.io.File) {
                        input = new DataInputStream(new FileInputStream((File) binParam.getValue()));
                        filename = ((File) binParam.getValue()).getName();
                        filetype = getMimeType(filename);
                    } else if (binParam.getValue() instanceof java.io.InputStream) {
                        input = new DataInputStream((java.io.InputStream) binParam.getValue());
                        filename = binParam.getName();
                        filetype = getMimeType(filename);
                    } else throw new InvalidParameterException("A implementacao atual somente suporta parametro binario do tipo File.");
                    out.writeBytes("--" + boundary + CR_LF);
                    out.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(binParam.getName(), "iso-8859-1") + "\"; filename=\"" + URLEncoder.encode(filename, "iso-8859-1") + "\"" + CR_LF);
                    out.writeBytes("Content-Type: " + filetype + CR_LF);
                    out.writeBytes("Content-Transfer-Encoding: binary" + CR_LF + CR_LF);
                    try {
                        byte[] buffer = new byte[2033];
                        int readed;
                        while (0 < (readed = input.read(buffer))) {
                            out.write(buffer, 0, readed);
                        }
                        out.writeBytes(CR_LF);
                    } finally {
                        input.close();
                    }
                }
                for (RequestParameter param : request.getBody().getParameters()) {
                    out.writeBytes("--" + boundary + CR_LF);
                    out.writeBytes("Content-Disposition: form-data; name=\"" + URLEncoder.encode(param.getName(), "iso-8859-1") + "\"" + CR_LF + CR_LF);
                    out.writeBytes(param.getValue().toString() + CR_LF);
                }
                if (0 < request.getBody().getParameters().size() || 0 < request.getBody().getBinaryParameters().size()) out.writeBytes("--" + boundary + "--" + CR_LF);
            }
            out.writeBytes(CR_LF);
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String plainResponse = "";
            while (null != (line = reader.readLine())) plainResponse += line;
            response.setContent(plainResponse);
            response.setContentType(conn.getContentType());
            Map<String, List<String>> headers = conn.getHeaderFields();
            Response.treat(((List<String>) headers.get(null)).get(0), response);
        } catch (IOException e) {
            Response.treat(conn.getHeaderField(null), response);
            if (response.getStatus() == 200) throw e;
        } finally {
            if (null != out) out.close();
            if (null != reader) reader.close();
        }
        return response;
    }
