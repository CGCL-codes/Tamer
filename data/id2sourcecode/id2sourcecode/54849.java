    @SuppressWarnings("unchecked")
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        ServletOutputStream _xhtml_out = response.getOutputStream();
        try {
            if (!this.securityManager.isLogged()) {
                response.sendRedirect("/admin/Login");
            }
            writeDocumentHeader();
            int type = NEW;
            try {
                type = Integer.parseInt(request.getParameter("type"));
            } catch (NumberFormatException _ex) {
            }
            AttributeSet _user = null;
            Configuration _c = this.sessionManager.getConfiguration();
            SystemConfiguration _sc = new SystemConfiguration(this.sessionManager.getConfiguration());
            UserManager _um = this.sessionManager.getUserManager();
            GroupManager _gm = this.sessionManager.getGroupManager();
            SchemaManager _sm = this.sessionManager.getSchemaManager();
            BranchManager _bm = this.sessionManager.getBranchManager();
            String[] _common_attributes = new String[12];
            _common_attributes[11] = "title";
            _common_attributes[10] = "o";
            _common_attributes[9] = "ou";
            _common_attributes[8] = "employeeType";
            _common_attributes[7] = "employeeNumber";
            _common_attributes[6] = "telephoneNumber";
            _common_attributes[5] = "mobile";
            _common_attributes[4] = "facsimileTelephoneNumber";
            _common_attributes[3] = "street";
            _common_attributes[2] = "postalCode";
            _common_attributes[1] = "l";
            _common_attributes[0] = "st";
            String[] _posix_attributes = new String[4];
            _posix_attributes[3] = "shadowMin";
            _posix_attributes[2] = "shadowMax";
            _posix_attributes[1] = "shadowWarning";
            _posix_attributes[0] = "shadowInactive";
            String[] _attributes;
            switch(type) {
                default:
                    {
                        if (this.sessionManager.hasObjectSession("entry")) {
                            _user = (AttributeSet) this.sessionManager.getObjectSession("entry");
                            this.sessionManager.removeObjectSession("entry");
                        }
                        List<String> _branches = this.securityManager.getWriteUserBranches();
                        List<com.whitebearsolutions.directory.Entry> _groups = this.securityManager.getWriteUserGroups();
                        if (!this.securityManager.isRoot() && _branches.size() <= 0) {
                            throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                        }
                        String _user_branch = null;
                        int num_ou = 1, num_telephone = 1;
                        if (request.getParameter("num_ou") != null) {
                            try {
                                num_ou = Integer.parseInt(request.getParameter("num_ou"));
                            } catch (NumberFormatException _ex) {
                            }
                        }
                        if (request.getParameter("num_telephone") != null) {
                            try {
                                num_telephone = Integer.parseInt(request.getParameter("num_telephone"));
                            } catch (NumberFormatException _ex) {
                            }
                        }
                        _xhtml_out.println("<script>");
                        _xhtml_out.println("<!--");
                        _xhtml_out.println("function addOu() {");
                        _xhtml_out.println("  var n = parseInt(document.entry.num_ou.value);");
                        _xhtml_out.println("  document.entry.type.value = " + NEW + ";");
                        _xhtml_out.println("  document.entry.num_ou.value = (n + 1);");
                        _xhtml_out.println("  submitForm(document.entry.submit());");
                        _xhtml_out.println("}");
                        _xhtml_out.println("function addTelephone() {");
                        _xhtml_out.println("  var n = parseInt(document.entry.num_telephone.value);");
                        _xhtml_out.println("  document.entry.type.value = " + NEW + ";");
                        _xhtml_out.println("  document.entry.num_telephone.value = (n + 1);");
                        _xhtml_out.println("  submitForm(document.entry.submit());");
                        _xhtml_out.println("}");
                        _xhtml_out.println("//-->");
                        _xhtml_out.println("</script>");
                        writeDocumentBack("/admin/Directory");
                        _xhtml_out.print("<form action=\"/admin/Entry\" name=\"entry\" method=\"post\">");
                        _xhtml_out.print("<input type=\"hidden\" name=\"type\" value=\"" + STORE_NEW + "\"/>");
                        _xhtml_out.print("<input type=\"hidden\" name=\"num_ou\" value=\"" + num_ou + "\"/>");
                        _xhtml_out.print("<input type=\"hidden\" name=\"num_telephone\" value=\"" + num_telephone + "\"/>");
                        if (_user != null) {
                            _xhtml_out.print("<input type=\"hidden\" name=\"userGroup\" value=\"true\"/>");
                        }
                        if (request.getParameter("copyUid") != null) {
                            _xhtml_out.print("<input type=\"hidden\" name=\"copyUid\" value=\"" + request.getParameter("copyUid") + "\"/>");
                            com.whitebearsolutions.directory.Entry _u = _um.getUserEntry(request.getParameter("copyUid"));
                            _user = new AttributeSet(_u);
                            _user.setAttribute("dn", _u.getID());
                            _user_branch = _bm.getBranchPath(_u.getID());
                        }
                        _xhtml_out.println("<h1>");
                        _xhtml_out.print("<img src=\"/images/user_32.png\"/>");
                        _xhtml_out.print(getLanguageMessage("common.message.user"));
                        _xhtml_out.println("</h1>");
                        _xhtml_out.println("<div class=\"info\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.info"));
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div id=\"entry_content\" class=\"entry_content\">");
                        _xhtml_out.println("<div class=\"window_float\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("directory.common.attributes.basic"));
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<fieldset>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"givenName\">");
                        _xhtml_out.print(getLanguageMessage("directory.common.name"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"givenName\"");
                        if (request.getParameter("givenName") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("givenName"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("givenname")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("givenname"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"sn\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.sn"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sn\"");
                        if (request.getParameter("sn") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("sn"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("sn")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("sn"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"branch\">");
                        _xhtml_out.print(getLanguageMessage("common.message.branch"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.println("<select class=\"form_select\" name=\"branch\">");
                        if (this.securityManager.isRoot()) {
                            _xhtml_out.print("<option value=\"\">");
                            _xhtml_out.print(getLanguageMessage("directory.common.root"));
                            _xhtml_out.println("</option>");
                        }
                        for (String _branch : _branches) {
                            _xhtml_out.print("<option value=\"");
                            _xhtml_out.print(_branch);
                            _xhtml_out.print("\"");
                            if (request.getParameter("branch") != null && _branch.equals(request.getParameter("branch"))) {
                                _xhtml_out.print(" selected=\"selected\"");
                            } else if (request.getParameter("path") != null && _branch.equals(request.getParameter("path"))) {
                                _xhtml_out.print(" selected=\"selected\"");
                            } else if (_user != null && _user_branch != null && _branch.equals(_user_branch)) {
                                _xhtml_out.print(" selected=\"selected\"");
                            }
                            _xhtml_out.print(">");
                            _xhtml_out.print(_branch);
                            _xhtml_out.println("</option>");
                        }
                        _xhtml_out.println("</select>");
                        _xhtml_out.println("</div>");
                        for (int j = _common_attributes.length - 1; j >= 0; j--) {
                            if ("ou".equals(_common_attributes[j])) {
                                List<String> _values = new ArrayList<String>();
                                if (request.getParameterValues("ou") != null) {
                                    _values.addAll(Arrays.asList(request.getParameterValues("ou")));
                                }
                                for (int i = 0; i < num_ou || i < _values.size(); i++) {
                                    _xhtml_out.println("<div class=\"standard_form\">");
                                    _xhtml_out.print("<label");
                                    if (i == 0) {
                                        _xhtml_out.print(" for=\"");
                                        _xhtml_out.print(_common_attributes[j]);
                                        _xhtml_out.print("\">");
                                        _xhtml_out.print(getLanguageMessage("directory.user." + _common_attributes[j]));
                                        _xhtml_out.println(": ");
                                    } else {
                                        _xhtml_out.print(">");
                                    }
                                    _xhtml_out.println("</label>");
                                    _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"");
                                    _xhtml_out.print(_common_attributes[j]);
                                    _xhtml_out.print("\"");
                                    if (i < _values.size()) {
                                        _xhtml_out.print(" value=\"");
                                        _xhtml_out.print(_values.get(i));
                                        _xhtml_out.println("\"");
                                    } else if (_user != null && _user.hasAttribute(_common_attributes[j]) && i < _user.getAttribute(_common_attributes[j]).length) {
                                        _xhtml_out.print(" value=\"");
                                        _xhtml_out.print(String.valueOf(_user.getAttribute(_common_attributes[j])[i]));
                                        _xhtml_out.println("\"");
                                    }
                                    _xhtml_out.println("/>");
                                    if (i == 0) {
                                        _xhtml_out.print("<a href=\"javascript:addOu();\"><img src=\"/images/add_16.png\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.add"));
                                        _xhtml_out.print("\" alt=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.add"));
                                        _xhtml_out.println("\"/></a>");
                                    }
                                    _xhtml_out.println("</div>");
                                }
                            } else if ("telephoneNumber".equals(_common_attributes[j])) {
                                List<String> _values = new ArrayList<String>();
                                if (request.getParameterValues("telephoneNumber") != null) {
                                    _values.addAll(Arrays.asList(request.getParameterValues("telephoneNumber")));
                                }
                                for (int i = 0; i < num_telephone || i < _values.size(); i++) {
                                    _xhtml_out.println("<div class=\"standard_form\">");
                                    _xhtml_out.print("<label");
                                    if (i == 0) {
                                        _xhtml_out.print(" for=\"");
                                        _xhtml_out.print(_common_attributes[j]);
                                        _xhtml_out.print("\">");
                                        _xhtml_out.print(getLanguageMessage("directory.user." + _common_attributes[j]));
                                        _xhtml_out.println(": ");
                                    } else {
                                        _xhtml_out.print(">");
                                    }
                                    _xhtml_out.println("</label>");
                                    _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"");
                                    _xhtml_out.print(_common_attributes[j]);
                                    _xhtml_out.print("\"");
                                    if (i < _values.size()) {
                                        _xhtml_out.print(" value=\"");
                                        _xhtml_out.print(_values.get(i));
                                        _xhtml_out.println("\"");
                                    } else if (_user != null && _user.hasAttribute(_common_attributes[j]) && i < _user.getAttribute(_common_attributes[j]).length) {
                                        _xhtml_out.print(" value=\"");
                                        _xhtml_out.print(String.valueOf(_user.getAttribute(_common_attributes[j])[i]));
                                        _xhtml_out.println("\"");
                                    }
                                    _xhtml_out.println("/>");
                                    if (i == 0) {
                                        _xhtml_out.print("<a href=\"javascript:addTelephone();\"><img src=\"/images/add_16.png\" title=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.add"));
                                        _xhtml_out.print("\" alt=\"");
                                        _xhtml_out.print(getLanguageMessage("common.message.add"));
                                        _xhtml_out.println("\"/></a>");
                                    }
                                    _xhtml_out.println("</div>");
                                }
                            } else {
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"");
                                _xhtml_out.print(_common_attributes[j]);
                                _xhtml_out.print("\">");
                                _xhtml_out.print(getLanguageMessage("directory.user." + _common_attributes[j]));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"");
                                _xhtml_out.print(_common_attributes[j]);
                                _xhtml_out.print("\"");
                                if (request.getParameter(_common_attributes[j]) != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter(_common_attributes[j]));
                                    _xhtml_out.print("\"");
                                } else if (_user != null && _user.hasAttribute(_common_attributes[j])) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(_user.getAttributeFirstStringValue(_common_attributes[j]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.println("/>");
                                _xhtml_out.println("</div>");
                            }
                        }
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"window_float\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("directory.user.attributes.nt"));
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<fieldset>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"sambaHomePath\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.sambaHomePath"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaHomePath\"");
                        if (request.getParameter("sambaHomePath") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("sambaHomePath"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("sambaHomePath")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("sambaHomePath"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"sambaHomeDrive\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.sambaHomeDrive"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<select class=\"form_select\" name=\"sambaHomeDrive\">");
                        String _drive = "Z:";
                        String[] _drives = new String[] { "Z:", "W:", "X:", "U:", "T:", "S:", "R:", "Q:", "P:", "O:", "M:", "N:", "L:", "K:", "J:", "I:", "H:", "G:", "F:", "E:" };
                        if (_user != null) {
                            if (_user.hasAttribute("sambaHomeDrive")) {
                                _drive = _user.getAttributeFirstStringValue("sambaHomeDrive");
                            } else if (_c.getProperty("samba.home.drive") != null) {
                                _drive = _c.getProperty("samba.home.drive");
                            }
                        } else if (_c.getProperty("samba.home.drive") != null) {
                            _drive = _c.getProperty("samba.home.drive");
                        }
                        for (int j = (_drives.length - 1); j >= 0; --j) {
                            _xhtml_out.print("<option");
                            if (_drives[j].equals(_drive)) {
                                _xhtml_out.print(" selected");
                            }
                            _xhtml_out.print("> " + _drives[j] + "</option>");
                        }
                        _xhtml_out.print("</select>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"sambaProfilePath\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.sambaProfilePath"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaProfilePath\"");
                        if (request.getParameter("sambaProfilePath") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("sambaProfilePath"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("sambaProfilePath")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("sambaProfilePath"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"sambaLogonScript\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.sambaLogonScript"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaLogonScript\"");
                        if (request.getParameter("sambaLogonScript") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("sambaLogonScript"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("sambaLogonScript")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("sambaLogonScript"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"sambapwdNoChange\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.pwdNoChange"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_checkbox\" type=\"checkbox\" name=\"sambapwdNoChange\" value=\"true\"");
                        if (_user != null && _user.hasAttribute("sambaAcctFlags") && _user.getAttributeFirstStringValue("sambaAcctFlags").contains("X")) {
                            _xhtml_out.print(" checked=\"checked\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"pwdDisabled\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.pwdDisabled"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_checkbox\" type=\"checkbox\" name=\"pwdDisabled\" value=\"true\"");
                        if (_user != null && _user.hasAttribute("sambaAcctFlags") && _user.getAttributeFirstStringValue("sambaAcctFlags").contains("D")) {
                            _xhtml_out.print(" checked=\"checked\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"window_float\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("directory.user.attributes.posix"));
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<fieldset>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"uid\">");
                        _xhtml_out.print(getLanguageMessage("directory.common.login"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"uid\"");
                        if (request.getParameter("uid") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("uid"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("uid")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("uid"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"homeDirectory\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.homeDirectory"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"homeDirectory\"");
                        if (request.getParameter("homeDirectory") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("homeDirectory"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("homeDirectory")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("homeDirectory"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("uid")) {
                            _xhtml_out.print(" value=\"/home/");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("uid"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        for (int j = _posix_attributes.length - 1; j >= 0; j--) {
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label for=\"");
                            _xhtml_out.print(_posix_attributes[j]);
                            _xhtml_out.print("\">");
                            _xhtml_out.print(getLanguageMessage("directory.user." + _posix_attributes[j]));
                            _xhtml_out.println(": </label>");
                            _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"" + _posix_attributes[j] + "\"");
                            if (_user != null && _user.hasAttribute(_posix_attributes[j])) {
                                _xhtml_out.print(" value=\"");
                                _xhtml_out.print(_user.getAttributeFirstStringValue(_posix_attributes[j]));
                                _xhtml_out.print("\"");
                            }
                            _xhtml_out.print("/>");
                            _xhtml_out.println("</div>");
                        }
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"loginShell\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.loginShell"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.println("<select class=\"form_select\" name=\"loginShell\">");
                        _attributes = new String[] { "/bin/sh", "/bin/bash", "/bin/csh", "/bin/false" };
                        for (String _name : _attributes) {
                            _xhtml_out.print("<option value=\"");
                            _xhtml_out.print(_name);
                            _xhtml_out.print("\"");
                            if (_user != null && _user.checkAttributeFirstValue("loginshell", _name)) {
                                _xhtml_out.print(" selected=\"selected\"");
                            }
                            _xhtml_out.print(">");
                            _xhtml_out.print(_name);
                            _xhtml_out.print("</option>");
                        }
                        _xhtml_out.println("</select>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"gidNumber\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.gidNumber"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.println("<select class=\"form_select\" name=\"gidNumber\">");
                        boolean selected = false;
                        for (com.whitebearsolutions.directory.Entry _g : _groups) {
                            if (_sc.getAdministrativeGroup(512).equals(_g.getAttribute("cn")[0]) || _sc.getAdministrativeGroup(513).equals(_g.getAttribute("cn")[0]) || _sc.getAdministrativeGroup(514).equals(_g.getAttribute("cn")[0]) || _sc.getAdministrativeGroup(515).equals(_g.getAttribute("cn")[0])) {
                                continue;
                            }
                            _xhtml_out.print("<option value=\"");
                            _xhtml_out.print(String.valueOf(_g.getAttribute("gidNumber")[0]));
                            _xhtml_out.print("\"");
                            if (!selected && _user != null && _user.checkAttributeFirstValue("gidnumber", _g.getAttribute("gidNumber")[0])) {
                                selected = true;
                                _xhtml_out.print(" selected=\"selected\"");
                            } else if (!selected && "1001".equals(_g.getAttribute("gidNumber")[0])) {
                                selected = true;
                                _xhtml_out.print(" selected=\"selected\"");
                            }
                            _xhtml_out.print(">");
                            _xhtml_out.print(String.valueOf(_g.getAttribute("cn")[0]));
                            _xhtml_out.println("</option>");
                        }
                        if (!selected && _user != null && _user.hasAttribute("gidnumber")) {
                            try {
                                com.whitebearsolutions.directory.Entry _g = _gm.getGroupEntry(Integer.parseInt(_user.getAttributeFirstStringValue("gidnumber")));
                                _xhtml_out.print("<option value=\"");
                                _xhtml_out.print(String.valueOf(_g.getAttribute("gidNumber")[0]));
                                _xhtml_out.print("\" selected=\"selected\">");
                                _xhtml_out.print(String.valueOf(_g.getAttribute("cn")[0]));
                                _xhtml_out.println("</option>");
                            } catch (NumberFormatException _ex) {
                            }
                        }
                        _xhtml_out.println("</select>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        Map<String, Boolean> attributetypes = new TreeMap<String, Boolean>(Collator.getInstance(new Locale("es")));
                        attributetypes.putAll(_sm.getUserAttributes());
                        if (!attributetypes.isEmpty()) {
                            _xhtml_out.println("<div class=\"window_float\">");
                            _xhtml_out.println("<h2>");
                            _xhtml_out.print(getLanguageMessage("directory.common.attributes.optional"));
                            _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.save"));
                            _xhtml_out.print("\" alt=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.save"));
                            _xhtml_out.println("\"/></a>");
                            _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                            _xhtml_out.print("\" alt=\"");
                            _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                            _xhtml_out.println("\"/></a>");
                            _xhtml_out.println("</h2>");
                            _xhtml_out.println("<fieldset>");
                            for (String _name : attributetypes.keySet()) {
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"loginShell\">");
                                _xhtml_out.print(_name);
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"" + _name + "\"");
                                if (request.getParameter(_name) != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter(_name));
                                    _xhtml_out.print("\"");
                                } else if (_user != null && _user.hasAttribute(_name)) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(_user.getAttributeFirstStringValue(_name));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.print("/>");
                                if (attributetypes.get(_name)) {
                                    _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                                }
                                _xhtml_out.println("</div>");
                            }
                            _xhtml_out.println("</fieldset>");
                            _xhtml_out.println("<div class=\"clear\"></div>");
                            _xhtml_out.println("</div>");
                        }
                        _xhtml_out.println("<div class=\"window_float\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("directory.user.attributes.mail"));
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<fieldset>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"maildrop\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.mail"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"maildrop\"");
                        if (request.getParameter("maildrop") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("maildrop"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("maildrop")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("maildrop"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("mail")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("mail"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.println("/>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"mailbox\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.mailbox"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"mailbox\"");
                        if (request.getParameter("mailbox") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("mailbox"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("mailbox")) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(_user.getAttributeFirstStringValue("mailbox"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.println("/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"quota\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.quota"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"quota\"");
                        if (request.getParameter("quota") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("quota"));
                            _xhtml_out.print("\"");
                        } else if (_user != null && _user.hasAttribute("quota")) {
                            String sQuota = _user.getAttributeFirstStringValue("quota");
                            int quota = -1;
                            if (sQuota.toUpperCase().endsWith("S")) {
                                sQuota = sQuota.substring(0, sQuota.length() - 1);
                            }
                            try {
                                quota = Integer.parseInt(sQuota);
                            } catch (NumberFormatException _ex) {
                            }
                            if (quota != -1) {
                                quota = quota / 1048576;
                            }
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(quota);
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.println("/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"window_float\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("directory.user.attributes.security"));
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<fieldset>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"accountStatus\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.account.status"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.println("<select class=\"form_select\" name=\"accountStatus\">");
                        _xhtml_out.print("<option value=\"enabled\" selected=\"selected\">");
                        _xhtml_out.print(getLanguageMessage("common.message.enabled"));
                        _xhtml_out.println("</option>");
                        _xhtml_out.print("<option value=\"disabled\">");
                        _xhtml_out.print(getLanguageMessage("common.message.disabled"));
                        _xhtml_out.println("</option>");
                        _xhtml_out.println("</select>");
                        _xhtml_out.print("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"userPassword\">");
                        _xhtml_out.print(getLanguageMessage("common.login.password"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"password\" name=\"userPassword\"");
                        if (request.getParameter("userPassword") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("userPassword"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"standard_form\">");
                        _xhtml_out.print("<label for=\"password\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.userPasswordConfirmation"));
                        _xhtml_out.println(": </label>");
                        _xhtml_out.print("<input class=\"form_text\" type=\"password\" name=\"userPasswordConfirmation\"");
                        if (request.getParameter("userPasswordConfirmation") != null) {
                            _xhtml_out.print(" value=\"");
                            _xhtml_out.print(request.getParameter("userPasswordConfirmation"));
                            _xhtml_out.print("\"");
                        }
                        _xhtml_out.print("/>");
                        _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                    }
                    break;
                case STORE_NEW:
                    {
                        _attributes = new String[] { "uid", "givenName", "sn", "homeDirectory", "loginShell", "maildrop", "userPassword", "userPasswordConfirmation" };
                        for (int j = _attributes.length - 1; j >= 0; --j) {
                            if (request.getParameter(_attributes[j]) == null || request.getParameter(_attributes[j]).isEmpty()) {
                                throw new Exception(getLanguageMessage("directory.entry.exception_attribute") + getLanguageMessage("directory.user." + _attributes[j]) + "]");
                            } else if (!request.getParameter("userPassword").equals(request.getParameter("userPasswordConfirmation"))) {
                                throw new Exception(getLanguageMessage("system.general.exception.password"));
                            }
                        }
                        _attributes = new String[] { "uid", "givenName", "sn", "title", "ou", "o", "employeeType", "employeeNumber", "mobile", "telephoneNumber", "facsimileTelephoneNumber", "street", "postalCode", "l", "st", "sambaHomePath", "sambaHomeDrive", "sambaProfilePath", "sambaLogonScript", "shadowMax", "shadowMin", "shadowWarning", "shadowInactive", "homeDirectory", "loginShell" };
                        if (_um.userExists(request.getParameter("uid"))) {
                            throw new Exception(getLanguageMessage("directory.entry.exception.user_prev") + request.getParameter("uid") + getLanguageMessage("directory.entry.exception.user_post"));
                        }
                        AttributeSet _entry_attributes = new AttributeSet();
                        com.whitebearsolutions.directory.Entry _e = _um.getNewUserEntry(request.getParameter("uid"), request.getParameter("branch"));
                        if (!this.securityManager.checkWritePermissions(_e.getID())) {
                            throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                        }
                        try {
                            _entry_attributes.setAttribute("gidnumber", Integer.parseInt(request.getParameter("gidNumber")));
                        } catch (NumberFormatException _ex) {
                            throw new Exception(getLanguageMessage("directory.entry.exception.gidnumber"));
                        }
                        for (String name : _attributes) {
                            if (request.getParameter(name) != null && !request.getParameter(name).isEmpty()) {
                                List<String> _values = new ArrayList<String>();
                                for (String _value : request.getParameterValues(name)) {
                                    if (!_value.isEmpty()) {
                                        _values.add(_value);
                                    }
                                }
                                if (!_values.isEmpty()) {
                                    _entry_attributes.setAttribute(name, _values.toArray());
                                }
                            }
                        }
                        if (request.getParameter("pwdNoChange") != null && !request.getParameter("pwdNoChange").isEmpty()) {
                            _entry_attributes.setAttribute("pwdnochange", request.getParameter("pwdNoChange"));
                        }
                        if (request.getParameter("pwdDisabled") != null && !request.getParameter("pwdDisabled").isEmpty()) {
                            _entry_attributes.setAttribute("pwddisabled", request.getParameter("pwdDisabled"));
                        }
                        Map<String, Boolean> attributetypes = new TreeMap<String, Boolean>();
                        attributetypes.putAll(_sm.getUserAttributes());
                        if (!attributetypes.isEmpty()) {
                            for (String name : new ArrayList<String>(attributetypes.keySet())) {
                                if (request.getParameter(name) != null && !request.getParameter(name).isEmpty()) {
                                    for (String value : request.getParameterValues(name)) {
                                        _entry_attributes.setAttribute(name, value);
                                    }
                                } else if (attributetypes.get(name)) {
                                    throw new Exception(getLanguageMessage("directory.entry.exception.optional_attr_prev") + name + getLanguageMessage("directory.entry.exception.optional_attr_post"));
                                }
                            }
                        }
                        _entry_attributes.setAttribute("maildrop", request.getParameter("maildrop"));
                        _entry_attributes.setAttribute("mailbox", request.getParameter("mailbox"));
                        _entry_attributes.setAttribute("quota", request.getParameter("quota"));
                        _entry_attributes.setAttribute("password", request.getParameter("userPassword"));
                        if (request.getParameter("accountStatus") != null) {
                            _entry_attributes.setAttribute("accountStatus", request.getParameter("accountStatus"));
                        }
                        StringBuilder _sb = new StringBuilder();
                        RuleEngine _re = new RuleEngine(this.securityManager);
                        int _answer = _re.writeEntry(RuleEngine.USER_ADD, _entry_attributes, request.getParameter("branch"));
                        if (_answer == RuleEngine.ANSWER_CONFIRMATION) {
                            _sb.append(getLanguageMessage("directory.entry.correctly_confirmation"));
                        } else {
                            _sb.append(getLanguageMessage("directory.entry.correctly"));
                        }
                        if (request.getParameter("copyUid") != null) {
                            try {
                                _e = _um.getUserEntry(request.getParameter("uid"));
                                _re.writeMembershipEntry(RuleEngine.GROUP_USER_ADD, _gm.getUserGroups(_um.getUserEntry(request.getParameter("copyUid"))), _e);
                            } catch (Exception _ex) {
                                _sb.append(getLanguageMessage("directory.entry.copy_group"));
                            }
                            writeDocumentContinueResponse(_sb.toString(), "/admin/Directory", "/admin/Entry");
                        } else if (this.sessionManager.hasObjectSession("entryGroups")) {
                            Map<String, String> _failGroups = new HashMap<String, String>();
                            List<com.whitebearsolutions.directory.Entry> _groups = new ArrayList<com.whitebearsolutions.directory.Entry>();
                            for (String name : (List<String>) this.sessionManager.getObjectSession("entryGroups")) {
                                try {
                                    _groups.add(_gm.getGroupEntry(name));
                                } catch (Exception _ex) {
                                    _failGroups.put(name, _ex.getMessage());
                                }
                            }
                            this.sessionManager.removeObjectSession("entryGroups");
                            _re.writeMembershipEntry(RuleEngine.GROUP_USER_ADD, _groups, _um.getUserEntry(request.getParameter("uid")));
                            if (_failGroups.size() > 0) {
                                _sb.append(getLanguageMessage("directory.entry.copy_groups"));
                                writeDocumentPartialContinueResponse(_sb.toString(), new ArrayList<String>(_failGroups.keySet()), "/admin/Directory", "/admin/Entry");
                            } else {
                                writeDocumentContinueResponse(_sb.toString(), "/admin/Directory", "/admin/Entry");
                            }
                        } else {
                            writeDocumentContinueResponse(_sb.toString(), "/admin/Directory", "/admin/Entry");
                        }
                    }
                    break;
                case UPDATE:
                    {
                        com.whitebearsolutions.directory.Entry _e = _um.getUserEntry(request.getParameter("uid"));
                        if (!_e.getID().equals(this.securityManager.getUserDN()) && !this.securityManager.checkReadPermissions(_e.getID())) {
                            throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                        }
                        String _user_branch = "";
                        List<String> _branches = this.securityManager.getReadUserBranches();
                        List<com.whitebearsolutions.directory.Entry> _groups = this.securityManager.getWriteUserGroups();
                        List<String> _restricted_attributes = this.securityManager.getAttributeRestrictions(_e.getID());
                        int num_ou = 1, num_telephone = 1;
                        if (request.getParameter("branch") != null) {
                            _user_branch = request.getParameter("branch");
                        } else {
                            _user_branch = _bm.getBranchPath(_e.getID());
                        }
                        if (request.getParameter("num_ou") != null) {
                            try {
                                num_ou = Integer.parseInt(request.getParameter("num_ou"));
                            } catch (NumberFormatException _ex) {
                            }
                        } else if (_e.hasAttribute("ou")) {
                            num_ou = _e.getAttribute("ou").length;
                        }
                        if (request.getParameter("num_telephone") != null) {
                            try {
                                num_telephone = Integer.parseInt(request.getParameter("num_telephone"));
                            } catch (NumberFormatException _ex) {
                            }
                        } else if (_e.hasAttribute("telephoneNumber")) {
                            num_telephone = _e.getAttribute("telephoneNumber").length;
                        }
                        _xhtml_out.println("<script>");
                        _xhtml_out.println("<!--");
                        _xhtml_out.println("function addOu() {");
                        _xhtml_out.println("  var n = parseInt(document.entry.num_ou.value);");
                        _xhtml_out.println("  document.entry.type.value = " + UPDATE + ";");
                        _xhtml_out.println("  document.entry.num_ou.value = (n + 1);");
                        _xhtml_out.println("  submitForm(document.entry.submit());");
                        _xhtml_out.println("}");
                        _xhtml_out.println("function addTelephone() {");
                        _xhtml_out.println("  var n = parseInt(document.entry.num_telephone.value);");
                        _xhtml_out.println("  document.entry.type.value = " + UPDATE + ";");
                        _xhtml_out.println("  document.entry.num_telephone.value = (n + 1);");
                        _xhtml_out.println("  submitForm(document.entry.submit());");
                        _xhtml_out.println("}");
                        _xhtml_out.println("//-->");
                        _xhtml_out.println("</script>");
                        StringBuilder _back = new StringBuilder();
                        _back.append("/admin/Entry?uid=");
                        _back.append(request.getParameter("uid"));
                        _back.append("&type=");
                        _back.append(Entry.UPDATE);
                        writeDocumentBack();
                        pushDocumentBack(_back.toString());
                        _xhtml_out.println("<form action=\"/admin/Entry\" name=\"entry\" method=\"post\">");
                        _xhtml_out.println("<input type=\"hidden\" name=\"type\" value=\"" + STORE_UPDATE + "\"/>");
                        if (_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0])) {
                            _xhtml_out.println("<input type=\"hidden\" name=\"uid\" value=\"" + request.getParameter("uid") + "\"/>");
                        } else {
                            _xhtml_out.println("<input type=\"hidden\" name=\"uid\" value=\"" + _e.getAttribute("uid")[0] + "\"/>");
                        }
                        if (_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0]) || !this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_BASIC)) {
                            _xhtml_out.println("<input type=\"hidden\" name=\"branch\" value=\"" + _user_branch + "\"/>");
                        }
                        _xhtml_out.println("<input type=\"hidden\" name=\"num_ou\" value=\"" + num_ou + "\"/>");
                        _xhtml_out.println("<input type=\"hidden\" name=\"num_telephone\" value=\"" + num_telephone + "\"/>");
                        _xhtml_out.println("<h1>");
                        _xhtml_out.print("<img src=\"/images/user_32.png\"/>");
                        _xhtml_out.print(_e.getID());
                        _xhtml_out.println("</h1>");
                        _xhtml_out.println("<div class=\"info\">");
                        _xhtml_out.print(getLanguageMessage("directory.user.info"));
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div id=\"entry_content\" class=\"entry_content\">");
                        if (!_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0])) {
                            if (this.securityManager.checkReadPermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_BASIC)) {
                                _xhtml_out.println("<div class=\"window_float\">");
                                _xhtml_out.println("<h2>");
                                _xhtml_out.print(getLanguageMessage("directory.common.attributes.basic"));
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.println("</h2>");
                                _xhtml_out.println("<fieldset>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"name\">");
                                _xhtml_out.print(getLanguageMessage("directory.common.name"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"givenName\"");
                                if (request.getParameter("givenName") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("givenName"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("givenName")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("givenName")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sn\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sn"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sn\"");
                                if (request.getParameter("sn") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("sn"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("sn")) {
                                    _xhtml_out.print(" value=\"" + _e.getAttribute("sn")[0] + "\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"branch\">");
                                _xhtml_out.print(getLanguageMessage("common.message.branch"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.println("<select class=\"form_select\" name=\"branch\">");
                                if (this.securityManager.isRoot()) {
                                    _xhtml_out.print("<option value=\"\">");
                                    _xhtml_out.print(getLanguageMessage("directory.common.root"));
                                    _xhtml_out.println("</option>");
                                }
                                if (!_user_branch.isEmpty() && !_branches.contains(_user_branch)) {
                                    _xhtml_out.print("<option value=\"");
                                    _xhtml_out.print(_user_branch);
                                    _xhtml_out.print("\" selected=\"selected\">");
                                    _xhtml_out.print(_user_branch);
                                    _xhtml_out.println("</option>");
                                }
                                for (String _branch : _branches) {
                                    _xhtml_out.print("<option value=\"");
                                    _xhtml_out.print(_branch);
                                    _xhtml_out.print("\"");
                                    if (_branch.equals(_user_branch)) {
                                        _xhtml_out.print(" selected=\"selected\"");
                                    }
                                    _xhtml_out.print(">");
                                    _xhtml_out.print(_branch);
                                    _xhtml_out.println("</option>");
                                }
                                _xhtml_out.println("</select>");
                                _xhtml_out.println("</div>");
                                for (int j = _common_attributes.length - 1; j >= 0; j--) {
                                    if (_restricted_attributes.contains(_common_attributes[j])) {
                                        continue;
                                    } else if ("ou".equals(_common_attributes[j])) {
                                        List<String> _values = new ArrayList<String>();
                                        if (request.getParameterValues(_common_attributes[j]) != null) {
                                            _values.addAll(Arrays.asList(request.getParameterValues(_common_attributes[j])));
                                        }
                                        for (int i = 0; i < num_ou || i < _values.size(); i++) {
                                            _xhtml_out.println("<div class=\"standard_form\">");
                                            _xhtml_out.print("<label");
                                            if (i == 0) {
                                                _xhtml_out.print(" for=\"");
                                                _xhtml_out.print(_common_attributes[j]);
                                                _xhtml_out.print("\">");
                                                _xhtml_out.print(getLanguageMessage("directory.user." + _common_attributes[j]));
                                                _xhtml_out.print(": ");
                                            } else {
                                                _xhtml_out.print(">");
                                            }
                                            _xhtml_out.println("</label>");
                                            _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"");
                                            _xhtml_out.print(_common_attributes[j]);
                                            _xhtml_out.print("\"");
                                            if (i < _values.size()) {
                                                _xhtml_out.print(" value=\"");
                                                _xhtml_out.print(_values.get(i));
                                                _xhtml_out.print("\"");
                                            } else if (_e.hasAttribute(_common_attributes[j]) && i < _e.getAttribute(_common_attributes[j]).length) {
                                                _xhtml_out.print(" value=\"");
                                                _xhtml_out.print(String.valueOf(_e.getAttribute(_common_attributes[j])[i]));
                                                _xhtml_out.print("\"");
                                            }
                                            _xhtml_out.println("/>");
                                            if (i == 0) {
                                                _xhtml_out.println("<a href=\"javascript:addOu();\"><img src=\"/images/add_16.png\" title=\"");
                                                _xhtml_out.print(getLanguageMessage("common.message.add"));
                                                _xhtml_out.print("\" alt=\"");
                                                _xhtml_out.print(getLanguageMessage("common.message.add"));
                                                _xhtml_out.println("\"/></a>");
                                            }
                                            _xhtml_out.println("</div>");
                                        }
                                    } else if ("telephoneNumber".equals(_common_attributes[j])) {
                                        List<String> _values = new ArrayList<String>();
                                        if (request.getParameterValues("telephoneNumber") != null) {
                                            _values.addAll(Arrays.asList(request.getParameterValues("telephoneNumber")));
                                        }
                                        for (int i = 0; i < num_telephone || i < _values.size(); i++) {
                                            _xhtml_out.println("<div class=\"standard_form\">");
                                            _xhtml_out.print("<label");
                                            if (i == 0) {
                                                _xhtml_out.print(" for=\"");
                                                _xhtml_out.print(_common_attributes[j]);
                                                _xhtml_out.print("\">");
                                                _xhtml_out.print(getLanguageMessage("directory.user." + _common_attributes[j]));
                                                _xhtml_out.print(": ");
                                            } else {
                                                _xhtml_out.print(">");
                                            }
                                            _xhtml_out.println("</label>");
                                            _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"");
                                            _xhtml_out.print(_common_attributes[j]);
                                            _xhtml_out.print("\"");
                                            if (i < _values.size()) {
                                                _xhtml_out.print(" value=\"");
                                                _xhtml_out.print(_values.get(i));
                                                _xhtml_out.print("\"");
                                            } else if (_e.hasAttribute(_common_attributes[j]) && i < _e.getAttribute(_common_attributes[j]).length) {
                                                _xhtml_out.print(" value=\"");
                                                _xhtml_out.print(String.valueOf(_e.getAttribute(_common_attributes[j])[i]));
                                                _xhtml_out.print("\"");
                                            }
                                            _xhtml_out.println("/>");
                                            if (i == 0) {
                                                _xhtml_out.print("<a href=\"javascript:addTelephone();\"><img src=\"/images/add_16.png\" title=\"");
                                                _xhtml_out.print(getLanguageMessage("common.message.add"));
                                                _xhtml_out.print("\" alt=\"");
                                                _xhtml_out.print(getLanguageMessage("common.message.add"));
                                                _xhtml_out.println("\"/></a>");
                                            }
                                            _xhtml_out.println("</div>");
                                        }
                                    } else {
                                        _xhtml_out.println("<div class=\"standard_form\">");
                                        _xhtml_out.print("<label for=\"");
                                        _xhtml_out.print(_common_attributes[j]);
                                        _xhtml_out.print("\">");
                                        _xhtml_out.print(getLanguageMessage("directory.user." + _common_attributes[j]));
                                        _xhtml_out.println(": </label>");
                                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"");
                                        _xhtml_out.print(_common_attributes[j]);
                                        _xhtml_out.print("\"");
                                        if (request.getParameter(_common_attributes[j]) != null) {
                                            _xhtml_out.print(" value=\"");
                                            _xhtml_out.print(request.getParameter(_common_attributes[j]));
                                            _xhtml_out.print("\"");
                                        } else if (_e.hasAttribute(_common_attributes[j])) {
                                            _xhtml_out.print(" value=\"");
                                            _xhtml_out.print(String.valueOf(_e.getAttribute(_common_attributes[j])[0]));
                                            _xhtml_out.print("\"");
                                        }
                                        _xhtml_out.println("/>");
                                        _xhtml_out.println("</div>");
                                    }
                                }
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"name\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.photo"));
                                _xhtml_out.println(": </label>");
                                if (_e.hasAttribute("jpegPhoto") && _c.getProperty("directory.images.path") != null) {
                                    File _f = new File(_c.getProperty("directory.images.path") + File.separator + _e.getAttribute("uid")[0] + ".jpg");
                                    if (!_f.exists()) {
                                        ByteArrayInputStream _bais = new ByteArrayInputStream((byte[]) _e.getAttribute("jpegPhoto")[0]);
                                        FileOutputStream _fos = new FileOutputStream(_f);
                                        for (; _bais.available() > 0; _fos.write(_bais.read())) ;
                                        _fos.close();
                                    }
                                    _xhtml_out.print("<img class=\"directory_entry\" src=\"");
                                    _xhtml_out.print(_c.getProperty("directory.images.url"));
                                    _xhtml_out.print("/");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                                    _xhtml_out.print(".jpg\" />");
                                } else {
                                    _xhtml_out.print("<img class=\"directory_entry\" src=\"");
                                    _xhtml_out.print(_c.getProperty("directory.images.url"));
                                    _xhtml_out.print("/nophoto.png\" />");
                                }
                                _xhtml_out.print("<a class=\"form_a\" href=\"/admin/Attribute?uid=");
                                _xhtml_out.print(request.getParameter("uid"));
                                _xhtml_out.print("&attr=jpegPhoto\">");
                                _xhtml_out.print(getLanguageMessage("common.message.update"));
                                _xhtml_out.println("<img src=\"/images/wizard_16.png\"/></a>");
                                _xhtml_out.print("</div>");
                                _xhtml_out.println("</fieldset>");
                                _xhtml_out.println("<div class=\"clear\"></div>");
                                _xhtml_out.println("</div>");
                            }
                            if (this.securityManager.checkReadPermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_NT)) {
                                _xhtml_out.println("<div class=\"window_float\">");
                                _xhtml_out.println("<h2>");
                                _xhtml_out.print(getLanguageMessage("directory.user.attributes.nt"));
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.println("</h2>");
                                _xhtml_out.println("<fieldset>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambaHomePath\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sambaHomePath"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaHomePath\"");
                                if (request.getParameter("sambaHomePath") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("sambaHomePath"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("sambaHomePath")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("sambaHomePath")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambaHomeDrive\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sambaHomeDrive"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<select class=\"form_select\" name=\"sambaHomeDrive\">");
                                String _drive = "Z:";
                                String[] _drives = new String[] { "Z:", "W:", "X:", "U:", "T:", "S:", "R:", "Q:", "P:", "O:", "M:", "N:", "L:", "K:", "J:", "I:", "H:", "G:", "F:", "E:" };
                                if (_e.hasAttribute("sambaHomeDrive")) {
                                    _drive = String.valueOf(_e.getAttribute("sambaHomeDrive")[0]);
                                } else if (_c.getProperty("samba.home.drive") != null) {
                                    _drive = _c.getProperty("samba.home.drive");
                                }
                                for (int j = (_drives.length - 1); j >= 0; --j) {
                                    _xhtml_out.print("<option");
                                    if (_drives[j].equals(_drive)) {
                                        _xhtml_out.print(" selected=\"selected\"");
                                    }
                                    _xhtml_out.print("> " + _drives[j] + "</option>");
                                }
                                _xhtml_out.print("</select>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambaProfilePath\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sambaProfilePath"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaProfilePath\"");
                                if (request.getParameter("sambaProfilePath") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("sambaProfilePath"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("sambaProfilePath")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("sambaProfilePath")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambaLogonScript\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sambaLogonScript"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaLogonScript\"");
                                if (request.getParameter("sambaLogonScript") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("sambaLogonScript"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("sambaLogonScript")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("sambaLogonScript")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambaKickoffTime\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sambaKickoffTime"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaKickoffTime\" disabled=\"disabled\"");
                                if (_e.hasAttribute("sambaKickoffTime")) {
                                    _xhtml_out.print(" value=\"" + _e.getAttribute("sambaKickoffTime")[0] + "\"");
                                }
                                _xhtml_out.println("/>");
                                _xhtml_out.print("<a href=\"/admin/Attribute?uid=");
                                _xhtml_out.print(request.getParameter("uid"));
                                _xhtml_out.print("&attr=sambaKickoffTime\">");
                                _xhtml_out.println("<img src=\"/images/wizard_16.png\"/></a>");
                                _xhtml_out.print("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambaPwdCanChange\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sambaPwdCanChange"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaPwdCanChange\" disabled=\"disabled\"");
                                if (_e.hasAttribute("sambaPwdCanChange")) {
                                    _xhtml_out.print(" value=\"" + _e.getAttribute("sambaPwdCanChange")[0] + "\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.print("<a href=\"/admin/Attribute?uid=");
                                _xhtml_out.print(request.getParameter("uid"));
                                _xhtml_out.print("&attr=sambaPwdCanChange\">");
                                _xhtml_out.println("<img src=\"/images/wizard_16.png\"/></a>");
                                _xhtml_out.print("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambaPwdMustChange\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.sambaPwdMustChange"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"sambaPwdMustChange\" disabled=\"disabled\"");
                                if (_e.hasAttribute("sambaPwdMustChange")) {
                                    _xhtml_out.print(" value=\"" + _e.getAttribute("sambaPwdMustChange")[0] + "\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.print("<a href=\"/admin/Attribute?uid=");
                                _xhtml_out.print(request.getParameter("uid"));
                                _xhtml_out.print("&attr=sambaPwdMustChange\">");
                                _xhtml_out.println("<img src=\"/images/wizard_16.png\"/></a>");
                                _xhtml_out.print("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"sambapwdNoChange\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.pwdNoChange"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_checkbox\" type=\"checkbox\" name=\"pwdNoChange\" value=\"true\"");
                                if (_e.hasAttribute("sambaAcctFlags") && String.valueOf(_e.getAttribute("sambaAcctFlags")[0]).contains("X")) {
                                    _xhtml_out.print(" checked=\"checked\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"pwdDisabled\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.pwdDisabled"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_checkbox\" type=\"checkbox\" name=\"pwdDisabled\" value=\"true\"");
                                if (_e.hasAttribute("sambaAcctFlags") && String.valueOf(_e.getAttribute("sambaAcctFlags")[0]).contains("D")) {
                                    _xhtml_out.print(" checked=\"checked\"");
                                }
                                _xhtml_out.print("/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("</fieldset>");
                                _xhtml_out.println("<div class=\"clear\"></div>");
                                _xhtml_out.println("</div>");
                            }
                            if (this.securityManager.checkReadPermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_POSIX)) {
                                _xhtml_out.println("<div class=\"window_float\">");
                                _xhtml_out.println("<h2>");
                                _xhtml_out.print(getLanguageMessage("directory.user.attributes.posix"));
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.println("</h2>");
                                _xhtml_out.println("<fieldset>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"uidNumber\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.uidNumber"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"uidNumber\"");
                                if (request.getParameter("uidNumber") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("uidNumber"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("uidNumber")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("uidNumber")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.println("/>");
                                _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"homeDirectory\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.homeDirectory"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"homeDirectory\"");
                                if (request.getParameter("homeDirectory") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("homeDirectory"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("homeDirectory")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("homeDirectory")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.println("/>");
                                _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                                _xhtml_out.println("</div>");
                                for (int j = _posix_attributes.length - 1; j >= 0; j--) {
                                    _xhtml_out.println("<div class=\"standard_form\">");
                                    _xhtml_out.print("<label for=\"");
                                    _xhtml_out.print(_posix_attributes[j]);
                                    _xhtml_out.print("\">");
                                    _xhtml_out.print(getLanguageMessage("directory.user." + _posix_attributes[j]));
                                    _xhtml_out.println(": </label>");
                                    _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"" + _posix_attributes[j] + "\"");
                                    if (request.getParameter(_posix_attributes[j]) != null) {
                                        _xhtml_out.print(" value=\"");
                                        _xhtml_out.print(request.getParameter(_posix_attributes[j]));
                                        _xhtml_out.print("\"");
                                    } else if (_e.hasAttribute(_posix_attributes[j])) {
                                        _xhtml_out.print(" value=\"" + _e.getAttribute(_posix_attributes[j])[0] + "\"");
                                    }
                                    _xhtml_out.println("/>");
                                    _xhtml_out.println("</div>");
                                }
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"loginShell\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.loginShell"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.println("<select class=\"form_select\" name=\"loginShell\">");
                                _attributes = new String[] { "/bin/sh", "/bin/bash", "/bin/csh", "/bin/false" };
                                for (String _name : _attributes) {
                                    _xhtml_out.print("<option value=\"");
                                    _xhtml_out.print(_name);
                                    _xhtml_out.print("\"");
                                    if (String.valueOf(_e.getAttribute("loginShell")[0]).equals(_name)) {
                                        _xhtml_out.print(" selected=\"selected\"");
                                    }
                                    _xhtml_out.print(">");
                                    _xhtml_out.print(_name);
                                    _xhtml_out.print("</option>");
                                }
                                _xhtml_out.println("</select>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"gidNumber\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.gidNumber"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.println("<select class=\"form_select\" name=\"gidNumber\">");
                                boolean selected = false;
                                for (com.whitebearsolutions.directory.Entry _g : _groups) {
                                    if (_sc.getAdministrativeGroup(512).equals(_g.getAttribute("cn")[0]) || _sc.getAdministrativeGroup(513).equals(_g.getAttribute("cn")[0]) || _sc.getAdministrativeGroup(514).equals(_g.getAttribute("cn")[0]) || _sc.getAdministrativeGroup(515).equals(_g.getAttribute("cn")[0])) {
                                        continue;
                                    }
                                    _xhtml_out.print("<option value=\"");
                                    _xhtml_out.print(String.valueOf(_g.getAttribute("gidNumber")[0]));
                                    _xhtml_out.print("\"");
                                    if (!selected && _e.hasAttribute("gidNumber") && _e.getAttribute("gidNumber")[0].equals(_g.getAttribute("gidNumber")[0])) {
                                        selected = true;
                                        _xhtml_out.print(" selected=\"selected\"");
                                    } else if (!selected && "1001".equals(_g.getAttribute("gidNumber")[0])) {
                                        if (_e == null || !_e.hasAttribute("gidNumber")) {
                                            selected = true;
                                            _xhtml_out.print(" selected=\"selected\"");
                                        }
                                    }
                                    _xhtml_out.print(">");
                                    _xhtml_out.print(String.valueOf(_g.getAttribute("cn")[0]));
                                    _xhtml_out.println("</option>");
                                }
                                if (!selected && _e != null && _e.hasAttribute("gidNumber")) {
                                    try {
                                        com.whitebearsolutions.directory.Entry _g = _gm.getGroupEntry(Integer.parseInt(String.valueOf(_e.getAttribute("gidNumber")[0])));
                                        _xhtml_out.print("<option value=\"");
                                        _xhtml_out.print(String.valueOf(_g.getAttribute("gidNumber")[0]));
                                        _xhtml_out.print("\" selected=\"selected\">");
                                        _xhtml_out.print(String.valueOf(_g.getAttribute("cn")[0]));
                                        _xhtml_out.println("</option>");
                                    } catch (NumberFormatException _ex) {
                                    }
                                }
                                _xhtml_out.println("</select>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"gidNumber\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.user_groups"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<a class=\"form_a\" href=\"/admin/Group?type=");
                                _xhtml_out.print(Group.MANAGE);
                                _xhtml_out.print("&uid=");
                                _xhtml_out.print(HTTPURL.encode(request.getParameter("uid")));
                                _xhtml_out.print("\">");
                                _xhtml_out.print(getLanguageMessage("common.message.admin"));
                                _xhtml_out.print("<img src=\"/images/wizard_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.wizard"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.wizard"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("</fieldset>");
                                _xhtml_out.println("<div class=\"clear\"></div>");
                                _xhtml_out.println("</div>");
                            }
                            if (this.securityManager.checkReadPermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_OPTIONAL)) {
                                TreeMap<String, Boolean> attributetypes = new TreeMap<String, Boolean>(Collator.getInstance(new Locale("es")));
                                attributetypes.putAll(_sm.getUserAttributes());
                                if (!attributetypes.isEmpty()) {
                                    _xhtml_out.println("<div class=\"window_float\">");
                                    _xhtml_out.println("<h2>");
                                    _xhtml_out.print(getLanguageMessage("directory.common.attributes.optional"));
                                    _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.save"));
                                    _xhtml_out.print("\" alt=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.save"));
                                    _xhtml_out.println("\"/></a>");
                                    _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                    _xhtml_out.print("\" alt=\"");
                                    _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                    _xhtml_out.println("\"/></a>");
                                    _xhtml_out.println("</h2>");
                                    _xhtml_out.println("<fieldset>");
                                    for (String _name : attributetypes.keySet()) {
                                        _xhtml_out.println("<div class=\"standard_form\">");
                                        _xhtml_out.print("<label for=\"");
                                        _xhtml_out.print(_name);
                                        _xhtml_out.print("\">");
                                        _xhtml_out.print(_name);
                                        _xhtml_out.println(": </label>");
                                        _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"" + _name + "\"");
                                        if (request.getParameter(_name) != null) {
                                            _xhtml_out.print(" value=\"");
                                            _xhtml_out.print(request.getParameter(_name));
                                            _xhtml_out.print("\"");
                                        } else if (_e.hasAttribute(_name)) {
                                            _xhtml_out.print(" value=\"" + _e.getAttribute(_name)[0] + "\"");
                                        }
                                        _xhtml_out.print("/>");
                                        if (attributetypes.get(_name)) {
                                            _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                                        }
                                        _xhtml_out.println("</div>");
                                    }
                                    _xhtml_out.println("</fieldset>");
                                    _xhtml_out.println("<div class=\"clear\"></div>");
                                    _xhtml_out.println("</div>");
                                }
                            }
                            if (this.securityManager.checkReadPermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_MAIL)) {
                                _xhtml_out.println("<div class=\"window_float\">");
                                _xhtml_out.println("<h2>");
                                _xhtml_out.print(getLanguageMessage("directory.user.attributes.mail"));
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.save"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.print("\" alt=\"");
                                _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                                _xhtml_out.println("\"/></a>");
                                _xhtml_out.println("</h2>");
                                _xhtml_out.println("<fieldset>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"maildrop\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.mail"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"maildrop\"");
                                if (request.getParameter("maildrop") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("maildrop"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("maildrop")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("maildrop")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.println("/>");
                                _xhtml_out.println("<img src=\"/images/asterisk_orange_16.png\"/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"mailbox\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.mailbox"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"mailbox\"");
                                if (request.getParameter("mailbox") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("mailbox"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("mailbox")) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(String.valueOf(_e.getAttribute("mailbox")[0]));
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.println("/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"quota\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.quota"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<input class=\"form_text\" type=\"text\" name=\"quota\"");
                                if (request.getParameter("quota") != null) {
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(request.getParameter("quota"));
                                    _xhtml_out.print("\"");
                                } else if (_e.hasAttribute("quota")) {
                                    String sQuota = String.valueOf(_e.getAttribute("quota")[0]);
                                    int quota = -1;
                                    if (sQuota.toUpperCase().endsWith("S")) {
                                        sQuota = sQuota.substring(0, sQuota.length() - 1);
                                    }
                                    try {
                                        quota = Integer.parseInt(sQuota);
                                    } catch (NumberFormatException _ex) {
                                    }
                                    if (quota != -1) {
                                        quota = quota / 1048576;
                                    }
                                    _xhtml_out.print(" value=\"");
                                    _xhtml_out.print(quota);
                                    _xhtml_out.print("\"");
                                }
                                _xhtml_out.println("/>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.print("<label for=\"aliases\">");
                                _xhtml_out.print(getLanguageMessage("directory.user.mailalias"));
                                _xhtml_out.println(": </label>");
                                _xhtml_out.print("<a href=\"/admin/Attribute?uid=");
                                _xhtml_out.print(request.getParameter("uid"));
                                _xhtml_out.print("&attr=mail\"><img src=\"/images/email_add_16.png\"/></a>");
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.println("<label></label>");
                                if (_e.hasAttribute("mail")) {
                                    _xhtml_out.println("<table>");
                                    for (Object _alias : _e.getAttribute("mail")) {
                                        if (_e.hasAttribute("maildrop") && !_alias.equals(_e.getAttribute("maildrop")[0])) {
                                            _xhtml_out.print("<tr><td>");
                                            _xhtml_out.print(String.valueOf(_alias));
                                            _xhtml_out.println("</td>");
                                            _xhtml_out.println("<td>");
                                            _xhtml_out.print("<a href=\"/admin/Attribute?uid=");
                                            _xhtml_out.print(request.getParameter("uid"));
                                            _xhtml_out.print("&type=");
                                            _xhtml_out.print(Attribute.DELETE);
                                            _xhtml_out.print("&mail=");
                                            _xhtml_out.print(String.valueOf(_alias));
                                            _xhtml_out.print("&attr=mail\">");
                                            _xhtml_out.print("<img src=\"/images/email_delete_16.png\" title=\"");
                                            _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                            _xhtml_out.print("\" alt=\"");
                                            _xhtml_out.print(getLanguageMessage("common.message.delete"));
                                            _xhtml_out.println("\"/></a>");
                                            _xhtml_out.print("</td></tr>");
                                        }
                                    }
                                    _xhtml_out.println("</table>");
                                }
                                _xhtml_out.println("</div>");
                                _xhtml_out.println("</fieldset>");
                                _xhtml_out.println("<div class=\"clear\"></div>");
                                _xhtml_out.println("</div>");
                            }
                        }
                        _xhtml_out.println("<div class=\"window_float\">");
                        _xhtml_out.println("<h2>");
                        _xhtml_out.print(getLanguageMessage("directory.user.attributes.security"));
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.entry.submit());\"><img src=\"/images/disk_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.save"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.print("<a href=\"javascript:submitForm(document.location.reload());\"><img src=\"/images/arrow_refresh_16.png\" title=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.print("\" alt=\"");
                        _xhtml_out.print(getLanguageMessage("common.message.refresh"));
                        _xhtml_out.println("\"/></a>");
                        _xhtml_out.println("</h2>");
                        _xhtml_out.println("<fieldset>");
                        if (!_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0]) && this.securityManager.checkWritePermissions(_e.getID())) {
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label for=\"accountStatus\">");
                            _xhtml_out.print(getLanguageMessage("directory.user.account.status"));
                            _xhtml_out.println(": </label>");
                            _xhtml_out.println("<select class=\"form_select\" name=\"accountStatus\">");
                            _xhtml_out.print("<option value=\"enabled\"");
                            if (!_e.hasAttribute("accountEnableStatus") || !"disabled".equals(String.valueOf(_e.getAttribute("accountEnableStatus")[0]))) {
                                _xhtml_out.print(" selected=\"selected\"");
                            }
                            _xhtml_out.print(">");
                            _xhtml_out.print(getLanguageMessage("common.message.enabled"));
                            _xhtml_out.println("</option>");
                            _xhtml_out.print("<option value=\"disabled\"");
                            if (_e.hasAttribute("accountEnableStatus") && "disabled".equals(String.valueOf(_e.getAttribute("accountEnableStatus")[0]))) {
                                _xhtml_out.print(" selected=\"selected\"");
                            }
                            _xhtml_out.print(">");
                            _xhtml_out.print(getLanguageMessage("common.message.disabled"));
                            _xhtml_out.println("</option>");
                            _xhtml_out.println("</select>");
                            _xhtml_out.println("</div>");
                        }
                        if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_SECURITY_PASSWORD)) {
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label for=\"userPassword\">");
                            _xhtml_out.print(getLanguageMessage("common.login.password"));
                            _xhtml_out.println(": </label>");
                            _xhtml_out.print("<input class=\"form_text\" type=\"password\" name=\"userPassword\"");
                            if (request.getParameter("userPassword") != null) {
                                _xhtml_out.print(" value=\"");
                                _xhtml_out.print(request.getParameter("userPassword"));
                                _xhtml_out.print("\"");
                            }
                            _xhtml_out.print("/>");
                            _xhtml_out.print("</div>");
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label for=\"userPasswordConfirmation\">");
                            _xhtml_out.print(getLanguageMessage("directory.user.userPasswordConfirmation"));
                            _xhtml_out.println(": </label>");
                            _xhtml_out.print("<input class=\"form_text\" type=\"password\" name=\"userPasswordConfirmation\"");
                            if (request.getParameter("userPasswordConfirmation") != null) {
                                _xhtml_out.print(" value=\"");
                                _xhtml_out.print(request.getParameter("userPasswordConfirmation"));
                                _xhtml_out.print("\"");
                            }
                            _xhtml_out.print("/>");
                            _xhtml_out.print("</div>");
                        }
                        if (!_sc.getAdministrativeUser().equals(_e.getAttribute("uid")[0])) {
                            _xhtml_out.println("<div class=\"standard_form\">");
                            _xhtml_out.print("<label for=\"certificate\">");
                            _xhtml_out.print(getLanguageMessage("directory.user.certificate"));
                            _xhtml_out.println(": </label>");
                            if (_e.hasAttribute("userSMIMECertificate")) {
                                _xhtml_out.print("<a href=\"/admin/PKI?uid=");
                                _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                                _xhtml_out.print("&attr=userSMIMECertificate&type=");
                                _xhtml_out.print(PKI.DOWNLOAD);
                                _xhtml_out.println("\"><img src=\"/images/rosette_green_16.png\"/>SMIME/PEM</a>");
                                if (this.securityManager.checkReadPermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_SECURITY_PRIVATE_CERTIFICATE)) {
                                    CertificateManager _cm = this.sessionManager.getCertificateManager();
                                    if (_cm.hasUserCertificate(String.valueOf(_e.getAttribute("uid")[0]))) {
                                        _xhtml_out.print("<a href=\"/admin/PKI?uid=");
                                        _xhtml_out.print(String.valueOf(_e.getAttribute("uid")[0]));
                                        _xhtml_out.print("&attr=userPKCS12&type=");
                                        _xhtml_out.print(PKI.DOWNLOAD);
                                        _xhtml_out.println("\"><img src=\"/images/rosette_red_16.png\"/>PKCS#12</a></td>");
                                    }
                                }
                            }
                            _xhtml_out.println("</div>");
                            if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_SECURITY_CERTIFICATE)) {
                                _xhtml_out.println("<div class=\"standard_form\">");
                                _xhtml_out.println("<label></label>");
                                _xhtml_out.print("<a class=\"form_a\" href=\"/admin/PKI?uid=");
                                _xhtml_out.print(request.getParameter("uid"));
                                _xhtml_out.print("&attr=userSMIMECertificate&type=");
                                _xhtml_out.print(PKI.NEW);
                                _xhtml_out.print("\">");
                                _xhtml_out.print(getLanguageMessage("common.message.update"));
                                _xhtml_out.print("<img src=\"/images/wizard_16.png\"/></a>");
                                _xhtml_out.println("</div>");
                            }
                        }
                        _xhtml_out.println("</fieldset>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                        _xhtml_out.println("<div class=\"clear\"></div>");
                        _xhtml_out.println("</div>");
                    }
                    break;
                case STORE_UPDATE:
                    {
                        if (!this.securityManager.isLogged()) {
                            throw new Exception(getLanguageMessage("directory.entry.exception.active_session"));
                        }
                        com.whitebearsolutions.directory.Entry _e = _um.getUserEntry(request.getParameter("uid"));
                        if (!_e.getID().equals(this.securityManager.getUserDN()) && !this.securityManager.checkWritePermissions(_e.getID())) {
                            throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                        }
                        _attributes = new String[] {};
                        if (!_sc.getAdministrativeUser().equals(request.getParameter("uid")) && this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_BASIC)) {
                            _attributes = new String[] { "givenName", "sn" };
                            for (int j = _attributes.length - 1; j >= 0; --j) {
                                if (request.getParameter(_attributes[j]) == null || request.getParameter(_attributes[j]).length() < 1) {
                                    throw new Exception(getLanguageMessage("directory.entry.no_attribute") + _attributes[j] + "]");
                                }
                            }
                        }
                        AttributeSet _entry_attributes = new AttributeSet();
                        _entry_attributes.setAttribute("uid", request.getParameter("uid"));
                        if (!_sc.getAdministrativeUser().equals(request.getParameter("uid"))) {
                            if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_BASIC)) {
                                _entry_attributes.setAttribute("givenName", request.getParameter("givenName"));
                                _entry_attributes.setAttribute("sn", request.getParameter("sn"));
                                _attributes = new String[] { "title", "ou", "o", "employeeType", "employeeNumber", "mobile", "telephoneNumber", "facsimileTelephoneNumber", "street", "postalCode", "l", "st" };
                                for (int j = _attributes.length - 1; j >= 0; --j) {
                                    if ("ou".equals(_attributes[j]) || "telephoneNumber".equals(_attributes[j])) {
                                        List<String> _def_values = new ArrayList<String>();
                                        String[] _values = request.getParameterValues(_attributes[j]);
                                        for (int u = 0; u < _values.length; u++) {
                                            if (_values[u] != null && !_values[u].trim().isEmpty()) {
                                                _def_values.add(_values[u]);
                                            }
                                        }
                                        if (_def_values.size() > 0) {
                                            _entry_attributes.setAttribute(_attributes[j].toLowerCase(), _def_values.toArray());
                                        } else {
                                            _entry_attributes.setAttribute(_attributes[j].toLowerCase(), null);
                                        }
                                    } else {
                                        _entry_attributes.setAttribute(_attributes[j].toLowerCase(), request.getParameter(_attributes[j]));
                                    }
                                }
                            }
                            if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_OPTIONAL)) {
                                Map<String, Boolean> attributetypes = new TreeMap<String, Boolean>();
                                attributetypes.putAll(_sm.getUserAttributes());
                                if (!attributetypes.isEmpty()) {
                                    for (String name : new ArrayList<String>(attributetypes.keySet())) {
                                        if (request.getParameter(name) != null && !request.getParameter(name).isEmpty()) {
                                            _entry_attributes.setAttribute(name, request.getParameter(name));
                                        }
                                    }
                                }
                            }
                            if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_NT)) {
                                if (request.getParameter("pwdNoChange") != null && !request.getParameter("pwdNoChange").isEmpty()) {
                                    _entry_attributes.setAttribute("pwdNoChange", request.getParameter("pwdNoChange"));
                                }
                                if (request.getParameter("pwdDisabled") != null && !request.getParameter("pwdDisabled").isEmpty()) {
                                    _entry_attributes.setAttribute("pwdDisabled", request.getParameter("pwdDisabled"));
                                }
                                _attributes = new String[] { "sambaHomePath", "sambaHomeDrive", "sambaProfilePath", "sambaLogonScript" };
                                for (int j = _attributes.length - 1; j >= 0; j--) {
                                    _entry_attributes.setAttribute(_attributes[j], request.getParameter(_attributes[j]));
                                }
                            }
                            if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_POSIX)) {
                                _entry_attributes.setAttribute("uidNumber", request.getParameter("uidNumber"));
                                _entry_attributes.setAttribute("gidNumber", request.getParameter("gidNumber"));
                                _attributes = new String[] { "shadowMax", "shadowMin", "shadowWarning", "shadowInactive", "homeDirectory", "loginShell" };
                                for (int j = _attributes.length - 1; --j >= 0; ) {
                                    _entry_attributes.setAttribute(_attributes[j].toLowerCase(), request.getParameter(_attributes[j]));
                                }
                            }
                            if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_MAIL)) {
                                _entry_attributes.setAttribute("maildrop", request.getParameter("maildrop"));
                                _entry_attributes.setAttribute("mailbox", request.getParameter("mailbox"));
                                _entry_attributes.setAttribute("quota", request.getParameter("quota"));
                            }
                        }
                        if (this.securityManager.checkWritePermissions(_e.getID())) {
                            if (request.getParameter("accountStatus") != null && !request.getParameter("accountStatus").isEmpty()) {
                                _entry_attributes.setAttribute("accountStatus", request.getParameter("accountStatus"));
                            }
                        }
                        if (this.securityManager.checkWritePermissions(_e.getID(), SecurityManager.USER_ATTRIBUTES_SECURITY_PASSWORD)) {
                            if (request.getParameter("userPassword") != null && !request.getParameter("userPassword").isEmpty()) {
                                if (!request.getParameter("userPassword").equals(request.getParameter("userPasswordConfirmation"))) {
                                    throw new Exception(getLanguageMessage("system.general.exception.password"));
                                }
                                _entry_attributes.setAttribute("password", request.getParameter("userPassword"));
                            }
                        }
                        RuleEngine _re = new RuleEngine(this.securityManager);
                        int _answer = _re.writeEntry(RuleEngine.USER_UPDATE, _entry_attributes, request.getParameter("branch"));
                        if (_answer == RuleEngine.ANSWER_CONFIRMATION) {
                            writeDocumentResponse(getLanguageMessage("directory.entry.user_confirmation"), "/admin/Entry?uid=" + request.getParameter("uid") + "&type=" + Entry.UPDATE);
                        } else {
                            writeDocumentResponse(getLanguageMessage("directory.entry.user_ok"), "/admin/Entry?uid=" + request.getParameter("uid") + "&type=" + Entry.UPDATE);
                        }
                    }
                    break;
                case DELETE:
                    {
                        if (request.getParameter("confirm") != null) {
                            com.whitebearsolutions.directory.Entry _e = _um.getUserEntry(request.getParameter("uid"));
                            if (!this.securityManager.checkWritePermissions(_e.getID())) {
                                throw new Exception(getLanguageMessage("common.message.no_privilegios"));
                            }
                            AttributeSet _entry_attributes = new AttributeSet();
                            _entry_attributes.setAttribute("uid", request.getParameter("uid"));
                            RuleEngine _re = new RuleEngine(this.securityManager);
                            int _answer = _re.writeEntry(RuleEngine.USER_REMOVE, _entry_attributes);
                            if (_answer == RuleEngine.ANSWER_CONFIRMATION) {
                                if (!request.getParameter("uid").contains("$")) {
                                    writeDocumentResponse(getLanguageMessage("directory.entry.user_delete_confirmation"), getDocumentBack("/admin/Directory"));
                                } else {
                                    writeDocumentResponse(getLanguageMessage("directory.entry.computer_delete_confirmation"), getDocumentBack("/admin/Directory"));
                                }
                            } else {
                                if (!request.getParameter("uid").contains("$")) {
                                    writeDocumentResponse(getLanguageMessage("directory.entry.user_delete_ok"), getDocumentBack("/admin/Directory"));
                                } else {
                                    writeDocumentResponse(getLanguageMessage("directory.entry.computer_delete_ok"), getDocumentBack("/admin/Directory"));
                                }
                            }
                        } else {
                            if (!request.getParameter("uid").contains("$")) {
                                writeDocumentQuestion(getLanguageMessage("directory.entry.question_delete_user") + " <strong>" + request.getParameter("uid") + "</strong>?", "/admin/Entry?uid=" + request.getParameter("uid") + "&type=" + DELETE + "&confirm=true", null);
                            } else {
                                writeDocumentQuestion(getLanguageMessage("directory.entry.question_delete_computer") + " <strong>" + request.getParameter("uid") + "</strong>?", "/admin/Entry?uid=" + request.getParameter("uid") + "&type=" + DELETE + "&confirm=true", null);
                            }
                        }
                    }
                    break;
            }
        } catch (Exception _ex) {
            writeDocumentError(_ex.getMessage());
        } finally {
            writeDocumentFooter();
        }
    }
