        public String getBodyAsString() throws IOException {
            Body body = bodyPart.getBody();
            String result = null;
            if (body instanceof TextBody) {
                Reader reader = ((TextBody) body).getReader();
                try {
                    StringWriter writer = new StringWriter();
                    char[] buffer = new char[4048];
                    int n = 0;
                    while ((n = reader.read(buffer)) != -1) writer.write(buffer, 0, n);
                    result = writer.toString();
                } finally {
                    reader.close();
                }
            } else if (body instanceof BinaryBody) {
                InputStream inputStream = ((BinaryBody) body).getInputStream();
                InputStreamReader inputStreamReader = null;
                try {
                    String charset = contentType.getParameters().get("charset");
                    if (charset != null) charset = CharsetUtil.toJavaCharset(charset);
                    inputStreamReader = charset == null ? new InputStreamReader(inputStream) : new InputStreamReader(inputStream, charset);
                    StringWriter writer = new StringWriter();
                    char[] buffer = new char[4048];
                    int n = 0;
                    while ((n = inputStreamReader.read(buffer)) != -1) writer.write(buffer, 0, n);
                    result = writer.toString();
                } finally {
                    if (inputStreamReader != null) inputStreamReader.close();
                    inputStream.close();
                }
            }
            return result;
        }
