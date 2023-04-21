    public void run() {
        try {
            final String url = ((request.getPath().getPath().trim().length() <= 1) ? "/index.html" : (request.getPath().getPath()));
            if (new File(config.getProperty("INSTALL_PATH") + "/webapps" + url).exists()) {
                if (request.getPath().getPath().contains("img/")) {
                    sendPage(url);
                } else if (request.getSession().get("name") != null) {
                    sendPage(url);
                } else {
                    request.getSession().clear();
                    sendPage("/index.html");
                }
            } else {
                if (InternalPage.contains(request.getPath().getName().trim())) {
                    java.sql.Connection con;
                    ResultSet rs;
                    Statement stmt;
                    PreparedStatement pstmt;
                    String qry = "";
                    switch(InternalPage.getPage(request.getPath().getName().trim())) {
                        case 1:
                            con = connectionPool.getConnection("config");
                            qry = "select admin,password from admin where admin='" + request.getForm().get("username") + "' and password='" + request.getForm().get("password") + "'";
                            stmt = con.createStatement();
                            rs = stmt.executeQuery(qry);
                            if (rs.next()) {
                                if (rs.getString(1).equals(request.getForm().get("username")) && rs.getString(2).equals(request.getForm().get("password"))) ;
                                {
                                    request.getSession(true).put("name", rs.getString(1));
                                    sendPage("/home.html");
                                }
                            } else if (request.getSession().get("name") != null) {
                                sendPage("/home.html");
                            } else {
                                sendPage("/index.html");
                            }
                            con.close();
                            break;
                        case 2:
                            if (request.getParameter("operation").equals("start")) {
                                qry = "SELECT DISTINCT SOURCEPATH,BACKUPPATH,TEMPLATE,PREPROCESSFREQUENCY FROM SERVICE WHERE SERVICENAME='" + request.getParameter("service") + "'";
                                con = connectionPool.getConnection("config");
                                stmt = con.createStatement();
                                rs = stmt.executeQuery(qry);
                                if (rs.next()) {
                                    String sourcePath = rs.getString(1);
                                    String backupPath = rs.getString(2);
                                    String template = rs.getString(3);
                                    int preFreq = rs.getInt(4);
                                    con.close();
                                    if (!new File(sourcePath).exists()) {
                                        throw new DoxenException(1001, sourcePath);
                                    }
                                    if (!new File(config.getProperty("INSTALL_PATH") + "/data/preprocess/" + request.getParameter("service")).exists()) {
                                        new File(config.getProperty("INSTALL_PATH") + "/data/preprocess/" + request.getParameter("service")).mkdirs();
                                    }
                                    URLClassLoader loader = URLClassLoader.newInstance(new URL[] { new URL("file:/" + config.getProperty("INSTALL_PATH") + "/db/") });
                                    PreprocessBase preprocessBase = (PreprocessBase) loader.loadClass(template.substring(0, template.indexOf("."))).getConstructor().newInstance();
                                    PreprocessRunnable run = new PreprocessRunnable(sourcePath, request.getParameter("service"), preprocessThreadStore, preprocessStatus, config, connectionPool, preprocessBase);
                                    if (!preprocessThreadStore.containsKey(request.getParameter("service"))) {
                                        preprocessThreadStore.put(request.getParameter("service") + "-Run", run);
                                        preprocessThreadStore.put(request.getParameter("service"), schedulerPool.scheduleWithFixedDelay(run, 0, preFreq, TimeUnit.SECONDS));
                                    }
                                } else {
                                    throw new DoxenException(1000, request.getParameter("service"));
                                }
                            } else if (request.getParameter("operation").equals("stop")) {
                                ((PreprocessRunnable) preprocessThreadStore.get(request.getParameter("service") + "-Run")).cancel();
                                ((ScheduledFuture) preprocessThreadStore.get(request.getParameter("service"))).cancel(true);
                                System.out.println(new Date() + ": Preprocess stopped for the service " + request.getParameter("service"));
                                preprocessThreadStore.remove(request.getParameter("service"));
                                preprocessStatus.remove(request.getParameter("service"));
                            }
                            break;
                        case 3:
                            break;
                        case 4:
                            if (request.getParameter("action").equals("preprocess")) {
                                if (preprocessStatus.containsKey(request.getParameter("service"))) {
                                    print.print("1," + (Date) preprocessStatus.get(request.getParameter("service")));
                                } else {
                                    print.print("0,--");
                                }
                            } else if (request.getParameter("action").equals("dispatch")) {
                                if (dispatchStatus.containsKey(request.getParameter("service"))) {
                                    print.print("1," + (Date) dispatchStatus.get(request.getParameter("service")));
                                } else {
                                    print.print("0,--");
                                }
                            }
                            break;
                        case 5:
                            String serviceName = "";
                            con = connectionPool.getConnection("config");
                            qry = "select distinct id,SERVICENAME from service order by id";
                            stmt = con.createStatement();
                            rs = stmt.executeQuery(qry);
                            while (rs.next()) {
                                serviceName += rs.getInt(1) + "," + rs.getString(2) + "#";
                            }
                            print.print(serviceName);
                            con.close();
                            break;
                        case 6:
                            con = connectionPool.getConnection("config");
                            qry = "insert into service ( id , servicename , sourcepath , backuppath , template , preprocessfrequency , dispatchfrequency ) values((select case when max(id) is null then 1 else max(id)+1 end as id from service),?,?,?,?,?,? )";
                            pstmt = con.prepareStatement(qry);
                            pstmt.setString(1, request.getForm().get("serviceName"));
                            pstmt.setString(2, request.getForm().get("sourcePath"));
                            pstmt.setString(3, request.getForm().get("backupPath"));
                            pstmt.setString(4, request.getPart("templateFile").getFileName());
                            pstmt.setInt(5, Integer.parseInt(request.getForm().get("preprocessFreq")));
                            pstmt.setInt(6, Integer.parseInt(request.getForm().get("dispatchFreq")));
                            pstmt.execute();
                            con.close();
                            InputStream reader = request.getPart("templateFile").getInputStream();
                            FileOutputStream writer = new FileOutputStream(config.getProperty("INSTALL_PATH") + "/db/" + request.getPart("templateFile").getFileName());
                            byte[] readBuffer = new byte[1024];
                            int read;
                            while ((read = reader.read(readBuffer)) != -1) {
                                writer.write(readBuffer, 0, read);
                            }
                            reader.close();
                            writer.close();
                            Class.forName("org.h2.Driver");
                            Connection conn = DriverManager.getConnection("jdbc:h2:" + config.getProperty("INSTALL_PATH") + "/db/" + request.getForm().get("serviceName"), "sa", "");
                            qry = "create table if not exists preprocess(ID integer PRIMARY KEY AUTO_INCREMENT,docid integer NOT NULL,userid varchar(32),username varchar(32),tomailid varchar(255),ccmailid varchar(255),outputfilename varchar(255),frommailid varchar(255),subject varchar(255),creationdate timestamp,unixdate int not null)";
                            Statement stmt1 = conn.createStatement();
                            stmt1.execute(qry);
                            conn.close();
                            connectionPool.addConnection(request.getForm().get("serviceName"));
                            connectionPool.getConnection(request.getForm().get("serviceName")).close();
                            sendPage("/service.html");
                            break;
                        default:
                            break;
                    }
                } else {
                    response.setCode(Status.NOT_FOUND.getCode());
                    sendPage("/errorpages/pagenotfound.html");
                }
            }
        } catch (DoxenException e) {
            switch(e.getErrorCode()) {
                case 1000:
                    System.out.println(new Date() + ":Service configuration is missing for service " + (String) e.getExtraParam());
                    break;
                case 1001:
                    System.out.println(new Date() + ":Source directory " + (String) e.getExtraParam() + " is not present");
                    break;
                default:
                    break;
            }
        } catch (java.io.IOException e) {
        } catch (Exception e) {
            try {
                response.setCode(Status.INTERNAL_SERVER_ERROR.getCode());
                System.out.println("RequestHandler.java:" + e);
                e.printStackTrace();
            } catch (Exception ex) {
            }
        } finally {
            try {
                out.close();
                print.close();
                response.close();
            } catch (Exception ex) {
            }
        }
    }
