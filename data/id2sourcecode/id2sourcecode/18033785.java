    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String res = request.getParameter("resource");
        if (res != null) {
            if (res.endsWith(".js")) {
                response.setContentType("text/javascript");
            } else if (res.endsWith(".xml")) {
                response.setContentType("text/xml");
            } else {
                response.setContentType("application/octet-stream");
            }
            String _res = "/net/openkoncept/vroom/resource/" + res;
            InputStream resStream = null;
            resStream = VroomServlet.class.getResourceAsStream(_res);
            if (resStream == null) {
                _res = "/WEB-INF/" + res;
                String filename = getServletContext().getRealPath(_res);
                resStream = new FileInputStream(filename);
            }
            BufferedInputStream bis = new BufferedInputStream(resStream);
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int read = bis.read(data, 0, 1024);
            do {
                if (read != -1) {
                    bos.write(data, 0, read);
                    read = bis.read(data, 0, 1024);
                }
            } while (read != -1);
            bis.close();
            bos.flush();
            bos.close();
        } else {
            String path = request.getParameter("path");
            String call = request.getParameter("call");
            String[] callParts = call.split("[.]");
            String id = null;
            String method = null;
            if (callParts.length > 1) {
                id = callParts[0];
                method = callParts[1];
            } else {
                method = callParts[0];
            }
            VroomConfig vc = VroomProcessor.getInstance(null).getVroomConfig();
            Document doc = null;
            for (Document _doc : vc.getDocumentMap().values()) {
                if (_doc.getPath().equals(path)) {
                    doc = _doc;
                    break;
                }
            }
            String className = null;
            String var = null;
            String scope = null;
            if (doc != null) {
                className = doc.getBackingBean();
                var = doc.getVar();
                scope = doc.getScope();
                if (id != null) {
                    for (Form form : doc.getFormMap().values()) {
                        if (form.getId().equals(id)) {
                            if (form.getBackingBean() != null) {
                                className = form.getBackingBean();
                                var = form.getVar();
                                scope = form.getScope();
                                for (Event event : form.getEventMap().values()) {
                                    if (event.getMethod().equals(method)) {
                                        if (event.getBackingBean() != null) {
                                            className = event.getBackingBean();
                                            var = event.getVar();
                                            scope = event.getScope();
                                        }
                                    }
                                }
                            }
                        }
                        for (Element elem : form.getElementMap().values()) {
                            if (elem.getId().equals(id)) {
                                if (form.getBackingBean() != null) {
                                    className = form.getBackingBean();
                                    var = form.getVar();
                                    scope = form.getScope();
                                }
                                if (elem.getBackingBean() != null) {
                                    className = elem.getBackingBean();
                                    var = elem.getVar();
                                    scope = elem.getScope();
                                }
                                for (Event event : elem.getEventMap().values()) {
                                    if (event.getMethod().equals(method)) {
                                        if (event.getBackingBean() != null) {
                                            className = event.getBackingBean();
                                            var = event.getVar();
                                            scope = event.getScope();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    for (Element elem : doc.getElementMap().values()) {
                        if (elem.getId().equals(id)) {
                            if (elem.getBackingBean() != null) {
                                className = elem.getBackingBean();
                                var = elem.getVar();
                                scope = elem.getScope();
                            }
                            for (Event event : elem.getEventMap().values()) {
                                if (event.getMethod().equals(method)) {
                                    if (event.getBackingBean() != null) {
                                        className = event.getBackingBean();
                                        var = event.getVar();
                                        scope = event.getScope();
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (Event event : doc.getEventMap().values()) {
                        if (event.getMethod().equals(method)) {
                            if (event.getBackingBean() != null) {
                                className = event.getBackingBean();
                                var = event.getVar();
                                scope = event.getScope();
                            }
                        }
                    }
                }
            }
            Object obj = null;
            var = (var == null) ? "" : var;
            if (scope.equals("session")) {
                obj = request.getSession().getAttribute(var);
                if (obj == null) {
                    try {
                        obj = Class.forName(className).newInstance();
                        request.getSession().setAttribute(var, obj);
                    } catch (Exception ex) {
                        Logger.getLogger(VroomServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else if (scope.equals("request")) {
                obj = request.getAttribute(var);
                if (obj == null) {
                    try {
                        obj = Class.forName(className).newInstance();
                        request.setAttribute(var, obj);
                    } catch (Exception ex) {
                        Logger.getLogger(VroomServlet.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (obj != null) {
                try {
                    Method m = null;
                    String[] rt = null;
                    try {
                        m = obj.getClass().getMethod(method, new Class[] { javax.servlet.http.HttpServletRequest.class, javax.servlet.http.HttpServletResponse.class, java.lang.String.class });
                        rt = (String[]) m.invoke(obj, new Object[] { request, response, id });
                    } catch (NoSuchMethodException ex) {
                        m = obj.getClass().getMethod(method, new Class[] { javax.servlet.http.HttpServletRequest.class, javax.servlet.http.HttpServletResponse.class });
                        rt = (String[]) m.invoke(obj, new Object[] { request, response });
                    }
                    if (rt != null) {
                        response.setHeader("Content-Type", "application/json");
                        StringBuffer sbOutput = new StringBuffer();
                        sbOutput.append("{ \"output\": [");
                        for (int i = 0; i < rt.length; i++) {
                            String output = VroomUtilities.normalize(rt[i]);
                            sbOutput.append("\"" + output + "\"");
                            sbOutput.append(",");
                        }
                        sbOutput.append("\"\" ]}");
                        response.getWriter().print(sbOutput.toString());
                    }
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(VroomServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(VroomServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(VroomServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NoSuchMethodException ex) {
                    Logger.getLogger(VroomServlet.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(VroomServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
