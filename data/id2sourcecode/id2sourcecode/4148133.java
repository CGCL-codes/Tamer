    private void getPrintJobCommand(String uuid, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        String jobId = request.getParameter(ParameterKeys.PRINT_JOB_ID);
        StringBuffer responseOutput = new StringBuffer("");
        IRemotePrintJobManager manager = RemotePrintJobManagerFactory.getRemotePrintJobManager();
        RemotePrintJob printJob = manager.getRemotePrintJob(Long.parseLong(jobId));
        InputStream input = null;
        if (RemotePrintServiceLookup.isMobile(uuid)) {
            input = ConversionServerUtils.INSTANCE.convertToMobile(printJob.getPrintServiceName(), printJob.getPrintData());
        } else {
            input = printJob.getPrintData();
        }
        if (input != null) {
            OutputStream output = response.getOutputStream();
            while (input.available() > 0) {
                output.write(input.read());
            }
            input.close();
        } else {
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.write(responseOutput.toString());
        }
    }
