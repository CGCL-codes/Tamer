    public void download(String filename, InputStream reportStream, Map params, List list) throws ReportException {
        String pathReport = filename;
        File report = new File(pathReport);
        byte[] buffer = null;
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");
            if (params == null) {
                params = new HashMap();
            }
            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(list);
            JasperPrint print = JasperFillManager.fillReport(reportStream, params, ds);
            JasperExportManager.exportReportToPdfFile(print, pathReport);
            FileInputStream fis = new FileInputStream(report);
            OutputStream os = response.getOutputStream();
            int read = 0;
            buffer = new byte[1024];
            while ((read = fis.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
            os.close();
            fis.close();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Throwable ex) {
            throw new ReportException(ex);
        } finally {
            buffer = null;
            report.delete();
        }
    }
