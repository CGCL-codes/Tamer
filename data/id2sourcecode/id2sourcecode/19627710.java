    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String basePath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String requestedURL = requestURI.replace(basePath + "/", "");
        Map<String, Statistique> stats = MirrorManager.getInstance().getURLStatistics();
        Set<String> presentBaseDir = null;
        presentBaseDir = MirrorManager.getInstance().getPresentBaseDir();
        if (requestedURL.length() <= 1) {
            buildHome(request, response);
        } else {
            if (presentBaseDir.contains(requestedURL)) {
                buildLocallyStockedFiles(request, response);
            } else {
                if (stats.containsKey(requestedURL)) {
                    PrintWriter out = response.getWriter();
                    response.setContentType("text/html;charset=UTF-8");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Detail on " + requestedURL + "</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("RequestedURL = " + requestedURL + "<br/>");
                    stats.get(requestedURL).buildTree(out);
                    buildFoot(out);
                    out.println("</body>");
                    out.println("</html>");
                    out.close();
                } else {
                    boolean alreadyTold = false;
                    DownloadTask downloadTask = MirrorManager.getInstance().downloadFromThis(requestedURL);
                    InputStream ins = downloadTask.getInputStream();
                    Thread task = new Thread(downloadTask);
                    task.setName("download : " + requestedURL);
                    task.start();
                    log(MessageFormat.format("Started to write to output at {0,date, long}", new Date()));
                    OutputStream outputFile = response.getOutputStream();
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read = ins.read(buffer);
                    log(MessageFormat.format("{0,number,integer} bytes was read", read));
                    while (read >= 0) {
                        if (read > 0 && !alreadyTold) {
                            if (downloadTask.getContentLength() > 0) {
                                System.out.println(requestedURL + " is " + downloadTask.getContentLength() + " long");
                                response.setContentLength(downloadTask.getContentLength());
                            }
                            if (downloadTask.getContentType() != null) {
                                response.setContentType(downloadTask.getContentType());
                            }
                            alreadyTold = true;
                        }
                        outputFile.write(buffer, 0, read);
                        read = ins.read(buffer);
                    }
                    log(MessageFormat.format("finished to write to output at {0,date,long}", new Date()));
                    ins.close();
                    outputFile.close();
                }
            }
        }
    }
