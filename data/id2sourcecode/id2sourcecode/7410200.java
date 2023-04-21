    public String downloadArchive() {
        try {
            System.out.println("download() : projectName --> " + projectName);
            String fileName = projectName + ".zip";
            String hibouHome = System.getenv().get("HIBOU_HOME");
            String separator = System.getProperty("file.separator");
            String path = hibouHome + separator + projectName + separator + "Archives" + separator + fileName;
            File file = new File(path);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            HttpServletResponse response = ServletActionContext.getResponse();
            String mimetype = ServletActionContext.getServletContext().getMimeType(fileName);
            System.out.println("mimetype --> " + mimetype);
            response.setContentType(mimetype);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
            ServletOutputStream os = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            long l = file.length();
            for (long i = 0; i < l; i++) {
                bos.write(bis.read());
            }
            bos.flush();
            os.close();
            bis.close();
            hibouWSPort.incrementDownloadStatistics();
            return null;
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
    }
