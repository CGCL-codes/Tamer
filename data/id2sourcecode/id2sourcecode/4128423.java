    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String text = request.getParameter("text");
            if (text == null || text.trim().length() == 0) {
                text = "You didn't enter any text.";
            }
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph(String.format("You have submitted the following text using the %s method:", request.getMethod())));
            document.add(new Paragraph(text));
            document.close();
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setContentType("application/pdf");
            response.setContentLength(baos.size());
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }
