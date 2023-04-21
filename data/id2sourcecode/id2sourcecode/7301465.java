    public void handle(Request request, Response response) throws Exception {
        response.setContentType(ContentType.TEXT_HTML);
        String uri = request.getRequestUri();
        File file = new File(ROOT + uri);
        if (file.isDirectory()) {
            Writer writer = response.getWriter();
            writer.write("<ul>");
            File[] children = file.listFiles();
            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                if (child.isDirectory()) {
                    writer.write("<li><a href='" + child.getName() + "/'>" + child.getName() + "/</a>");
                } else {
                    writer.write("<li><a href='" + child.getName() + "'>" + child.getName() + "</a> ");
                    writer.write(" (" + child.length() + " bytes)");
                }
            }
            writer.write("</ul>");
        } else {
            String ext = IOUtil.getExtention(file.getName()).toLowerCase();
            String contentType = (String) mimeTypes.get(ext);
            OutputStream outputStream = response.getOutputStream();
            InputStream inputStream = null;
            try {
                if (contentType == null) {
                    contentType = ContentType.OCTET_STREAM;
                }
                response.setContentType(contentType);
                inputStream = IOUtil.openInputStream(file);
                IOUtil.copy(inputStream, outputStream);
            } finally {
                IOUtil.close(inputStream);
            }
        }
    }
