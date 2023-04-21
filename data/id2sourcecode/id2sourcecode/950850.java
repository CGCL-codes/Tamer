    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.format = FORMAT_XHTML;
        ServletOutputStream _xhtml_out = response.getOutputStream();
        try {
            if (!this.securityManager.isLogged()) {
                response.sendRedirect("/admin/Login");
            }
            List<com.whitebearsolutions.directory.Entry> results = null;
            SystemConfiguration _sc = new SystemConfiguration(this.sessionManager.getConfiguration());
            EntryManager _em = this.sessionManager.getEntryManager();
            BranchManager _bm = this.sessionManager.getBranchManager();
            String[][] _attributes = new String[4][2];
            _attributes[0][0] = getLanguageMessage("directory.user.mail");
            _attributes[0][1] = "mail";
            _attributes[1][0] = getLanguageMessage("common.login.user");
            _attributes[1][1] = "uid";
            _attributes[2][0] = getLanguageMessage("directory.user.sn");
            _attributes[2][1] = "sn";
            _attributes[3][0] = getLanguageMessage("directory.common.name");
            _attributes[3][1] = "givenName";
            int object = USER;
            if (request.getParameter("objecttype") != null && request.getParameter("objecttype").equals("machine")) {
                object = MACHINE;
            } else if (request.getParameter("objecttype") != null && request.getParameter("objecttype").equals("group")) {
                object = GROUP;
            } else if (request.getParameter("objecttype") != null && request.getParameter("objecttype").equals("dns")) {
                object = DOMAIN;
            } else if (request.getParameter("objecttype") != null && request.getParameter("objecttype").equals("branch")) {
                object = BRANCH;
            } else if (request.getParameter("objecttype") != null && request.getParameter("objecttype").equals("context")) {
                object = CONTEXT;
            }
            String content = null;
            if (request.getParameter("content") != null) {
                content = request.getParameter("content");
            }
            int MAX_ENTRIES = 10;
            if (this.sessionManager.getConfiguration().getProperty("directory.maxentries") != null) {
                MAX_ENTRIES = Integer.parseInt(this.sessionManager.getConfiguration().getProperty("directory.maxentries"));
            }
            int pag = 1;
            if (request.getParameter("pag") != null) {
                try {
                    pag = Integer.valueOf(request.getParameter("pag"));
                } catch (NumberFormatException _ex) {
                }
            }
            int offset = (pag * MAX_ENTRIES) - MAX_ENTRIES;
            try {
                switch(object) {
                    case GROUP:
                        if (content != null) {
                            results = _em.searchEntries(content, request.getParameter("selected_path"), EntryManager.GROUP);
                        } else {
                            results = new ArrayList<com.whitebearsolutions.directory.Entry>();
                        }
                        break;
                    case MACHINE:
                        if (content != null) {
                            results = _em.searchEntries(content, request.getParameter("selected_path"), EntryManager.MACHINE);
                        } else {
                            results = new ArrayList<com.whitebearsolutions.directory.Entry>();
                        }
                        break;
                    case DOMAIN:
                        if (content != null) {
                            results = _em.searchEntries(content, request.getParameter("selected_path"), EntryManager.DOMAIN);
                        } else {
                            results = new ArrayList<com.whitebearsolutions.directory.Entry>();
                        }
                        break;
                    default:
                        if (content != null) {
                            results = _em.searchEntries(content, request.getParameter("selected_path"), EntryManager.USER);
                        } else {
                            results = new ArrayList<com.whitebearsolutions.directory.Entry>();
                        }
                        break;
                }
                if ((object == USER || object == GROUP) && request.getParameter("format") != null && request.getParameter("format").toLowerCase().equals("csv")) {
                    this.format = FORMAT_CSV;
                } else {
                    response.setContentType("text/html");
                    writeDocumentHeader();
                    _xhtml_out.println("<script>");
                    _xhtml_out.println("<!--");
                    _xhtml_out.println("function searchNestedUsers() {");
                    _xhtml_out.println("  document.group.action=\"/admin/NestedGroup\";");
                    _xhtml_out.println("  document.group.type.value=\"" + NestedGroup.SHOW_NESTED_USERS + "\";");
                    _xhtml_out.println("  submitForm(document.group.submit());");
                    _xhtml_out.println("}");
                    _xhtml_out.println("function showSearchInfo() {");
                    _xhtml_out.println("  var info = document.getElementById(\"searchinfo\");");
                    _xhtml_out.println("  if(info != null) {");
                    _xhtml_out.println("    if(info.style.visibility == \"visible\") {");
                    _xhtml_out.println("      info.style.visibility = \"hidden\";");
                    _xhtml_out.println("    } else {");
                    _xhtml_out.println("      info.style.visibility = \"visible\";");
                    _xhtml_out.println("    }");
                    _xhtml_out.println("  }");
                    _xhtml_out.println("}");
                    _xhtml_out.println("//-->");
                    _xhtml_out.println("</script>");
                    _xhtml_out.println(new JavaScriptHelper(this.sessionManager.getSecurityManager()).getJavascriptBranchTree("/admin/Directory", "index", request.getParameter("index")));
                    _xhtml_out.println("<form onsubmit=\"showLoadingPage();\" action=\"/admin/Directory\" id=\"search\" name=\"search\" method=\"get\">");
                    if (request.getParameter("index") != null) {
                        _xhtml_out.print("<input type=\"hidden\" name=\"index\" value=\"");
                        _xhtml_out.print(request.getParameter("index"));
                        _xhtml_out.println("\"/>");
                    }
                    if (request.getParameter("selected_path") != null) {
                        _xhtml_out.print("<input type=\"hidden\" name=\"selected_path\" value=\"");
                        _xhtml_out.print(request.getParameter("selected_path"));
                        _xhtml_out.println("\"/>");
                    }
                    if (request.getParameter("selected_type") != null) {
                        _xhtml_out.print("<input type=\"hidden\" name=\"selected_type\" value=\"");
                        _xhtml_out.print(request.getParameter("selected_type"));
                        _xhtml_out.println("\"/>");
                    }
                    _xhtml_out.println("<div id=\"directory_content\" class=\"directory_content\">");
                    _xhtml_out.println("<div id=\"directory_menu\" class=\"directory_menu\">");
                    _xhtml_out.println("<script type=\"text/javascript\">");
                    _xhtml_out.println("<!--");
                    _xhtml_out.print("createTree(Tree,1,");
                    if (request.getParameter("index") == null) {
                        _xhtml_out.print(1);
                    } else {
                        try {
                            _xhtml_out.print(Integer.parseInt(request.getParameter("index")) + 1);
                        } catch (NumberFormatException _ex) {
                            _xhtml_out.print(1);
                        }
                    }
                    _xhtml_out.println(");");
                    _xhtml_out.println("//-->");
                    _xhtml_out.println("</script>");
                    _xhtml_out.println("</div>");
                    _xhtml_out.println("<div id=\"directory_subcontent\" class=\"directory_subcontent\">");
                    _xhtml_out.println("<div id=\"directory_search\" class=\"directory_search\">");
                    _xhtml_out.println("<div id=\"searchinfo\">");
                    _xhtml_out.println("<strong>");
                    _xhtml_out.println(getLanguageMessage("directory.common.find.info"));
                    _xhtml_out.println("</strong><br/>");
                    _xhtml_out.println("<br/><strong>-virtual</strong>: ");
                    _xhtml_out.println(getLanguageMessage("directory.common.find.info.virtual"));
                    _xhtml_out.println("<br/><strong>-member</strong>: ");
                    _xhtml_out.println(getLanguageMessage("directory.common.find.info.member"));
                    _xhtml_out.println("<br/><strong>-nomember</strong>: ");
                    _xhtml_out.println(getLanguageMessage("directory.common.find.info.nomember"));
                    _xhtml_out.println("<br/><strong>-usermember</strong>: ");
                    _xhtml_out.println(getLanguageMessage("directory.common.find.info.usermember"));
                    _xhtml_out.println("<br/><strong>-nousermember</strong>: ");
                    _xhtml_out.println(getLanguageMessage("directory.common.find.info.nousermember"));
                    _xhtml_out.println("<br/><strong>-nonestedusermember</strong>: ");
                    _xhtml_out.println(getLanguageMessage("directory.common.find.info.nonestedusermember"));
                    _xhtml_out.println("</div>");
                    _xhtml_out.println("<fieldset class=\"box\">");
                    _xhtml_out.print("<label>");
                    _xhtml_out.print(getLanguageMessage("directory.common.find"));
                    _xhtml_out.println("</label>");
                    _xhtml_out.println("<select class=\"form_select\" name=\"objecttype\">");
                    _xhtml_out.print("<option value=\"user\">");
                    _xhtml_out.print(getLanguageMessage("common.message.user"));
                    _xhtml_out.println("</option>");
                    _xhtml_out.print("<option value=\"group\"");
                    if (request.getParameter("objecttype") != null && "group".equals(request.getParameter("objecttype"))) {
                        _xhtml_out.print(" selected=\"selected\"");
                    }
                    _xhtml_out.print(">");
                    _xhtml_out.print(getLanguageMessage("common.message.group"));
                    _xhtml_out.println("</option>");
                    if (this.securityManager.isRoot() && ((request.getParameter("selected_type") == null && request.getParameter("selected_path") == null) || !"branch".equals(request.getParameter("selected_type")))) {
                        _xhtml_out.print("<option value=\"machine\"");
                        if (request.getParameter("objecttype") != null && "machine".equals(request.getParameter("objecttype"))) {
                            _xhtml_out.print(" selected=\"selected\"");
                        }
                        _xhtml_out.print(">");
                        _xhtml_out.print(getLanguageMessage("common.message.machine"));
                        _xhtml_out.println("</option>");
                        _xhtml_out.print("<option value=\"dns\"");
                        if (request.getParameter("objecttype") != null && "dns".equals(request.getParameter("objecttype"))) {
                            _xhtml_out.print(" selected=\"selected\"");
                        }
                        _xhtml_out.print(">");
                        _xhtml_out.print(getLanguageMessage("common.message.dns_entry"));
                        _xhtml_out.println("</option>");
                    }
                    _xhtml_out.println("</select>");
                    if (request.getParameter("selected_type") != null && "branch".equals(request.getParameter("selected_type"))) {
                        _xhtml_out.print("<label>");
                        _xhtml_out.print(getLanguageMessage("directory.common.in"));
                        _xhtml_out.println("</label>");
                        _xhtml_out.println("<img src=\"/images/arrow_branch_16.png\"/>");
                        _xhtml_out.print("<label class=\"bold\">");
                        _xhtml_out.print(_bm.getBranchName(request.getParameter("selected_path")));
                        _xhtml_out.println("</label>");
                    } else if (request.getParameter("selected_type") != null && "context".equals(request.getParameter("selected_type"))) {
                        _xhtml_out.print("<label>");
                        _xhtml_out.print(getLanguageMessage("directory.common.in"));
                        _xhtml_out.println("</label>");
                        _xhtml_out.println("<img src=\"/images/chart_organisation_16.png\"/>");
                        _xhtml_out.print("<label class=\"bold\">");
                        _xhtml_out.print(_bm.getBranchName(request.getParameter("selected_path")));
                        _xhtml_out.println("</label>");
                    }
                    _xhtml_out.print("<label>");
                    _xhtml_out.print(getLanguageMessage("directory.common.contains"));
                    _xhtml_out.println("</label>");
                    _xhtml_out.println("<input class=\"form_text\" type=\"text\" name=\"content\"/>");
                    _xhtml_out.print("<a href=\"javascript:submitForm(document.search.submit());\"><img src=\"/images/find_16.png\" title=\"");
                    _xhtml_out.print(getLanguageMessage("common.message.search"));
                    _xhtml_out.print("\" alt=\"");
                    _xhtml_out.print(getLanguageMessage("common.message.search"));
                    _xhtml_out.println("\"/></a>");
                    _xhtml_out.print("<a href=\"javascript:showSearchInfo();\"><img src=\"/images/eye_16.png\" title=\"");
                    _xhtml_out.print(getLanguageMessage("directory.common.find.info"));
                    _xhtml_out.print("\" alt=\"");
                    _xhtml_out.print(getLanguageMessage("directory.common.find.info"));
                    _xhtml_out.println("\"/></a>");
                    if (results != null && !results.isEmpty()) {
                        StringBuilder _back = new StringBuilder();
                        _back.append("/admin/Directory?content=");
                        if (request.getParameter("content") != null) {
                            _back.append(request.getParameter("content"));
                        }
                        if (request.getParameter("objecttype") != null) {
                            _back.append("&objecttype=");
                            _back.append(request.getParameter("objecttype"));
                        }
                        if (request.getParameter("index") != null) {
                            _back.append("&index=");
                            _back.append(request.getParameter("index"));
                        }
                        if (request.getParameter("selected_type") != null) {
                            _back.append("&selected_type=");
                            _back.append(request.getParameter("selected_type"));
                        }
                        if (request.getParameter("selected_path") != null) {
                            _back.append("&selected_path=");
                            _back.append(request.getParameter("selected_path"));
                        }
                        _back.append("&pag=");
                        _back.append(pag);
                        clearDocumentBack(0);
                        pushDocumentBack(_back.toString());
                        _xhtml_out.print("<a class=\"directory_csv\" href=\"/admin/Directory?content=");
                        _xhtml_out.print(content);
                        _xhtml_out.print("&objecttype=");
                        if (object == GROUP) {
                            _xhtml_out.print("group");
                        } else {
                            _xhtml_out.print("user");
                        }
                        _xhtml_out.print("&format=csv\"><img src=\"/images/text-csv_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.csv"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.csv"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.print("<label class=\"directory_pages\">");
                        _xhtml_out.print(getLanguageMessage("directory.common.pages"));
                        _xhtml_out.println(": </label>");
                        int j = pag - 1, max_pag;
                        if (j > 3) {
                            j -= 3;
                            max_pag = 2;
                        } else {
                            j = 0;
                            max_pag = (6 - pag);
                        }
                        for (; j <= (results.size() / MAX_ENTRIES) && j < (pag + max_pag); j++) {
                            if (j == (pag - 1)) {
                                _xhtml_out.println("<label class=\"directory_page\">" + (j + 1) + "</label>");
                            } else {
                                _xhtml_out.print("<a class=\"directory_page\" href=\"/admin/Directory?content=");
                                if (request.getParameter("content") != null) {
                                    _xhtml_out.print(request.getParameter("content"));
                                }
                                if (request.getParameter("objecttype") != null) {
                                    _xhtml_out.print("&objecttype=");
                                    _xhtml_out.print(request.getParameter("objecttype"));
                                }
                                if (request.getParameter("index") != null) {
                                    _xhtml_out.print("&index=");
                                    _xhtml_out.print(request.getParameter("index"));
                                }
                                if (request.getParameter("selected_type") != null) {
                                    _xhtml_out.print("&selected_type=");
                                    _xhtml_out.print(request.getParameter("selected_type"));
                                }
                                if (request.getParameter("selected_path") != null) {
                                    _xhtml_out.print("&selected_path=");
                                    _xhtml_out.print(request.getParameter("selected_path"));
                                }
                                _xhtml_out.print("&pag=");
                                _xhtml_out.print(j + 1);
                                _xhtml_out.print("\">");
                                _xhtml_out.print(j + 1);
                                _xhtml_out.println("</a>");
                            }
                        }
                    }
                    _xhtml_out.println("</fieldset>");
                    _xhtml_out.println("</div>");
                    if (content == null) {
                        _xhtml_out.println("<div class=\"window_float\">");
                        _xhtml_out.print("<h2>&nbsp;</h2>");
                        _xhtml_out.print("<div class=\"subwarn\">");
                        _xhtml_out.println(getLanguageMessage("directory.common.warning"));
                        _xhtml_out.print("</div>");
                        _xhtml_out.println("</div>");
                    }
                }
                switch(object) {
                    default:
                        String image_path = this.sessionManager.getConfiguration().getProperty("directory.images.path");
                        String image_url = this.sessionManager.getConfiguration().getProperty("directory.images.url");
                        if (this.format == FORMAT_CSV) {
                            response.setContentType("text/csv; name=\"users-result.csv\"");
                            response.setHeader("Content-Description", "Result users");
                            response.setHeader("Content-Disposition", "attachment; filename=\"users-result.csv\"");
                            for (com.whitebearsolutions.directory.Entry e : results) {
                                _xhtml_out.print(String.valueOf(e.getAttribute("cn")[0]));
                                _xhtml_out.print(",");
                                _xhtml_out.print(String.valueOf(e.getAttribute("uid")[0]));
                                _xhtml_out.print(",");
                                _xhtml_out.print(String.valueOf(e.getAttribute("uidNumber")[0]));
                                _xhtml_out.print(",");
                                if (e.hasAttribute("mail")) {
                                    _xhtml_out.print(String.valueOf(e.getAttribute("mail")[0]));
                                } else {
                                    _xhtml_out.print("no mail");
                                }
                                _xhtml_out.print("\r\n");
                            }
                        } else {
                            for (; offset < results.size() && offset < (pag * MAX_ENTRIES); offset++) {
                                com.whitebearsolutions.directory.Entry _e = results.get(offset);
                                if (_e.hasAttribute("accountEnableStatus") && "disabled".equals(String.valueOf(_e.getAttribute("accountEnableStatus")[0]))) {
                                    _xhtml_out.println("<div class=\"window_entry_disabled\">");
                                } else {
                                    _xhtml_out.println("<div class=\"window_entry\">");
                                }
                                _xhtml_out.print("<h2>");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("cn")[0]));
                                String baseDN = EntryManager.getBaseDN(_e.getID());
                                if (baseDN.startsWith("ou") || baseDN.equals(this.sessionManager.getConfiguration().getProperty("ldap.basedn"))) {
                                    if (!_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0])) {
                                        _xhtml_out.print("<a href=\"/admin/Entry?uid=");
                                        _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                                        _xhtml_out.print("&type=");
                                        _xhtml_out.print(Entry.DELETE);
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                        _xhtml_out.println("\" src=\"/images/user_delete_16.png\"></a>");
                                    }
                                    _xhtml_out.print("<a href=\"/admin/Entry?uid=");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                                    _xhtml_out.print("&type=");
                                    _xhtml_out.print(Entry.UPDATE);
                                    _xhtml_out.print("\"><img alt=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.edit"));
                                    _xhtml_out.print("\" title=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.edit"));
                                    _xhtml_out.println("\" src=\"/images/user_edit_16.png\"></a>");
                                    if (!_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0])) {
                                        _xhtml_out.print("<a href=\"/admin/Entry?copyUid=");
                                        _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                                        _xhtml_out.print("&type=");
                                        _xhtml_out.print(Entry.NEW);
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.copy"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.copy"));
                                        _xhtml_out.println("\" src=\"/images/user_attach_16.png\"></a>");
                                    }
                                    _xhtml_out.print("<a href=\"/admin/EntryLog?uid=");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                                    _xhtml_out.print("&type=");
                                    _xhtml_out.print(EntryLog.SHOW);
                                    _xhtml_out.print("\"><img alt=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.audit"));
                                    _xhtml_out.print("\" title=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.audit"));
                                    _xhtml_out.println("\" src=\"/images/report_16.png\"></a>");
                                }
                                _xhtml_out.print("</h2>");
                                if (_e.hasAttribute("jpegPhoto") && image_path != null) {
                                    File _f = new File(image_path + File.separator + _e.getAttribute("uid")[0] + ".jpg");
                                    if (!_f.exists()) {
                                        ByteArrayInputStream _bais = new ByteArrayInputStream((byte[]) ((Object[]) _e.getAttribute("jpegPhoto"))[0]);
                                        FileOutputStream _fos = new FileOutputStream(_f);
                                        while (_bais.available() > 0) {
                                            _fos.write(_bais.read());
                                        }
                                        _fos.close();
                                    }
                                    _xhtml_out.print("<img class=\"directory_entry\" src=\"" + image_url + "/" + _e.getAttribute("uid")[0] + ".jpg\" />");
                                } else {
                                    _xhtml_out.print("<img class=\"directory_entry\" src=\"" + image_url + "/nophoto.png\" />");
                                }
                                _xhtml_out.print("<div>");
                                if (!_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0])) {
                                    for (int j = (_attributes.length - 1); j >= 0; --j) {
                                        if (_e.hasAttribute((String) _attributes[j][1])) {
                                            _xhtml_out.print("<label>");
                                            _xhtml_out.print(_attributes[j][0]);
                                            _xhtml_out.print("</label>: ");
                                            _xhtml_out.print(String.valueOf(_e.getAttribute(_attributes[j][1])[0]));
                                            _xhtml_out.print("<br/>");
                                        }
                                    }
                                } else {
                                    _xhtml_out.print("<label>");
                                    _xhtml_out.print(getLanguageMessage("directory.common.name"));
                                    _xhtml_out.print("</label>: " + _sc.getAdministrativeUser() + "<br/>");
                                    _xhtml_out.print("<label>");
                                    _xhtml_out.print(getLanguageMessage("directory.common.description"));
                                    _xhtml_out.print("</label>: ");
                                    _xhtml_out.print(getLanguageMessage("directory.common.dir_adm"));
                                }
                                _xhtml_out.print("</div>");
                                _xhtml_out.println("</div>");
                            }
                        }
                        break;
                    case GROUP:
                        if (this.format == FORMAT_CSV) {
                            response.setContentType("text/csv; name=\"groups-result.csv\"");
                            response.setHeader("Content-Description", "Result groups");
                            response.setHeader("Content-Disposition", "attachment; filename=\"groups-result.csv\"");
                            for (com.whitebearsolutions.directory.Entry e : results) {
                                _xhtml_out.print(String.valueOf(e.getAttribute("cn")[0]));
                                _xhtml_out.print(",");
                                _xhtml_out.print(String.valueOf(e.getAttribute("gidNumber")[0]));
                                _xhtml_out.print(",");
                                if (e.hasAttribute("description")) {
                                    _xhtml_out.print(String.valueOf(e.getAttribute("description")[0]));
                                } else {
                                    _xhtml_out.print(getLanguageMessage("directory.common.no_description"));
                                }
                                _xhtml_out.print("\r\n");
                            }
                        } else {
                            for (; offset < results.size() && offset < (pag * MAX_ENTRIES); offset++) {
                                com.whitebearsolutions.directory.Entry _e = results.get(offset);
                                _xhtml_out.println("<div class=\"window_entry\">");
                                _xhtml_out.print("<h2>");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("cn")[0]));
                                String baseDN = EntryManager.getBaseDN(_e.getID());
                                if (baseDN.startsWith("ou") || baseDN.equals(this.sessionManager.getConfiguration().getProperty("ldap.basedn"))) {
                                    if (!_e.getAttribute("cn")[0].equals(_sc.getAdministrativeGroup(1001)) && !_e.getAttribute("cn")[0].equals(_sc.getAdministrativeGroup(512)) && !_e.getAttribute("cn")[0].equals(_sc.getAdministrativeGroup(513)) && !_e.getAttribute("cn")[0].equals(_sc.getAdministrativeGroup(514)) && !_e.getAttribute("cn")[0].equals(_sc.getAdministrativeGroup(515))) {
                                        _xhtml_out.print("<a href=\"/admin/Group?cn=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("&type=");
                                        _xhtml_out.print(Group.DELETE);
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                        _xhtml_out.println("\" src=\"/images/group_delete_16.png\"></a>");
                                        _xhtml_out.print("<a href=\"/admin/Group?cn=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("&type=");
                                        _xhtml_out.print(Group.UPDATE);
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.edit"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.edit"));
                                        _xhtml_out.println("\" src=\"/images/group_edit_16.png\"></a>");
                                    } else if (!GroupManager.isVirtualGroup(_e)) {
                                        _xhtml_out.print("<a href=\"/admin/Group?type=");
                                        _xhtml_out.print(Group.CLEAN);
                                        _xhtml_out.print("&cn=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.integrity"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.integrity"));
                                        _xhtml_out.println("\" src=\"/images/group_cut_16.png\"/></a>");
                                        _xhtml_out.print("<a href=\"/admin/Group?type=");
                                        _xhtml_out.print(Group.SET_VIRTUAL);
                                        _xhtml_out.print("&cn=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.virtualize"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.virtualize"));
                                        _xhtml_out.println("\" src=\"/images/group_go_16.png\"/></a>");
                                    } else {
                                        _xhtml_out.print("<a href=\"/admin/Group?type=");
                                        _xhtml_out.print(Group.SET_STANDARD);
                                        _xhtml_out.print("&cn=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.normalize"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.normalize"));
                                        _xhtml_out.println("\" src=\"/images/group_undo_16.png\"/></a>");
                                    }
                                    if (!GroupManager.isVirtualGroup(_e)) {
                                        _xhtml_out.print("<a href=\"/admin/Group?type=");
                                        _xhtml_out.print(Group.MANAGE);
                                        _xhtml_out.print("&cn=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.group_members"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.group_members"));
                                        _xhtml_out.println("\" src=\"/images/group_gear_16.png\"/></a>");
                                        _xhtml_out.print("<a href=\"/admin/Group?type=");
                                        _xhtml_out.print(Group.MANAGE_USERS);
                                        _xhtml_out.print("&cn=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.user_members"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("directory.group.user_members"));
                                        _xhtml_out.println("\" src=\"/images/group_16.png\"/></a>");
                                        _xhtml_out.print("<a href=\"/admin/EntryLog?group=");
                                        _xhtml_out.print(HTTPURL.encode(String.valueOf(_e.getAttribute("cn")[0])));
                                        _xhtml_out.print("&type=");
                                        _xhtml_out.print(EntryLog.SHOW);
                                        _xhtml_out.print("\"><img alt=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.audit"));
                                        _xhtml_out.print("\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.audit"));
                                        _xhtml_out.println("\" src=\"/images/report_16.png\"></a>");
                                    }
                                }
                                _xhtml_out.println("</h2>");
                                _xhtml_out.print("<div>");
                                _xhtml_out.print("<label>");
                                _xhtml_out.print(getLanguageMessage("directory.group.id"));
                                _xhtml_out.print("</label>: ");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("gidNumber")[0]));
                                _xhtml_out.println("<br/>");
                                _xhtml_out.print("<label>");
                                _xhtml_out.print(getLanguageMessage("directory.group.status"));
                                _xhtml_out.print("</label>: ");
                                if (GroupManager.isVirtualGroup(_e)) {
                                    _xhtml_out.print("Virtual&nbsp;");
                                } else {
                                    _xhtml_out.print("Normal");
                                }
                                _xhtml_out.println("<br/>");
                                _xhtml_out.print("<label>");
                                _xhtml_out.print(getLanguageMessage("directory.common.description"));
                                _xhtml_out.print("</label>: ");
                                if (_e.hasAttribute("description")) {
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("description")[0]));
                                }
                                _xhtml_out.print("</div>");
                                _xhtml_out.println("</div>");
                            }
                        }
                        break;
                    case MACHINE:
                        for (; offset < results.size() && offset < (pag * MAX_ENTRIES); offset++) {
                            com.whitebearsolutions.directory.Entry _e = results.get(offset);
                            _xhtml_out.println("<div class=\"window_entry\">");
                            _xhtml_out.print("<h2>");
                            _xhtml_out.print(String.valueOf(((Object[]) _e.getAttribute("cn"))[0]));
                            _xhtml_out.print("<a href=\"/admin/Machine?workstation=");
                            _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                            _xhtml_out.print("&type=");
                            _xhtml_out.print(Machine.DELETE);
                            _xhtml_out.print("\"><img alt=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.delete"));
                            _xhtml_out.print("\" title=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.delete"));
                            _xhtml_out.println("\" src=\"/images/computer_delete_16.png\"></a>");
                            _xhtml_out.println("</h2>");
                            _xhtml_out.print("<div>");
                            _xhtml_out.print("<label>");
                            _xhtml_out.print(getLanguageMessage("directory.common.name"));
                            _xhtml_out.print("</label>: ");
                            _xhtml_out.print(String.valueOf(((Object[]) _e.getAttribute("cn"))[0]));
                            _xhtml_out.println("<br/>");
                            _xhtml_out.print("<label>");
                            _xhtml_out.print(getLanguageMessage("directory.common.login"));
                            _xhtml_out.print("</label>: ");
                            _xhtml_out.print(String.valueOf(((Object[]) _e.getAttribute("uid"))[0]));
                            _xhtml_out.println("<br/>");
                            _xhtml_out.println("</div>");
                            _xhtml_out.println("</div>");
                        }
                        break;
                    case DOMAIN:
                        DomainNameManager _dm = this.sessionManager.getDomainNameManager();
                        for (; offset < results.size() && offset < (pag * MAX_ENTRIES); offset++) {
                            com.whitebearsolutions.directory.Entry _e = results.get(offset);
                            _xhtml_out.println("<div class=\"window_entry\">");
                            _xhtml_out.print("<h2>");
                            if ("@".equals(_e.getAttribute("relativeDomainName")[0])) {
                                _xhtml_out.print(String.valueOf(_e.getAttribute("zoneName")[0]));
                            } else {
                                _xhtml_out.print(String.valueOf(_e.getAttribute("relativeDomainName")[0]));
                                _xhtml_out.print(".");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("zoneName")[0]));
                            }
                            if (_dm.isRemovable(_e)) {
                                _xhtml_out.print("<a href=\"/admin/DNS?relativeDomainName=");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("relativeDomainName")[0]));
                                _xhtml_out.print("&zoneName=");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("zoneName")[0]));
                                _xhtml_out.print("&type=");
                                _xhtml_out.print(DNS.DELETE);
                                _xhtml_out.print("\"><img alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                _xhtml_out.print("\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                _xhtml_out.println("\" src=\"/images/internet_delete_16.png\"></a>");
                            }
                            if (_dm.isEditable(_e)) {
                                _xhtml_out.print("<a href=\"/admin/DNS?relativeDomainName=");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("relativeDomainName")[0]));
                                _xhtml_out.print("&zoneName=");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("zoneName")[0]));
                                _xhtml_out.print("&type=");
                                _xhtml_out.print(DNS.UPDATE);
                                _xhtml_out.print("\"><img alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.edit"));
                                _xhtml_out.print("\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.edit"));
                                _xhtml_out.println("\" src=\"/images/internet_edit_16.png\"></a>");
                            }
                            _xhtml_out.println("</h2>");
                            _xhtml_out.print("<div>");
                            _xhtml_out.print("<label>");
                            _xhtml_out.print(getLanguageMessage("directory.common.type"));
                            _xhtml_out.print("</label>: ");
                            if (_e.hasAttribute("aRecord") || _e.hasAttribute("aAAARecord")) {
                                _xhtml_out.print("A");
                            } else if (_e.hasAttribute("cNAMERecord")) {
                                _xhtml_out.print("CNAME");
                            } else if (_e.hasAttribute("pTRRecord")) {
                                _xhtml_out.print("PTR");
                            } else if (_e.hasAttribute("SOARecord")) {
                                _xhtml_out.print("SOA");
                            } else if (_e.hasAttribute("tXTRecord")) {
                                _xhtml_out.print("TXT");
                            } else {
                                _xhtml_out.print(getLanguageMessage("common.message.unknown"));
                            }
                            _xhtml_out.print("<br/>");
                            _xhtml_out.print("<label>");
                            _xhtml_out.print(getLanguageMessage("directory.dns.zone"));
                            _xhtml_out.print("</label>: ");
                            _xhtml_out.print(String.valueOf(_e.getAttribute("zoneName")[0]));
                            _xhtml_out.print("<br/>");
                            _xhtml_out.print("<label>");
                            _xhtml_out.print(getLanguageMessage("directory.dns.class"));
                            _xhtml_out.print("</label>: ");
                            _xhtml_out.print(String.valueOf(_e.getAttribute("dNSClass")[0]));
                            _xhtml_out.print("<br/>");
                            if (_e.hasAttribute("aRecord") || _e.hasAttribute("aAAARecord")) {
                                _xhtml_out.print("<label>A</label>: ");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("aRecord")[0]));
                                _xhtml_out.print("<br/>");
                            } else if (_e.hasAttribute("cNAMERecord")) {
                                _xhtml_out.print("<label>CNAME</label>: ");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("cNAMERecord")[0]));
                                _xhtml_out.print("<br/>");
                            } else if (_e.hasAttribute("pTRRecord")) {
                                _xhtml_out.print("<label>PTR</label>: ");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("pTRRecord")[0]));
                                _xhtml_out.print("<br/>");
                            } else if (_e.hasAttribute("tXTRecord")) {
                                _xhtml_out.print("<label>TXT</label>: ");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("tXTRecord")[0]));
                                _xhtml_out.print("<br/>");
                            }
                            _xhtml_out.println("</div>");
                            _xhtml_out.println("</div>");
                        }
                        break;
                }
                if (this.format != FORMAT_CSV) {
                    _xhtml_out.println("</div>");
                    _xhtml_out.println("</div>");
                    _xhtml_out.println("<div class=\"clear\"></div>");
                    _xhtml_out.println("</div>");
                }
            } catch (Exception _ex) {
                if (this.format == FORMAT_CSV) {
                    this.format = FORMAT_XHTML;
                    writeDocumentHeader();
                }
                writeDocumentError(_ex.getMessage());
            }
        } catch (Exception _ex) {
            if (this.format == FORMAT_CSV) {
                this.format = FORMAT_XHTML;
                try {
                    writeDocumentHeader();
                } catch (Exception _ex2) {
                }
            }
            writeDocumentError(_ex.getMessage());
        } finally {
            if (this.format != FORMAT_CSV) {
                writeDocumentFooter();
            }
        }
    }
