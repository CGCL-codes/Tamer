    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String documentId = request.getParameter("documentId");
            String directory = DNSUtil.getProperty("dns.upload.review.report.path");
            Document document = Locator.lookupService(DocumentService.class).getDocumentById(Integer.parseInt(documentId));
            String fileName = document.getReviewReportName();
            File f = new File(directory + fileName);
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset();
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            OutputStream out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) out.write(buf, 0, len);
            br.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
