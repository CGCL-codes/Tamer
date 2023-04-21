    public String downloadProjectFile() {
        try {
            System.out.println("download() : projectName --> " + projectName);
            System.out.println("nom du fichier : " + fileName);
            System.out.println("type : " + type);
            String hibouHome = System.getenv().get("HIBOU_HOME");
            String separator = System.getProperty("file.separator");
            String path = hibouHome + separator + projectName + separator + type + separator + fileName;
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
            if (type.equals("Archives")) {
                hibouWSPort.incrementDownloadStatistics();
            }
            return null;
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
    }
