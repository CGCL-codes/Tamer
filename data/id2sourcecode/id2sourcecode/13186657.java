    public static String getRequestContent(HttpServletRequest request) throws IOException {
        String encoding = request.getCharacterEncoding();
        if (encoding == null) encoding = "UTF-8";
        try {
            ServletInputStream is = request.getInputStream();
            Reader reader = new InputStreamReader(is, encoding);
            StringWriter content = new StringWriter();
            char ch[] = new char[4096];
            int len = 0;
            while ((len = reader.read(ch)) > -1) content.write(ch, 0, len);
            return content.toString();
        } catch (UnsupportedEncodingException e) {
            throw new IOException("Unsupported character encoding in request content: " + encoding);
        }
    }
