    public static void main(String[] arguments) throws Exception {
        try {
            URL url = new URL("http://localhost:" + PORT + "/__SHUTDOWN__/");
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            in.close();
        } catch (SocketException ex) {
        }
        Server server = new Server(PORT);
        final Context testContext = new Context(server, CONTEXT_PATH, Context.SESSIONS);
        testContext.addServlet(new ServletHolder(SERVLET_CLASS), PATH_SPEC);
        Context shutdownContext = new Context(server, "/__SHUTDOWN__");
        shutdownContext.addServlet(new ServletHolder(new HttpServlet() {

            protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                try {
                    System.out.println("Shutdown request received: terminating.");
                    System.exit(0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }), "/");
        System.out.println("Deploying " + SERVLET_CLASS.getName() + " on http://localhost:" + PORT + CONTEXT_PATH + PATH_SPEC);
        server.start();
        server.join();
    }
