    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestManager req = new RequestManager(request);
        SessionManager session = new SessionManager(request);
        UserLoginBeans user = (UserLoginBeans) session.get("#USER#");
        if ((req.command("") || req.command("FIND")) && user.r(UGRP)) {
            int pageNumber = (req.getString("page").equalsIgnoreCase("")) ? 1 : Integer.parseInt(req.getString("page"));
            int pageTotal = 1;
            int rowCount = 0;
            String listTheme = "";
            String ext = req.getInt("perm") == 0 ? "" : "AND PERMISSION = " + req.getInt("perm");
            Connection conn = Connector.getConnection();
            try {
                rowCount = SQL.rowCount("SELECT * FROM USER_GROUP WHERE NAME LIKE '%" + req.getString("keyword") + "%' " + ext, conn);
                pageTotal = new Double(Math.ceil(new Integer(rowCount).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();
                if (pageNumber > pageTotal) {
                    pageNumber = pageTotal;
                }
                int start = ((pageNumber - 1) * rowLimit) + 1;
                Statement stm = conn.createStatement();
                ResultSet raw = SQL.limit("SELECT * FROM USER_GROUP WHERE NAME LIKE '%" + req.getString("keyword") + "%' " + ext + " ORDER BY ID DESC", stm, start, rowLimit);
                while (raw.next()) {
                    HashMap<String, String> listMap = new HashMap<String, String>();
                    listMap.put(":V_USER_GROUP_ID:", raw.getString("ID"));
                    listMap.put(":V_USER_GROUP_NAME:", raw.getString("NAME"));
                    listMap.put(":V_PERMISSION:", UserGroupController.getPermissionName(raw.getInt("PERMISSION")));
                    listMap.putAll(map);
                    listTheme += html.render("/user_group_list.html", listMap);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
            map.put(":V_BEGIN:", "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=1");
            map.put(":V_PREVIEW:", ((pageNumber - 1) < 1) ? "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=1" : "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + (pageNumber - 1));
            map.put(":V_NEXT:", ((pageNumber + 1) > pageTotal) ? "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + pageTotal : "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + (pageNumber + 1));
            map.put(":V_END:", "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + pageTotal);
            map.put(":V_JUMP_URL:", "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=");
            String opt = OptionManager.permissionOption(req.getInt("perm"), true);
            map.put(":PERMISSION_OPTION:", opt);
            map.put(":DATA_LIST:", listTheme);
            map.put(":V_KEYWORD:", req.getString("keyword"));
            map.put(":V_ROW_COUNT:", Integer.toString(rowCount));
            map.put(":V_PAGE_NUMBER:", Integer.toString(pageNumber));
            map.put(":V_PAGE_TOTAL:", Integer.toString(pageTotal));
            content += html.render("/user_group.html", map);
            html.output(content);
        } else if (req.command("NEW") && user.w(UGRP)) {
            map.put(":V_USER_GROUP_ID:", "");
            map.put(":V_USER_GROUP_NAME:", "");
            map.put(":PERMISSION_OPTION:", OptionManager.permissionOption(Constant.STRANGER, false));
            map.put(":READ_OPTION:", getReadAccessOption(""));
            map.put(":WRITE_OPTION:", getWriteAccessOption(""));
            map.put(":EXEC_OPTION:", getExecAccessOption(""));
            map.put(":STORE_OPTION:", getStoreAccessOption(""));
            map.put(":PROGROUP_OPTION:", getProductGroupAccessOption(""));
            map.put(":CMD:", "ADD");
            content += html.render("/user_group_form.html", map);
            html.output(content);
        } else if (req.command("INFO") && user.r(UGRP)) {
            if (!req.getString("id").equalsIgnoreCase("")) {
                Connection conn = Connector.getConnection();
                try {
                    Statement stm = conn.createStatement();
                    ResultSet raw = stm.executeQuery("SELECT * FROM USER_GROUP WHERE ID = " + req.getString("id"));
                    while (raw.next()) {
                        map.put(":V_USER_GROUP_ID:", raw.getString("ID"));
                        map.put(":V_USER_GROUP_NAME:", raw.getString("NAME"));
                        map.put(":PERMISSION_OPTION:", OptionManager.permissionOption(raw.getInt("PERMISSION"), false));
                        map.put(":READ_OPTION:", getReadAccessOption(deNull(raw.getString("MODR"))));
                        map.put(":WRITE_OPTION:", getWriteAccessOption(deNull(raw.getString("MODW"))));
                        map.put(":EXEC_OPTION:", getExecAccessOption(deNull(raw.getString("MODX"))));
                        map.put(":STORE_OPTION:", getStoreAccessOption(deNull(raw.getString("MODS"))));
                        map.put(":PROGROUP_OPTION:", getProductGroupAccessOption(deNull(raw.getString("MODP"))));
                    }
                    raw.close();
                    stm.close();
                    map.put(":CMD:", "EDIT");
                    content += html.render("/user_group_form.html", map);
                    html.output(content);
                } catch (SQLException ex) {
                    log4j.error("SQL Exception", ex);
                    map.put(":V_ICON:", ICON_ERROR);
                    map.put(":V_MESSAGE:", I18n.get(":M_SQL_EXCEPTION:", EnvironmentController.getString("LOCALE")));
                    map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                    content += html.render("/common/message.html", map);
                    html.output(content);
                }
                Connector.close(conn);
            } else {
                map.put(":V_ICON:", ICON_ERROR);
                map.put(":V_MESSAGE:", I18n.get(":M_ID_BLANK:", EnvironmentController.getString("LOCALE")));
                map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                content += html.render("/common/message.html", map);
                html.output(content);
            }
        } else if (req.command("ADD") && user.w(UGRP)) {
            if (!req.getString("ugName").equalsIgnoreCase("")) {
                String[] r = req.getStrings("read");
                String[] w = req.getStrings("write");
                String[] x = req.getStrings("exec");
                String[] s = req.getStrings("store");
                String[] p = req.getStrings("progroup");
                String read = ":";
                String write = ":";
                String exec = ":";
                String store = ":";
                String progroup = ":";
                if (r != null) {
                    for (int i = 0; i < r.length; i++) {
                        read += r[i] + ":";
                    }
                }
                if (w != null) {
                    for (int i = 0; i < w.length; i++) {
                        write += w[i] + ":";
                    }
                }
                if (x != null) {
                    for (int i = 0; i < x.length; i++) {
                        exec += x[i] + ":";
                    }
                }
                if (s != null) {
                    for (int i = 0; i < s.length; i++) {
                        store += s[i] + ":";
                    }
                }
                if (p != null) {
                    for (int i = 0; i < p.length; i++) {
                        progroup += p[i] + ":";
                    }
                }
                Connection conn = Connector.getConnection();
                try {
                    Statement stm = conn.createStatement();
                    stm.executeUpdate("INSERT INTO USER_GROUP (ID, NAME, PERMISSION, MODR, MODW, MODX, MODS, MODP) values (" + "'', " + "'" + req.getString("ugName") + "', " + "'" + req.getString("perm") + "', " + "'" + read + "', " + "'" + write + "', " + "'" + exec + "', " + "'" + store + "', " + "'" + progroup + "'" + ")");
                    stm.close();
                    map.put(":V_ICON:", ICON_INFO);
                    map.put(":V_MESSAGE:", I18n.get(":M_ADD_DONE:", EnvironmentController.getString("LOCALE")));
                    map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                    content += html.render("/common/message.html", map);
                    html.output(content);
                    SystemController.log(user, SystemController.ADD, "USER_GROUP", "Add new user group '" + req.getString("ugName") + "'");
                } catch (SQLException ex) {
                    log4j.error("SQL Exception", ex);
                    map.put(":V_ICON:", ICON_ERROR);
                    map.put(":V_MESSAGE:", I18n.get(":M_ADD_FAIL:", EnvironmentController.getString("LOCALE")));
                    map.put(":V_LINK:", "history.go(-1)");
                    content += html.render("/common/message.html", map);
                    html.output(content);
                }
                Connector.close(conn);
            } else {
                map.put(":V_ICON:", ICON_ERROR);
                map.put(":V_MESSAGE:", I18n.get(":M_USER_GROUP_NAME_BLANK:", EnvironmentController.getString("LOCALE")));
                map.put(":V_LINK:", "history.go(-1)");
                content += html.render("/common/message.html", map);
                html.output(content);
            }
        } else if (req.command("EDIT") && user.r(UGRP)) {
            if (req.getInt("ugId") > 0 && !req.getString("ugName").isEmpty()) {
                if ((req.getInt("ugId") == 1 || req.getInt("ugId") == 2) && user.getPermission() < Constant.SYSTEM) {
                    map.put(":V_ICON:", ICON_ERROR);
                    map.put(":V_MESSAGE:", "Could not edit default data.");
                    map.put(":V_LINK:", "history.go(-1)");
                    content += html.render("/common/message.html", map);
                    html.output(content);
                } else {
                    String[] r = req.getStrings("read");
                    String[] w = req.getStrings("write");
                    String[] x = req.getStrings("exec");
                    String[] s = req.getStrings("store");
                    String[] p = req.getStrings("progroup");
                    String read = ":";
                    String write = ":";
                    String exec = ":";
                    String store = ":";
                    String progroup = ":";
                    if (r != null) {
                        for (int i = 0; i < r.length; i++) {
                            read += r[i] + ":";
                        }
                    }
                    if (w != null) {
                        for (int i = 0; i < w.length; i++) {
                            write += w[i] + ":";
                        }
                    }
                    if (x != null) {
                        for (int i = 0; i < x.length; i++) {
                            exec += x[i] + ":";
                        }
                    }
                    if (s != null) {
                        for (int i = 0; i < s.length; i++) {
                            store += s[i] + ":";
                        }
                    }
                    if (p != null) {
                        for (int i = 0; i < p.length; i++) {
                            progroup += p[i] + ":";
                        }
                    }
                    Connection conn = Connector.getConnection();
                    try {
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE USER_GROUP SET " + "NAME = '" + req.getString("ugName") + "', " + "PERMISSION = '" + req.getString("perm") + "', " + "MODR = '" + read + "', " + "MODW = '" + write + "', " + "MODX = '" + exec + "', " + "MODS = '" + store + "', " + "MODP = '" + progroup + "' " + "WHERE ID = " + req.getString("ugId"));
                        ResultSet raw = stm.executeQuery("SELECT * FROM USER_INFO_V WHERE ID LIKE '" + user.getId() + "'");
                        if (raw.next()) {
                            user = new UserLoginBeans(raw.getInt("ID"), raw.getString("CODE"), raw.getString("NAME"), raw.getInt("GPERMISSION"), raw.getInt("LOCALE"), deNull(raw.getString("MODR")), deNull(raw.getString("MODW")), deNull(raw.getString("MODX")), deNull(raw.getString("MODS")), deNull(raw.getString("MODP")));
                            session.set("#USER#", user);
                            raw.close();
                        }
                        stm.close();
                        map.put(":V_ICON:", ICON_INFO);
                        map.put(":V_MESSAGE:", I18n.get(":M_EDIT_DONE:", EnvironmentController.getString("LOCALE")));
                        map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                        content += html.render("/common/message.html", map);
                        html.output(content);
                        SystemController.log(user, SystemController.EDIT, "USER_GROUP", "Edit user group '" + req.getString("ugName") + "'");
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception", ex);
                        map.put(":V_ICON:", ICON_ERROR);
                        map.put(":V_MESSAGE:", I18n.get(":M_EDIT_FAIL:", EnvironmentController.getString("LOCALE")));
                        map.put(":V_LINK:", "history.go(-1)");
                        content += html.render("/common/message.html", map);
                        html.output(content);
                    }
                    Connector.close(conn);
                }
            } else {
                map.put(":V_ICON:", ICON_ERROR);
                map.put(":V_MESSAGE:", I18n.get(":M_FIELD_BLANK:", EnvironmentController.getString("LOCALE")));
                map.put(":V_LINK:", "history.go(-1)");
                content += html.render("/common/message.html", map);
                html.output(content);
            }
        } else if (req.command("DELETE") && user.x(UGRP)) {
            if (req.getInt("id") > 0) {
                if (req.getInt("id") == 1 || req.getInt("id") == 2) {
                    map.put(":V_ICON:", ICON_ERROR);
                    map.put(":V_MESSAGE:", "Could not delete default data.");
                    map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                    content += html.render("/common/message.html", map);
                    html.output(content);
                } else {
                    Connection conn = Connector.getConnection();
                    try {
                        String name = "";
                        Statement stm = conn.createStatement();
                        ResultSet raw = stm.executeQuery("SELECT NAME FROM USER_GROUP WHERE ID = " + req.getString("id"));
                        if (raw.next()) {
                            name = raw.getString("NAME");
                        }
                        stm.executeUpdate("DELETE USER_GROUP WHERE ID = " + req.getString("id"));
                        raw.close();
                        stm.close();
                        map.put(":V_ICON:", ICON_INFO);
                        map.put(":V_MESSAGE:", I18n.get(":M_DELETE_DONE:", EnvironmentController.getString("LOCALE")));
                        map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                        content += html.render("/common/message.html", map);
                        html.output(content);
                        SystemController.log(user, SystemController.DELETE, "USER_GROUP", "Delete user group '" + name + "'");
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception", ex);
                        map.put(":V_ICON:", ICON_ERROR);
                        if (ex.getErrorCode() == 2292) {
                            map.put(":V_MESSAGE:", I18n.get(":M_DATA_USING:", EnvironmentController.getString("LOCALE")));
                        } else {
                            map.put(":V_MESSAGE:", I18n.get(":M_DELETE_FAIL:", EnvironmentController.getString("LOCALE")));
                        }
                        map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                        content += html.render("/common/message.html", map);
                        html.output(content);
                        Connector.close(conn);
                    }
                }
            } else {
                map.put(":V_ICON:", ICON_ERROR);
                map.put(":V_MESSAGE:", I18n.get(":M_ID_BLANK:", EnvironmentController.getString("LOCALE")));
                map.put(":V_LINK:", "MaGic.Window.load('/ustock/UserGroup')");
                content += html.render("/common/message.html", map);
                html.output(content);
            }
        } else {
            response.sendError(403);
        }
    }
