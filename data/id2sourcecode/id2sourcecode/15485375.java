    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String appender = request.getParameter("appender");
        if (SessionManagement.isValid(request.getSession())) {
            try {
                LoggingConfigurator conf = new LoggingConfigurator();
                String file = conf.getFile(appender, true);
                InputStream is = new BufferedInputStream(new FileInputStream(file));
                byte[] buf = new byte[1024];
                int read = 1;
                while (read > 0) {
                    read = is.read(buf, 0, buf.length);
                    if (read > 0) {
                        response.getOutputStream().write(buf, 0, read);
                    }
                }
                is.close();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
