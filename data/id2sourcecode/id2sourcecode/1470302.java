    public void run() {
        HttpURLConnection connection = null;
        try {
            Result result = new Result();
            result.setStartTime(new Date());
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            String status = connection.getHeaderField(null);
            InputStream instream = connection.getInputStream();
            byte[] buffer = new byte[1024];
            while (instream.read(buffer) != -1) {
            }
            result.setEndTime(new Date());
            result.setStatus(status);
            Report.getDefaultReport().addResult(result);
            LoggerUtil.getDefaultLoggerUtil().debug("Thread:" + this.getId() + "\t" + result.getStartTime() + "\t" + result.getEndTime());
        } catch (Exception e) {
            LoggerUtil.getDefaultLoggerUtil().error(e.getMessage(), e);
        } finally {
            connection.disconnect();
        }
        LoggerUtil.getDefaultLoggerUtil().info("Thread:" + this.getId() + "\t" + this.targetUrl);
    }
