    private void downloadReferBibIX(HttpServletResponse response, HashMap docs) throws IOException {
        StringBuffer buffer = new StringBuffer();
        for (Iterator it = docs.keySet().iterator(); it.hasNext(); ) {
            ThinDoc doc = (ThinDoc) docs.get(it.next());
            buffer.append(BiblioTransformer.toReferBibIX(doc));
            buffer.append("\n");
        }
        BufferedInputStream input = new BufferedInputStream(new ByteArrayInputStream(buffer.toString().getBytes()));
        BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
        response.reset();
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment; filename=\"metacart.txt\"");
        int contentLength = input.available();
        response.setContentLength(contentLength);
        output = new BufferedOutputStream(response.getOutputStream());
        while (contentLength-- > 0) {
            output.write(input.read());
        }
        output.flush();
    }
