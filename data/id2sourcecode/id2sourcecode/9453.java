    public synchronized void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = req.getSession();
        String op = (String) req.getParameter("op");
        if (op == null) {
            op = "apply_rule";
        }
        System.out.println("ALESSANDRA ----------------> op " + op);
        String actual_page = (String) session.getAttribute("actual_page");
        if (actual_page != null && actual_page.equals("apply_rules")) {
            op = "apply_rule";
        }
        String ret_url = null;
        if (op.equals("check_login")) {
            String login = (String) req.getParameter("login");
            String pwd = (String) req.getParameter("pwd");
            check_login(login, pwd, session);
            ret_url = "../login.jsp";
        } else {
            if (op.equals("create_rule")) {
                check_rule(session);
                DBmanager DBm = new DBmanager();
                String user_name = (String) session.getAttribute("user_name");
                user_name = user_name.trim();
                String ubl_type = (String) session.getAttribute("ubl_type");
                Calendar data = Calendar.getInstance();
                java.util.Date date = data.getTime();
                String rule_name = (String) session.getAttribute("rule_name_fv");
                String antecedents = (String) req.getParameter("p_antecedents");
                String consequences = (String) req.getParameter("p_consequences");
                String rule = antecedents + " --> " + consequences;
                DBm.connect(nomeDB, user, passwd);
                DBm.insertRule(user_name, ubl_type, date.toString(), rule_name, rule);
                DBm.disConnect();
                session.putValue("rule_name_fv", "");
                ret_url = "../create_rule.jsp";
            } else {
                if (op.equals("new_user")) {
                    String uid = (String) req.getParameter("uid");
                    DBmanager DBm = new DBmanager();
                    DBm.connect(nomeDB, user, passwd);
                    if (DBm.checklogin(uid)) {
                        session.putValue("new_login", "uid_false");
                        ret_url = "../login.jsp";
                    } else {
                        String password = (String) req.getParameter("password");
                        String password2 = (String) req.getParameter("password2");
                        if (!password.equals(password2)) {
                            session.putValue("new_login", "false");
                            ret_url = "../login.jsp";
                        } else {
                            String name = (String) req.getParameter("name");
                            String surname = (String) req.getParameter("surname");
                            String address = (String) req.getParameter("address");
                            String city = (String) req.getParameter("city");
                            String zip = (String) req.getParameter("zip");
                            String country = (String) req.getParameter("country");
                            String phone = (String) req.getParameter("phone");
                            String fax = (String) req.getParameter("fax");
                            String email = (String) req.getParameter("email");
                            String web = (String) req.getParameter("web");
                            String result = "";
                            String encryptedString = uid + password;
                            int i;
                            java.security.MessageDigest md = null;
                            try {
                                md = java.security.MessageDigest.getInstance("md5");
                            } catch (NoSuchAlgorithmException ex) {
                                ex.printStackTrace();
                            }
                            byte[] pw = encryptedString.getBytes();
                            for (i = 0; i < pw.length; i++) {
                                int vgl = pw[i];
                                if (vgl < 0) vgl += 256;
                                if (32 < vgl) md.update(pw[i]);
                            }
                            byte[] bresult = md.digest();
                            result = "";
                            for (i = 0; i < bresult.length; i++) {
                                int counter = bresult[i];
                                if (counter < 0) counter += 256;
                                String counterStr = Integer.toString(counter, 16);
                                while (counterStr.length() < 2) counterStr = '0' + counterStr;
                                result += counterStr;
                            }
                            DBm.connect(nomeDB, user, passwd);
                            DBm.insert_new_user(uid, result, name, surname, address, city, zip, country, phone, fax, email, web);
                            DBm.disConnect();
                            session.putValue("new_login", "true");
                            ret_url = "../login.jsp";
                        }
                    }
                    DBm.disConnect();
                } else {
                    if (op.equals("check_name_rule")) {
                        String user_name = (String) session.getAttribute("user_name");
                        String ubl_type = (String) session.getAttribute("ubl_type");
                        String rule_name = (String) req.getParameter("rule_name");
                        DBmanager DBm = new DBmanager();
                        DBm.connect(nomeDB, user, passwd);
                        if (DBm.titleChecker(user_name, rule_name, ubl_type)) {
                            session.putValue("check_name", "no");
                        } else {
                            session.putValue("check_name", "ok");
                            session.putValue("rule_name_fv", rule_name);
                        }
                        ret_url = "../create_rule.jsp";
                        DBm.disConnect();
                    } else {
                        if (op.equals("Continue")) {
                            String ubl_type = (String) req.getParameter("ubl_type");
                            session.putValue("ubl_type", ubl_type);
                            ret_url = "../home_page.jsp";
                        } else {
                            if (op.equals("del_rule")) {
                                String delList = (String) req.getParameter("delList");
                                StringTokenizer st = new StringTokenizer(delList, ",");
                                ArrayList delL = new ArrayList();
                                int count = 0;
                                while (st.hasMoreTokens()) {
                                    delL.add(count, st.nextToken());
                                    count++;
                                }
                                String user_name = (String) session.getAttribute("user_name");
                                String ubl_type = (String) session.getAttribute("ubl_type");
                                DBmanager DBm = new DBmanager();
                                DBm.connect(nomeDB, user, passwd);
                                DBm.deleteRule(user_name, ubl_type, delL);
                                DBm.disConnect();
                                ret_url = "../show_rules.jsp";
                            } else {
                                if (op.equals("apply_rule")) {
                                    try {
                                        String userName = (String) session.getAttribute("user_name");
                                        String receiver = (String) session.getAttribute("receiver");
                                        String ublType = (String) session.getAttribute("ubl_type");
                                        session.putValue("receiver", receiver);
                                        executeRule(req, session, userName + "_" + ublType);
                                        session.putValue("nameInferred", receiver + "_" + ublType);
                                        ret_url = "../apply_rules.jsp";
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    ;
                                } else {
                                }
                            }
                        }
                    }
                }
            }
        }
        response.setContentType("text/html");
        response.sendRedirect(ret_url);
    }
