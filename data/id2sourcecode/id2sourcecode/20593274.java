    @RequestMapping(value = "/flex/downloadCustomerTemplate.do", method = RequestMethod.GET)
    public void downloadCustomerTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "customerTemplate.xls";
        String filePath = this.getClass().getClassLoader().getResource("").getPath() + "template" + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(file));
        byte[] buf = new byte[1024];
        int len = 0;
        response.setContentType("application/vnd.ms-excel; charset=UTF-8");
        response.setHeader("Content-Disposition", "filename=" + fileName);
        response.setHeader("Cache-Control", "no-cache");
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0) out.write(buf, 0, len);
        br.close();
        out.close();
    }
