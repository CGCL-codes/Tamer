    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String sql = CommonConst.EMPTYSTR;
        String dlxml = CommonConst.EMPTYSTR;
        String loadedDriver = CommonConst.EMPTYSTR;
        String loadedURI = CommonConst.EMPTYSTR;
        String connectedCatalog = CommonConst.EMPTYSTR;
        String uri = getParameterDef(req, PARAM_URI, "");
        String password = getParameterDef(req, PARAM_PASSWORD, "");
        String passwordEnc = getParameterDef(req, PARAM_PASSWORDENC, "");
        if (!StringUtils.isEmpty(passwordEnc)) password = decodePassword(passwordEnc);
        String login = getParameterDef(req, PARAM_LOGIN, "");
        String xmltext = getParameterDef(req, PARAM_XMLTEXT, "");
        String action = getParameterDef(req, PARAM_ACTION, "");
        boolean performConnect = !StringUtils.isEmpty(uri);
        Connection conn = null;
        List<String> errorStrings = new Vector<String>();
        List<String> jndiURIs = new Vector<String>();
        findJNDIDatasourceURIs(jndiURIs, extractors);
        DataSource ds = null;
        if (performConnect) {
            try {
                conn = DriverManager.getConnection(uri, login, password);
                if (conn != null) {
                    loadedDriver = conn.getMetaData().getDriverName();
                    loadedURI = conn.getMetaData().getURL();
                    Properties p = new Properties();
                    p.put("driverClassName", DriverManager.getDriver(uri));
                    p.put("username", login);
                    p.put("password", password);
                    p.put("url", uri);
                    p.put("poolPreparedStatements", "true");
                    ds = BasicDataSourceFactory.createDataSource(p);
                } else {
                    LOG.debug(MessageFormat.format("No connection (uri: \"{0}\"; login:\"{1}\")", uri, login));
                }
            } catch (Exception e) {
                errorStrings.add(Messages.getString("DDLServlet.error.metadata", e.getMessage()));
                LOG.error("error requesting meta information", e);
            }
            if (conn != null) {
                try {
                    DatabaseIO io = new DatabaseIO();
                    io.setUseInternalDtd(true);
                    io.setValidateXml(false);
                    connectedCatalog = conn.getCatalog();
                    LOG.debug(conn.getMetaData().getURL());
                    Platform platform = PlatformFactory.createNewPlatformInstance(ds);
                    StringWriter xml = new StringWriter();
                    Database exdb = platform.readModelFromDatabase(conn, connectedCatalog);
                    io.write(exdb, xml);
                    xml.close();
                    dlxml = xml.toString();
                    if (ACTION_DDLTOAREA.equals(action)) {
                        xmltext = xml.toString();
                    }
                    if (ACTION_UPGRADE.equals(action) || ACTION_PREVIEW.equals(action)) {
                        if (!StringUtils.isEmpty(xmltext)) {
                            try {
                                Database newDB = io.read(new StringReader(xmltext));
                                sql = StringEscapeUtils.escapeHtml(platform.getAlterModelSql(exdb, newDB));
                                if (ACTION_UPGRADE.equals(action)) {
                                    try {
                                        platform.alterModel(conn, exdb, newDB, false);
                                        errorStrings.add(Messages.getString("DDLServlet.message.upgradePerformed"));
                                    } catch (Exception e) {
                                        errorStrings.add(Messages.getString("DDLServlet.message.upgradeFailed", e.getMessage()));
                                    }
                                }
                            } catch (Exception e) {
                                errorStrings.add(Messages.getString("DDLServlet.message.sqlFailed", e.getMessage()));
                            }
                        } else {
                            errorStrings.add(Messages.getString("DDLServlet.error.noXml"));
                        }
                    }
                    if (ACTION_BACKUPDATA.equals(action) || ACTION_BACKUPDATADOWNLOAD.equals(action)) {
                        try {
                            DatabaseDataIO dbdataIO = new DatabaseDataIO();
                            xml = new StringWriter();
                            dbdataIO.writeDataToXML(platform, exdb, xml, "UTF-8");
                            xml.close();
                            dlxml = xml.toString();
                            xmltext = xml.toString();
                        } catch (Exception e) {
                            errorStrings.add(Messages.getString("DDLServlet.message.backupdataFailed", e.getMessage()));
                            LOG.error("Failed retrieving Data", e);
                        }
                    }
                    if (ACTION_RESTOREDATA.equals(action)) {
                        try {
                            DatabaseDataIO dbdataIO = new DatabaseDataIO();
                            platform.setIdentityOverrideOn(true);
                            StringReader strread = new StringReader(xmltext);
                            dbdataIO.writeDataToDatabase(platform, exdb, new Reader[] { strread });
                            strread.close();
                        } catch (Exception e) {
                            errorStrings.add(Messages.getString("DDLServlet.message.backupdataFailed", e.getMessage()));
                            LOG.error("Failed retrieving Data", e);
                        }
                    }
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    errorStrings.add(Messages.getString("DDLServlet.error.unknowndsname", e.getMessage()));
                    LOG.error("error performing database access", e);
                }
            }
        }
        StringWriter sw = new StringWriter();
        MessageFormatWriter out = new MessageFormatWriter(sw);
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + Messages.getString("DDLServlet.title") + "</title>");
        out.println("<style><!--");
        out.println(Messages.getString("DDLServlet.cssStyle"));
        out.println("--></style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>" + Messages.getString("DDLServlet.text.heading") + "</h1>");
        out.println("<p>" + Messages.getString("DDLServlet.text.help") + "</p>");
        if (errorStrings.size() > 0) {
            for (String errString : errorStrings) {
                out.printfmt("<p class=\"error\">{0}</p>", errString);
            }
        }
        out.println("<hr/>");
        if (StringUtils.isEmpty(action)) {
            out.printfmt("<form action=\"{0}\" method=\"POST\" name=\"frm\">", req.getRequestURI());
            out.printfmt("<input type=\"hidden\" name=\"{0}\" value=\"{1}\">", PARAM_ACTION, "connect");
            out.println("<table width=\"90%\">");
            out.println("<tr class=\"uri\">");
            out.printfmt("<td class=\"heading\">{0}</td>", Messages.getString("DDLServlet.text.datasource"));
            out.println("<td class=\"value\">");
            out.println("<div><select name=\"datasource\" size=\"1\" onchange=\"javascript:if (this.value!=\'\') { this.form.uri.value=this.value;} return false;\">");
            out.printfmt("<option value=\"{0}\">{0}</option>", "", "---");
            for (String string : jndiURIs) {
                out.printfmt("<option value=\"{0}\">{0}</option>", StringEscapeUtils.escapeHtml(string));
            }
            out.println("</select></div>");
            out.printfmt("<div class=\"hint\">{0}</div>", Messages.getString("DDLServlet.text.uri.hint"));
            out.println("</td></tr>");
            out.println("<tr class=\"uri\">");
            out.printfmt("<td class=\"heading\">{0}</td>", Messages.getString("DDLServlet.text.uri"));
            out.println("<td class=\"value\">");
            out.printfmt("<div><input type=\"text\" name=\"uri\" size=\"80\" value=\"{0}\" onchange=\"javascript: this.form.datasource.value=''''; return false;\"/></div>", resp.encodeURL(uri));
            out.printfmt("<div class=\"hint\">{0}</div>", Messages.getString("DDLServlet.text.uri.hint"));
            out.println("</td></tr>");
            out.println("<tr class=\"login\"><td class=\"heading\">");
            out.println(Messages.getString("DDLServlet.text.login"));
            out.println("</td>");
            out.println("<td class=\"value\">");
            out.println("<input type=\"text\" name=\"login\" size=\"80\" value=\"" + login + "\" />");
            out.println("<br>");
            out.println("<div class=\"hint\">" + Messages.getString("DDLServlet.text.login.hint") + "</div>");
            out.println("</td></tr>");
            out.println("<tr class=\"password\"><td class=\"heading\">");
            out.println(Messages.getString("DDLServlet.text.password"));
            out.println("</td>");
            out.println("<td class=\"value\">");
            out.printfmt("<input type=\"password\" name=\"password\" size=\"80\" value=\"{0}\" />", password);
            out.println("<br>");
            out.println("<div class=\"hint\">" + Messages.getString("DDLServlet.text.password.hint") + "</div>");
            out.println("</td></tr>");
            renderSubmitButton(out);
            out.println("</table>");
            out.println("</form>");
        } else {
            if (conn == null) {
                printNoDBConnection(out, req);
            } else {
                out.printfmt("<form action=\"{0}\" method=\"POST\">", req.getRequestURI());
                out.printfmt("<input type=\"hidden\" name=\"{0}\" value=\"{1}\">", PARAM_LOGIN, login);
                out.printfmt("<input type=\"hidden\" name=\"{0}\" value=\"{1}\">", PARAM_PASSWORDENC, encodePassword(password));
                out.printfmt("<input type=\"hidden\" name=\"{0}\" value=\"{1}\">", PARAM_URI, uri);
                out.println("<table width=\"90%\">");
                printDBConnection(loadedDriver, loadedURI, out);
                printDBLogin(login, out);
                printSchemaName(out, connectedCatalog);
                out.println("<tr class=\"action\">");
                out.printfmt("<td class=\"heading\">{0}</td>", Messages.getString("DDLServlet.text.action"));
                out.println("<td class=\"value\">");
                out.println("<select name=\"action\" size=\"1\"/>");
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", "", printSelected(action, ""), Messages.getString("DDLServlet.text.action.reload"));
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", ACTION_DOWNLOAD, printSelected(action, ACTION_DOWNLOAD), Messages.getString("DDLServlet.text.action.download"));
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", ACTION_DDLTOAREA, printSelected(action, ACTION_DDLTOAREA), Messages.getString("DDLServlet.text.action.paste"));
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", ACTION_PREVIEW, printSelected(action, ACTION_PREVIEW), Messages.getString("DDLServlet.text.action.previewsql"));
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", ACTION_UPGRADE, printSelected(action, ACTION_UPGRADE), Messages.getString("DDLServlet.text.action.upgradedb"));
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", ACTION_BACKUPDATA, printSelected(action, ACTION_BACKUPDATA), Messages.getString("DDLServlet.text.action.backupdata"));
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", ACTION_BACKUPDATADOWNLOAD, printSelected(action, ACTION_BACKUPDATADOWNLOAD), Messages.getString("DDLServlet.text.action.backupdatadownload"));
                out.printfmt("<option value=\"{0}\" {1}>{2}</option>", ACTION_RESTOREDATA, printSelected(action, ACTION_RESTOREDATA), Messages.getString("DDLServlet.text.action.restoredata"));
                out.println("</select>");
                out.println("<br>");
                out.println("<div class=\"hint\">" + Messages.getString("DDLServlet.text.action.hint") + "</div>");
                out.println("</td>");
                out.println("</tr>");
                renderSubmitButton(out);
                out.println("<tr class=\"xml\"><td class=\"heading\">");
                out.println(Messages.getString("DDLServlet.text.xml"));
                out.println("</td>");
                out.println("<td class=\"value\">");
                out.println("<textarea name=\"xmltext\" cols=\"80\" rows=\"15\">" + StringEscapeUtils.escapeHtml(xmltext) + "</textarea>");
                out.println("</td></tr>");
                out.println("</table>");
                out.println("</form>");
                out.println("<form>");
                out.println("<table width=\"90%\">");
                out.println("<tr class=\"sql\">");
                out.println("<td class=\"heading\">" + Messages.getString("DDLServlet.text.sql") + "</td>");
                out.println("<td class=\"value\">");
                if (!StringUtils.isEmpty(sql)) {
                    out.println("<textarea cols=\"80\" rows=\"15\">" + sql + "</textarea>");
                } else {
                    out.println("---");
                }
                out.println("</td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("</form>");
                out.println("</body>");
                out.flush();
                out.close();
            }
        }
        if (!ACTION_DOWNLOAD.equals(action) && !ACTION_BACKUPDATADOWNLOAD.equals(action)) {
            response.setContentType(HeaderConsts.CONTENTTYPE_TEXT_HTML);
            response.getWriter().write(sw.toString());
        } else {
            resp.setHeader(HeaderConsts.HEADER_PRAGMA, "public");
            resp.setHeader(HeaderConsts.HEADER_CACHE_CONTROL, "cache");
            resp.setHeader(HeaderConsts.HEADER_CACHE_CONTROL, "must-revalidate");
            resp.setHeader(HeaderConsts.HEADER_CONTENT_DISPOSITION, MessageFormat.format("attachment;filename=\"ddl-{0}-{1,date,yyyyMMdd}{1,time,HHmmss}.xml\"", connectedCatalog, new Date()));
            resp.setContentType(HeaderConsts.CONTENTTYPE_APPLICATION_XML);
            response.getWriter().write(dlxml);
        }
        response.getWriter().flush();
    }
