    private boolean processTraceParameter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String traceCmd = WebUtil.getParameter(request, "Trace");
        String traceLevel = WebUtil.getParameter(request, "TraceLevel");
        if (traceLevel != null && traceLevel.length() > 0) {
            log.info("New Level: " + traceLevel);
            CLogMgt.setLevel(traceLevel);
            Ini.setProperty(Ini.P_TRACELEVEL, traceLevel);
            Ini.saveProperties(false);
            return false;
        }
        if (traceCmd == null || traceCmd.length() == 0) return false;
        log.info("Command: " + traceCmd);
        CLogFile fileHandler = CLogFile.get(false, null, false);
        if (traceCmd.equals("ROTATE")) {
            if (fileHandler != null) fileHandler.rotateLog();
            return false;
        } else if (traceCmd.equals("DELETE")) {
            File logDir = fileHandler.getLogDirectory();
            if (logDir != null && logDir.isDirectory()) {
                File[] logs = logDir.listFiles();
                for (int i = 0; i < logs.length; i++) {
                    String fileName = logs[i].getAbsolutePath();
                    if (fileName.equals(fileHandler.getFileName())) continue;
                    if (logs[i].delete()) log.warning("Deleted: " + fileName); else log.warning("Not Deleted: " + fileName);
                }
            }
            return false;
        }
        if (fileHandler != null && fileHandler.getFileName().equals(traceCmd)) fileHandler.flush();
        File file = new File(traceCmd);
        if (!file.exists()) {
            log.warning("Did not find File: " + traceCmd);
            return false;
        }
        if (file.length() == 0) {
            log.warning("File Length=0: " + traceCmd);
            return false;
        }
        log.info("Streaming: " + traceCmd);
        try {
            long time = System.currentTimeMillis();
            int fileLength = (int) file.length();
            int bufferSize = 2048;
            byte[] buffer = new byte[bufferSize];
            response.setContentType("text/plain");
            response.setBufferSize(bufferSize);
            response.setContentLength(fileLength);
            FileInputStream fis = new FileInputStream(file);
            ServletOutputStream out = response.getOutputStream();
            int read = 0;
            while ((read = fis.read(buffer)) > 0) out.write(buffer, 0, read);
            out.flush();
            out.close();
            fis.close();
            time = System.currentTimeMillis() - time;
            double speed = (fileLength / 1024) / ((double) time / 1000);
            log.info("length=" + fileLength + " - " + time + " ms - " + speed + " kB/sec");
        } catch (IOException ex) {
            log.log(Level.SEVERE, "stream" + ex);
        }
        return true;
    }
