    protected void download(String srcFullName, String saveAsName) {
        HttpServletResponse response = ServletActionContext.getResponse();
        InputStream is = null;
        OutputStream os = null;
        try {
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + saveAsName);
            os = response.getOutputStream();
            is = new FileInputStream(srcFullName);
            byte[] b = new byte[1024];
            while (is.read(b) != -1) os.write(b);
            b = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                ServletActionContext.getPageContext().getOut().clear();
                ServletActionContext.getPageContext().pushBody();
            } catch (Exception e) {
            }
            try {
                os.close();
                is.close();
            } catch (Exception e) {
            }
        }
    }
