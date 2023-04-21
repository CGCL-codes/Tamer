    public void execute() {
        executing = true;
        count = 0;
        Session ss = tools.Session.getSession();
        Session ss1 = tools.Session.getSession();
        Transaction tx = ss.beginTransaction();
        Transaction tx1 = ss1.beginTransaction();
        String sysfilepath = NewGenLibDesktopRoot.getRoot() + "/SystemFiles";
        try {
            Connection con = reports.utility.database.ConnectionPoolFactory.getInstance().getConnectionPool().getConnection();
            Statement stmt = con.createStatement();
            org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
            java.net.URL url = new java.net.URL(NewGenLibDesktopRoot.getInstance().getURLRoot() + "/SystemFiles");
            org.jdom.Document doc = sb.build(url.openStream());
            String importfilepath = doc.getRootElement().getChildTextTrim("PatronImportFileLocation");
            java.io.FileInputStream fism = new java.io.FileInputStream(new java.io.File(importfilepath));
            org.jdom.input.SAXBuilder sbm = new org.jdom.input.SAXBuilder();
            org.jdom.Document docm = sb.build(fism);
            java.util.List list = docm.getRootElement().getChildren("Patron");
            for (int i = 0; i < list.size(); i++) {
                org.jdom.Element ele = (org.jdom.Element) list.get(i);
                System.out.println(i);
                String id = ele.getAttributeValue("id");
                String fname = ele.getChild("Name").getChildText("fname");
                String mname = ele.getChild("Name").getChildText("mname");
                String lname = ele.getChild("Name").getChildText("lname");
                String startDate = ele.getChildText("MembershipStartDate");
                String endDate = ele.getChildText("MembershipEndDate");
                String department = ele.getChildText("Department");
                String pAddress = ele.getChildText("PermanentAddress");
                String cAddress = ele.getChildText("CurrentAddress");
                String emailId = ele.getChildText("EmailId");
                String category = ele.getChildText("PatronCategory");
                String course = ele.getChildText("Course");
                System.out.println("PATRON ID=" + id);
                java.util.StringTokenizer st = new java.util.StringTokenizer(startDate, "/");
                int tok = st.countTokens();
                int a[] = new int[tok];
                for (int j = 0; j < tok; j++) {
                    a[j] = Integer.parseInt(st.nextToken());
                }
                java.util.Calendar cal1 = java.util.Calendar.getInstance();
                cal1.set(cal1.HOUR, 0);
                cal1.set(cal1.MINUTE, 0);
                cal1.set(cal1.SECOND, 0);
                cal1.set(cal1.MILLISECOND, 0);
                cal1.set(cal1.DATE, a[0]);
                cal1.set(cal1.MONTH, a[1] - 1);
                cal1.set(cal1.YEAR, a[2]);
                java.util.StringTokenizer st1 = new java.util.StringTokenizer(endDate, "/");
                tok = st1.countTokens();
                int b[] = new int[tok];
                for (int k = 0; k < tok; k++) {
                    b[k] = Integer.parseInt(st1.nextToken());
                }
                java.util.Calendar cal2 = java.util.Calendar.getInstance();
                cal2.set(cal2.HOUR, 0);
                cal2.set(cal2.MINUTE, 0);
                cal2.set(cal2.SECOND, 0);
                cal2.set(cal2.MILLISECOND, 0);
                cal2.set(cal2.DATE, b[0]);
                cal2.set(cal2.MONTH, b[1] - 1);
                cal2.set(cal2.YEAR, b[2]);
                PATRON p = new PATRON();
                PATRON_KEY pk = new PATRON_KEY();
                PATRON_MANAGER pm = new PATRON_MANAGER();
                pk.setPatron_id(id);
                String loginlibid = reports.utility.StaticValues.getInstance().getLoginLibraryId();
                int libraryId = Integer.parseInt(loginlibid);
                pk.setLibrary_id(new Integer(libraryId));
                pk.setPatron_id(id);
                p.setPrimaryKey(pk);
                p.setFname(fname);
                if (mname.equals(null)) mname = "";
                p.setMname(mname);
                if (lname.equals(null)) lname = "";
                p.setLname(lname);
                java.sql.Timestamp memberStartDate = new java.sql.Timestamp(cal1.getTimeInMillis());
                p.setMembership_start_date(memberStartDate);
                java.sql.Timestamp memberExpireDate = new java.sql.Timestamp(cal2.getTimeInMillis());
                p.setMembership_expiry_date(memberExpireDate);
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(cal.HOUR, 0);
                cal.set(cal.MINUTE, 0);
                cal.set(cal.SECOND, 0);
                cal.set(cal.MILLISECOND, 0);
                java.sql.Timestamp entrydate = new java.sql.Timestamp(cal.getTimeInMillis());
                if (cAddress.equals(null)) cAddress = "";
                p.setAddress1(cAddress);
                if (pAddress.equals(null)) pAddress = "";
                p.setPaddress1(pAddress);
                p.setEmail(emailId);
                if (category.equals(null)) category = "";
                p.setPatron_type("B");
                p.setIsonline("Y");
                p.setOwns("");
                p.setCreated_on(entrydate);
                p.setPatron_type("B");
                p.setAddress2("");
                p.setCity("");
                p.setCountry("");
                p.setState("");
                p.setPin("");
                p.setPhone1("");
                p.setPhone2("");
                p.setFax("");
                p.setPaddress2("");
                p.setAddress2("");
                p.setPcity("");
                p.setPcountry("");
                p.setPemail("");
                p.setPfax("");
                p.setPphone1("");
                p.setPphone2("");
                p.setPpin("");
                p.setPstate("");
                p.setDelinquency_reason(null);
                if (emailId != null && !emailId.equals("")) p.setComm_email("Y"); else p.setComm_email("N");
                p.setComm_instant_msg("Y");
                p.setComm_print("Y");
                p.setEntry_date(entrydate);
                p.setUser_password("abc");
                p.setStatus("A");
                p.setSend_to_address("A");
                p.setCustom("");
                Integer patronCategoryId = null;
                PATRON_CATEGORY pc;
                PATRON_CATEGORY_KEY pck = new PATRON_CATEGORY_KEY();
                PATRON_CATEGORY_MANAGER pcm = new PATRON_CATEGORY_MANAGER();
                if (category != null) {
                    java.util.List l2 = ss.createQuery("from PATRON_CATEGORY as pc where pc.primaryKey.library_id='" + libraryId + "' and pc.patron_category_name='" + category + "'").list();
                    if (l2.size() != 0) {
                        pc = (PATRON_CATEGORY) l2.get(0);
                        patronCategoryId = pc.getPrimaryKey().getPatron_category_id();
                    } else {
                        patronCategoryId = pcm.getMaxPatId(con);
                        try {
                            stmt.executeUpdate("insert into patron_category values(" + patronCategoryId.intValue() + "," + libraryId + ",'" + category + "','','','" + entrydate + "')");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                p.setPatron_category_id(patronCategoryId);
                Integer courseId = null;
                if (course != null) {
                    COURSE_MANAGER cm = new COURSE_MANAGER();
                    java.util.List l3 = ss.createQuery("from COURSE as c where c.primaryKey.library_id='" + libraryId + "' and c.course_name='" + course + "'").list();
                    if (l3.size() != 0) {
                        COURSE c = (COURSE) l3.get(0);
                        courseId = c.getPrimaryKey().getCourse_id();
                    } else {
                        courseId = cm.getMaxCourseId(con);
                        try {
                            stmt.executeUpdate("insert into course values(" + libraryId + "," + courseId.intValue() + ",'" + course + "','','','" + entrydate + "')");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                p.setCourse_id(courseId);
                Integer deptId = null;
                DEPT d;
                DEPT_KEY dk = new DEPT_KEY();
                DEPT_MANAGER dm = new DEPT_MANAGER();
                if (department != null) {
                    java.util.List l1 = ss.createQuery("from DEPT as d where d.primaryKey.library_id='" + libraryId + "' and d.dept_name='" + department + "'").list();
                    if (l1.size() != 0) {
                        d = (DEPT) l1.get(0);
                        deptId = d.getPrimaryKey().getDept_id();
                    } else {
                        deptId = dm.getMaxDeptId(con);
                        try {
                            d = new DEPT();
                            d.setDept_id(deptId);
                            d.setDept_name(department);
                            d.setEntry_date(entrydate);
                            d.setEntry_id(id);
                            d.setHod_id("");
                            d.setLibrary_id(new Integer(libraryId));
                            dk.setDept_id(deptId);
                            dk.setLibrary_id(new Integer(libraryId));
                            d.setPrimaryKey(dk);
                            try {
                                ss1.save(d);
                                tx1.commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                p.setDept_id(deptId);
                try {
                    java.util.List l4 = ss.createQuery("from PATRON as p1 where p1.primaryKey.patron_id='" + id + "'").list();
                    if (l4.size() == 0) {
                        ss.save(p);
                        tx.commit();
                        System.out.println("inserted");
                        ++count1;
                    } else {
                        try {
                            ss1.update(p);
                            tx1.commit();
                            System.out.println("updated");
                            ++count;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            stmt.close();
            con.close();
            ss.close();
            ss1.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        executing = false;
    }
