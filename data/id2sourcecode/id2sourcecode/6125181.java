    private void processResult(Result result, HttpServletRequest request, HttpServletResponse response, Controller controller) throws IOException, ServletException {
        if (result.isStop()) {
            return;
        } else if (result.isRedirect()) {
            log.debug("redirecting to : " + result.getValue());
            if (result.getValue().startsWith("/")) {
                response.sendRedirect(request.getContextPath() + result.getValue());
            } else {
                response.sendRedirect(result.getValue());
            }
        } else {
            if (result.getType() == Result.Type.JSP) {
                if (controller.getRequestParameters() != null) {
                    for (String key : controller.getRequestParameters().keySet()) {
                        request.setAttribute(key, controller.getRequestParameters().get(key));
                    }
                }
                if (app.getHeader() != null && controller.getIncludeGlobalHeaderFooter() && result.isDecorate()) {
                    jspRequestDispatch(app.getHeader(), request, response, false);
                }
                if (controller.getHeader() != null && result.isDecorate() && !result.isError()) {
                    jspRequestDispatch(controller.getHeader(), request, response, false);
                }
                jspRequestDispatch(result.getValue(), request, response, false);
                if (controller.getFooter() != null && result.isDecorate() && !result.isError()) {
                    jspRequestDispatch(controller.getFooter(), request, response, false);
                }
                if (app.getFooter() != null && controller.getIncludeGlobalHeaderFooter() && result.isDecorate()) {
                    jspRequestDispatch(app.getFooter(), request, response, false);
                }
            } else {
                if (result.getType() == Result.Type.HTML) {
                    response.setContentType("text/html");
                    response.getWriter().write(result.getValue());
                    response.getWriter().close();
                } else if (result.getType() == Result.Type.XML) {
                    response.setContentType("text/xml");
                    response.setCharacterEncoding(request.getCharacterEncoding());
                    if (result.getValue().indexOf("<?xml") == -1) {
                        response.getWriter().write("<?xml version=\"1.0\" encoding=\"" + request.getCharacterEncoding() + "\"?>");
                    }
                    response.getWriter().write(result.getValue());
                    response.getWriter().close();
                } else if (result.getType() == Result.Type.RESOURCE) {
                    try {
                        InputStream in = new BufferedInputStream(request.getSession().getServletContext().getResourceAsStream(result.getValue()));
                        OutputStream out = new BufferedOutputStream(response.getOutputStream());
                        if (in != null && out != null) {
                            int read = 0;
                            byte[] buffer = new byte[4096];
                            while (read > -1) {
                                read = in.read(buffer);
                                if (read > -1) {
                                    out.write(buffer, 0, read);
                                }
                            }
                            in.close();
                            out.close();
                        }
                    } catch (FileNotFoundException e) {
                        log.warn(e);
                    } catch (IOException e) {
                        log.error(e);
                    }
                } else {
                    response.setContentType("text/plain");
                    response.getWriter().write(result.getValue());
                    response.getWriter().close();
                }
            }
        }
    }
