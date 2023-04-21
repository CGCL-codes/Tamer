    public void write(Map<String, Object> params) throws IOException {
        LineWriter writer = new LineWriter(this.out);
        for (Map.Entry<String, Object> param : params.entrySet()) {
            writer.println(MULTIPART_BOUNDARY_SEPARATOR);
            if (param.getValue() instanceof BinaryAttachment) {
                BinaryAttachment ba = (BinaryAttachment) param.getValue();
                writer.println("Content-Disposition: form-data; name=\"" + StringUtils.urlEncode(param.getKey()) + "\"; filename=\"" + StringUtils.urlEncode(ba.filename) + "\"");
                writer.println("Content-Type: " + ba.contentType);
                writer.println("Content-Transfer-Encoding: binary");
                writer.println();
                writer.flush();
                int read;
                byte[] chunk = new byte[8192];
                while ((read = ba.data.read(chunk)) > 0) this.out.write(chunk, 0, read);
            } else {
                writer.println("Content-Disposition: form-data; name=\"" + StringUtils.urlEncode(param.getKey()) + "\"");
                writer.println();
                writer.println(StringUtils.urlEncode(param.getValue().toString()));
            }
        }
        writer.println(MULTIPART_BOUNDARY_END);
        writer.flush();
    }
