    public ModelAndView handleRenderRequestAfterValidation(RenderRequest request, RenderResponse response) throws Exception {
        Map model = new HashMap();
        File tempFile = TempFileUtil.createTempFile("logfiles");
        FileOutputStream fo = new FileOutputStream(tempFile);
        ZipOutputStream zipOut = new ZipOutputStream(fo);
        FilterOutputStream wrapper = new FilterOutputStream(zipOut) {

            public void close() {
            }
        };
        File logDirectory = new File(SpringContextUtil.getServletContext().getRealPath("/WEB-INF/logs"));
        for (String logFile : logDirectory.list(new FilenameFilter() {

            public boolean accept(File file, String filename) {
                return filename.startsWith("ssf.log");
            }
        })) {
            zipOut.putNextEntry(new ZipEntry(logFile));
            FileCopyUtils.copy(new FileInputStream(new File(logDirectory, logFile)), wrapper);
        }
        zipOut.finish();
        model.put(WebKeys.DOWNLOAD_URL, WebUrlUtil.getServletRootURL(request) + WebKeys.SERVLET_VIEW_FILE + "?viewType=zipped&fileId=" + tempFile.getName() + "&" + WebKeys.URL_FILE_TITLE + "=logfiles.zip");
        return new ModelAndView("administration/close_button", model);
    }
