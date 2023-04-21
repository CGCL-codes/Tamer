    public static void start(final int webserverPort, final String hostname, final int gameserverPort) {
        org.mortbay.jetty.Server server = new org.mortbay.jetty.Server(webserverPort);
        final long time = (new Date()).getTime();
        Handler handler = new AbstractHandler() {

            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
                if (target.equals("/")) {
                    target = "/index.html";
                }
                if (target.endsWith(".gif")) {
                    response.setContentType("image/gif");
                } else if (target.endsWith(".jpeg") || target.endsWith(".jpg")) {
                    response.setContentType("image/jpeg");
                } else if (target.endsWith(".htm") || target.endsWith(".html")) {
                    response.setContentType("text/html");
                } else if (target.endsWith(".css")) {
                    response.setContentType("text/css");
                } else if (target.endsWith(".jnlp")) {
                    response.setContentType("application/x-java-jnlp-file");
                } else if (target.endsWith(".jar")) {
                    response.setContentType("application/java-archive");
                }
                response.setStatus(HttpServletResponse.SC_OK);
                response.setDateHeader("Date", time);
                response.setDateHeader("Last-Modified", time);
                InputStream inputStream = this.getClass().getResourceAsStream("/htdocs" + target);
                if (inputStream == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    logger.warn("file not found: /htdocs" + target);
                } else {
                    if (target.endsWith(".jnlp")) {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        while (bufferedReader.ready()) {
                            String line = bufferedReader.readLine();
                            line = line.replace("HOSTNAME", hostname);
                            line = line.replace("WEBSERVERPORT", String.valueOf(webserverPort));
                            line = line.replace("GAMESERVERPORT", String.valueOf(gameserverPort));
                            response.getOutputStream().println(line);
                        }
                    } else {
                        while (inputStream.available() > 0) {
                            response.getOutputStream().write(inputStream.read());
                        }
                    }
                }
                ((Request) request).setHandled(true);
            }
        };
        server.setHandler(handler);
        try {
            server.start();
        } catch (BindException e) {
            logger.error("could not start webserver, " + "because port could not be used.");
        } catch (Exception e) {
            logger.error("error while starting webserver");
        }
    }
