    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream _xhtml_out = response.getOutputStream();
        try {
            if (!this.securityManager.isLogged()) {
                response.sendRedirect("/admin/Login");
            }
            this.type = -1;
            if (request.getParameter("type") != null && request.getParameter("type").length() > 0) {
                try {
                    this.type = Integer.parseInt(request.getParameter("type"));
                } catch (NumberFormatException _ex) {
                }
            }
            if (this.type == OPERATION_USER_IMPORT) {
                if (request.getParameter("repository") == null || request.getParameter("repository").isEmpty()) {
                    response.setContentType("text/html");
                    writeDocumentHeader();
                    throw new Exception(getLanguageMessage("synchronization.exception.repository"));
                }
                if (request.getParameter("user") == null || request.getParameter("user").isEmpty()) {
                    response.setContentType("text/html");
                    writeDocumentHeader();
                    throw new Exception(getLanguageMessage("synchronization.exception.user_id"));
                }
                ExternalRepository _ex = ExternalRepository.getInstance(request.getParameter("repository"));
                AttributeSet _user = _ex.getUserEntry(request.getParameter("user"));
                this.sessionManager.loadObjectSession("entry", _user);
                this.sessionManager.loadObjectSession("entryGroups", _ex.getUserGroupNames(request.getParameter("user")));
                response.sendRedirect("/admin/Entry");
            }
            response.setContentType("text/html");
            writeDocumentHeader();
            switch(this.type) {
                default:
                    {
                        RepositoryManager _repositorym = new RepositoryManager();
                        BranchManager _branchm = this.sessionManager.getBranchManager();
                        List<Map<String, String>> _repositories = _repositorym.getRepositories();
                        List<String> _branches = _branchm.getAllBranchPaths();
                        _xhtml_out.println("<script>");
                        _xhtml_out.println("<!--");
                        _xhtml_out.println("  function SearchUser(server) {");
                        _xhtml_out.println("    document.replica.type.value = " + OPERATION_USER_SEARCH + ";");
                        _xhtml_out.println("    submitForm(document.replica.submit());");
                        _xhtml_out.println("  }");
                        _xhtml_out.println("  function Replica() {");
                        _xhtml_out.println("    document.replica.type.value = " + OPERATION_IMPORT + ";");
                        _xhtml_out.println("    submitForm(document.replica.submit());");
                        _xhtml_out.println("  }");
                        _xhtml_out.println("//-->");
                        _xhtml_out.println("</script>");
                        _xhtml_out.print("<form action=\"/admin/ReplicationOperation\" name=\"replica\" method=\"post\">");
                        _xhtml_out.print("<input type=\"hidden\" name=\"type\" value=\"" + OPERATION_USER_SEARCH + "\"/>");
                        _xhtml_out.println("<h1>");
                        _xhtml_out.print("<img src=\"/images/link_32.png\"/>");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation"));
                        _xhtml_out.println("</h1>");
                        _xhtml_out.println("<div class=\"info\">");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation.info"));
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"window\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation.repositories"));
                        _xhtml_out.print("<a href=\"javascript:document.location.reload();\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<fieldset>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"repository\">");
                        _xhtml_out.print(getLanguageMessage("synchronization.rule.repository"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.println("<select class=\"form_select\" name=\"repository\">");
                        for (Map<String, String> _repository : _repositories) {
                            _xhtml_out.print("<option value=\"");
                            _xhtml_out.print(_repository.get("name"));
                            _xhtml_out.print("\">");
                            _xhtml_out.print(_repository.get("name"));
                            _xhtml_out.println("</option>");
                        }
                        _xhtml_out.println("</select>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"window\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation.replica"));
                        _xhtml_out.print("<a href=\"javascript:document.location.reload();\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<div class=\"subinfo\">");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation.replica.info"));
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<fieldset>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"match\">");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation.replica.match"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"match\"/>");
                        _xhtml_out.print("<a href=\"javascript:SearchUser();\"><img src=\"/images/find_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.search"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.search"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</div>");
                        if (this.securityManager.isRoot()) {
                            _xhtml_out.println("<div class=\"clear\"></div>");
                            _xhtml_out.println("<div class=\"subinfo\">");
                            _xhtml_out.print(getLanguageMessage("synchronization.operation.replica.match.info"));
                            _xhtml_out.println("</div>");
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label for=\"branch\"></label>");
                            _xhtml_out.print("<input class=\"form_radio\" type=\"radio\" name=\"action\" value=\"import\" checked=\"checked\"/>");
                            _xhtml_out.print(getLanguageMessage("synchronization.import"));
                            _xhtml_out.println("</div>");
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label></label>");
                            _xhtml_out.print("<input class=\"form_radio\" type=\"radio\" name=\"action\" value=\"export\" checked=\"checked\"/>");
                            _xhtml_out.print(getLanguageMessage("synchronization.export"));
                            _xhtml_out.println("</div>");
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label for=\"branch\">");
                            _xhtml_out.print(getLanguageMessage("synchronization.destination_branch"));
                            _xhtml_out.println(": </label>");
                            _xhtml_out.println("<select class=\"form_select\" name=\"branch\">");
                            _xhtml_out.print("<option value=\"root\" selected>");
                            _xhtml_out.print(getLanguageMessage("directory.common.root"));
                            _xhtml_out.println("</option>");
                            for (String name : _branches) {
                                _xhtml_out.print("<option value=\"");
                                _xhtml_out.print(name);
                                _xhtml_out.print("\">");
                                _xhtml_out.print(name);
                                _xhtml_out.println("</option>");
                            }
                            _xhtml_out.println("</select>");
                            _xhtml_out.print("<a href=\"javascript:Replica();\"><img src=\"/images/wizard_16.png\" title=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.replicate"));
                            _xhtml_out.print("\" alt=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.replicate"));
                            _xhtml_out.println("\"/></a>");
                            _xhtml_out.println("</div>");
                        }
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        if (this.securityManager.isRoot()) {
                            _xhtml_out.println("<div class=\"window\">");
                            _xhtml_out.println("<h2>");
                            _xhtml_out.print(getLanguageMessage("synchronization.recent_process"));
                            _xhtml_out.print("<a href=\"javascript:document.location.reload();\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                            _xhtml_out.print("\" alt=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                            _xhtml_out.println("\"/></a>");
                            _xhtml_out.println("</h2>");
                            _xhtml_out.println("<div class=\"subinfo\">");
                            _xhtml_out.print(getLanguageMessage("synchronization.operation.replica.info_view"));
                            _xhtml_out.println("</div>");
                            _xhtml_out.println("<table>");
                            boolean replog = false;
                            for (Map<String, String> _repository : _repositories) {
                                File _f = new File("/tmp/replication-" + _repository.get("name") + ".log");
                                if (_f.exists()) {
                                    _xhtml_out.println("<tr>");
                                    _xhtml_out.print("<td>");
                                    _xhtml_out.print("<a href=\"/admin/ReplicationOperation?type=");
                                    _xhtml_out.print(OPERATION_LOG);
                                    _xhtml_out.print("&repository=");
                                    _xhtml_out.print(_repository.get("name"));
                                    _xhtml_out.print("\"><img src=\"/images/page_link_16.png\"/> " + _repository.get("name") + "</a>");
                                    _xhtml_out.println("</td>");
                                    _xhtml_out.println("</tr>");
                                    replog = true;
                                }
                            }
                            if (!replog) {
                                _xhtml_out.println("<tr>");
                                _xhtml_out.print("<td>");
                                _xhtml_out.print(getLanguageMessage("synchronization.operation.replica.no_process"));
                                _xhtml_out.println("</td>");
                                _xhtml_out.println("</tr>");
                            }
                            _xhtml_out.println("</table>");
                            _xhtml_out.println("<div class=\"clear\"></div>");
                            _xhtml_out.println("</div>");
                        }
                        _xhtml_out.println("</form>");
                    }
                    break;
                case OPERATION_USER_SEARCH:
                    {
                        if (request.getParameter("repository") == null || request.getParameter("repository").isEmpty()) {
                            throw new Exception(getLanguageMessage("synchronization.exception.repository"));
                        }
                        if (request.getParameter("match") == null || request.getParameter("match").isEmpty()) {
                            throw new Exception(getLanguageMessage("synchronization.exception.search_string"));
                        }
                        ExternalRepository _er = ExternalRepository.getInstance(request.getParameter("repository"));
                        List<AttributeSet> result = _er.searchUserEntry(request.getParameter("match"));
                        writeDocumentBack("/admin/ReplicationOperation");
                        _xhtml_out.print("<form action=\"/admin/ReplicationOperation\" name=\"replica\" method=\"post\">");
                        _xhtml_out.print("<input type=\"hidden\" name=\"type\" value=\"" + OPERATION_USER_IMPORT + "\"/>");
                        _xhtml_out.print("<input type=\"hidden\" name=\"repository\" value=\"" + request.getParameter("repository") + "\"/>");
                        _xhtml_out.println("<h1>");
                        _xhtml_out.print("<img src=\"/images/link_32.png\"/>");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation"));
                        _xhtml_out.println("</h1>");
                        _xhtml_out.println("<div class=\"info\">");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation.info"));
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"window\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(request.getParameter("repository"));
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.replica.submit());\"><img src=\"/images/user_add_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.add"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.add"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<table>");
                        if (result.isEmpty()) {
                            _xhtml_out.println("<tr>");
                            _xhtml_out.print("<td>");
                            _xhtml_out.print(getLanguageMessage("synchronization.operation.replica.no_user"));
                            _xhtml_out.println("</td>");
                            _xhtml_out.println("</tr>");
                        } else {
                            _xhtml_out.println("<tr>");
                            _xhtml_out.print("<td class=\"title\">");
                            _xhtml_out.print("&nbsp;");
                            _xhtml_out.println("</td>");
                            _xhtml_out.print("<td class=\"title\">");
                            _xhtml_out.print(getLanguageMessage("directory.common.name"));
                            _xhtml_out.println("</td>");
                            _xhtml_out.print("<td class=\"title\">");
                            _xhtml_out.print(getLanguageMessage("directory.common.login"));
                            _xhtml_out.println("</td>");
                            _xhtml_out.println("</tr>");
                            for (AttributeSet user : result) {
                                String uid = "";
                                if (user.hasAttribute("uid")) {
                                    uid = user.getAttributeFirstStringValue("uid");
                                }
                                _xhtml_out.println("<tr>");
                                _xhtml_out.print("<td>");
                                _xhtml_out.print("<input class=\"form_radio\" type=\"radio\" name=\"user\" value=\"");
                                _xhtml_out.print(uid);
                                _xhtml_out.print("\"/>");
                                _xhtml_out.println("</td><td>");
                                if (user.hasAttribute("cn")) {
                                    _xhtml_out.print(user.getAttributeFirstStringValue("cn"));
                                }
                                _xhtml_out.println("</td><td>");
                                _xhtml_out.print(uid);
                                _xhtml_out.println("</td>");
                                _xhtml_out.println("</tr>");
                            }
                        }
                        _xhtml_out.println("</table>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("</form>");
                    }
                    break;
                case OPERATION_IMPORT:
                    {
                        if (!this.securityManager.isRoot()) {
                            throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                        }
                        String _branch = null;
                        if (request.getParameter("repository") == null || request.getParameter("repository").isEmpty()) {
                            throw new Exception(getLanguageMessage("synchronization.exception.no_repository"));
                        }
                        if (request.getParameter("branch") != null || !request.getParameter("branch").isEmpty()) {
                            _branch = request.getParameter("branch");
                        }
                        ExternalRepository _er = ExternalRepository.getInstance(request.getParameter("repository"));
                        _er.importAllEntriesThread(_branch, new File("/tmp/replication-" + request.getParameter("repository") + ".log"));
                        writeDocumentResponse(getLanguageMessage("synchronization.operation.replica.info_thread_synchronization"), "/admin/ReplicationOperation");
                    }
                    break;
                case OPERATION_EXPORT:
                    {
                        if (!this.securityManager.isRoot()) {
                            throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                        }
                        if (request.getParameter("repository") == null || request.getParameter("repository").isEmpty()) {
                            throw new Exception(getLanguageMessage("synchronization.exception.no_repository"));
                        }
                        ExternalRepository _er = ExternalRepository.getInstance(request.getParameter("repository"));
                        _er.exportAllEntriesThread(new File("/tmp/replication-" + request.getParameter("repository") + ".log"));
                        writeDocumentResponse(getLanguageMessage("synchronization.operation.replica.info_thread_synchronization"), "/admin/ReplicationOperation");
                    }
                    break;
                case OPERATION_LOG:
                    {
                        if (!this.securityManager.isRoot()) {
                            throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                        }
                        if (request.getParameter("repository") == null || request.getParameter("repository").isEmpty()) {
                            throw new Exception(getLanguageMessage("synchronization.exception.no_repository"));
                        }
                        File _f = new File("/tmp/replication-" + request.getParameter("repository") + ".log");
                        writeDocumentBack("/admin/ReplicationOperation");
                        _xhtml_out.println("<h1>");
                        _xhtml_out.print("<img src=\"/images/link_32.png\"/>");
                        _xhtml_out.print(getLanguageMessage("synchronization.operation"));
                        _xhtml_out.println("</h1>");
                        _xhtml_out.println("<div class=\"window\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(request.getParameter("repository"));
                        _xhtml_out.print("<a href=\"javascript:document.location.reload();\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.print("<textarea cols=\"120\" rows=\"30\">");
                        if (_f.exists()) {
                            BufferedReader _reader = new BufferedReader(new InputStreamReader(new FileInputStream(_f)));
                            for (String line = _reader.readLine(); line != null; line = _reader.readLine()) {
                                _xhtml_out.println(line);
                            }
                            _reader.close();
                        }
                        _xhtml_out.print("</textarea>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                    }
                    break;
            }
        } catch (Exception _ex) {
            writeDocumentError(_ex.getMessage());
        } finally {
            writeDocumentFooter();
        }
    }
