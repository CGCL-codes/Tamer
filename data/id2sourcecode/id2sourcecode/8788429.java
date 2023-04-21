    public int doStartTag() throws JspException {
        try {
            Properties projectProperties = new Properties();
            try {
                if (applicationProperties != null) projectProperties.load(new FileInputStream(applicationProperties.toString()));
            } catch (IOException e) {
                System.err.println("IGNORED " + e);
            }
            JspWriter out = pageContext.getOut();
            out.println("<style type=\"text/css\"> h1,h2,h3,h4 {color: blue; margin-bottom:0px}</style>");
            out.println("<h1>Makumba System Information (&lt;mak:info /&gt;)</h1>");
            final String startupProp = "startupTime";
            DateFormat df = new SimpleDateFormat("dd MMM yyyy HH:mm");
            if (System.getProperty(startupProp) == null) {
                System.setProperty(startupProp, df.format(MakumbaSystem.loadingTime));
            }
            Runtime rt = java.lang.Runtime.getRuntime();
            long maxHeap = rt.maxMemory();
            String initialHeap = System.getProperty("tomcat.jvm.initial_memory");
            if (initialHeap == null) {
                initialHeap = projectProperties.getProperty("tomcat.jvm.initial_memory");
                if (initialHeap == null) {
                    initialHeap = "N/A";
                }
            }
            String username = System.getProperty("tomcat.manager.user");
            if (username == null) {
                username = projectProperties.getProperty("tomcat.manager.user");
            }
            String password = System.getProperty("tomcat.manager.pass");
            if (password == null) {
                password = projectProperties.getProperty("tomcat.manager.pass");
                if (password != null) {
                    projectProperties.remove("tomcat.manager.pass");
                }
            }
            String hotspot = System.getProperty("tomcat.jvm.hotspot");
            if (hotspot == null) {
                hotspot = projectProperties.getProperty("tomcat.jvm.hotspot");
                if (hotspot == null) {
                    hotspot = "N/A";
                }
            }
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            HttpSession session = request.getSession(true);
            int port = request.getServerPort();
            String activeSessions = null;
            String activeSessionsTitle = null;
            String serverInfo = session.getServletContext().getServerInfo();
            if (serverInfo.indexOf("Tomcat") != -1) {
                if (username != null && password != null) {
                    try {
                        HttpURLConnection uc = (HttpURLConnection) (new URL("http://localhost:" + port + "/manager/list")).openConnection();
                        uc.setRequestProperty("connection", "close");
                        uc.setRequestProperty("Authorization", "Basic " + Base64.encode((username + ":" + password).getBytes()));
                        uc.setUseCaches(false);
                        uc.connect();
                        if (uc.getResponseCode() != 200) throw new RuntimeException(uc.getResponseMessage());
                        if (uc.getContentLength() == 0) throw new RuntimeException("content zero");
                        StringWriter sw = new StringWriter();
                        InputStreamReader ir = new InputStreamReader(uc.getInputStream());
                        char[] buf = new char[1024];
                        int n;
                        while ((n = ir.read(buf)) != -1) sw.write(buf, 0, n);
                        String list = sw.toString();
                        String marker = request.getContextPath();
                        if (marker.length() == 0) marker = "/";
                        marker = marker + ":running:";
                        int found = list.indexOf(marker) + marker.length();
                        if (found == -1) throw new RuntimeException("context not found");
                        activeSessions = list.substring(found, list.indexOf(":", found + 1));
                    } catch (Throwable t) {
                        out.println(" <p>could connect to /manager/list: " + t.getMessage() + " </p>");
                    }
                } else {
                    activeSessionsTitle = "Could not authenticate: 'applicationProperties' attribute must specify 'tomcat.manager.user' and 'tomcat.manager.pass' entries!";
                }
            } else {
                activeSessionsTitle = "This feature is currentely only supported for Apache Tomcat!";
            }
            if (activeSessions == null) {
                activeSessions = "<span title=\"" + activeSessionsTitle + "\">Unknwon</span>";
            }
            out.println("<table border=\"0\" bgcolor=\"white\" cellspacing=\"3\" cellpadding=\"3\" >");
            out.println("  <tr>");
            out.println("    <td valign=\"top\" nowrap=\"nowrap\"><font size=\"+1\"><b>Client:</td>");
            out.println("    <td>");
            out.println("      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
            printSystemInfoRow(out, "Browser", request.getHeader("User-Agent"));
            printSystemInfoRow(out, "Host (IP)", request.getRemoteHost() + "(" + request.getRemoteAddr() + ")");
            out.println("      </table>");
            out.println("    </td>");
            out.println("  </tr>");
            out.println("  <tr>");
            out.println("    <td valign=\"top\"><font size=\"+1\"><b>Server:</b></font> JVM mode: <b>" + hotspot);
            out.println("    <td>");
            out.println("      <table border=\"0\" cellspacing=\"3\" cellpadding=\"3\">");
            String serverUpSince = ReadableFormatter.readableAge((new Date()).getTime() - new Date(System.getProperty(startupProp)).getTime());
            String serverURL = request.getScheme() + "://" + request.getServerName() + ":" + port;
            line = 0;
            printSystemInfoRow(out, "Initial heap (-Xms)", initialHeap);
            printSystemInfoRow(out, "Max heap (-Xmx)", ReadableFormatter.readableBytes(maxHeap));
            printSystemInfoRow(out, "Current heap size", ReadableFormatter.readableBytes(rt.totalMemory()));
            printSystemInfoRow(out, "Heap in use", ReadableFormatter.readableBytes(rt.totalMemory() - rt.freeMemory()));
            printSystemInfoRow(out, "Free heap", ReadableFormatter.readableBytes(rt.freeMemory()));
            printSystemInfoRow(out, "Server is up since", System.getProperty(startupProp) + "</b>(" + serverUpSince + " ago)");
            printSystemInfoRow(out, "Last application (re)load", df.format(MakumbaSystem.loadingTime) + "(" + ReadableFormatter.readableAge((new Date()).getTime() - MakumbaSystem.loadingTime.getTime()) + " ago)");
            printSystemInfoRow(out, "Server time", df.format(new Date()));
            printSystemInfoRow(out, "Active sessions", activeSessions);
            printSystemInfoRow(out, "Server protocol, name and port", "<a href=\"" + serverURL + "\">" + serverURL + "</a>");
            printSystemInfoRow(out, "Server software", pageContext.getServletContext().getServerInfo());
            out.println("      </table>");
            out.println("    </td>");
            out.println("  </tr>");
            out.println("</table>");
            if (applicationProperties != null) {
                out.println("<h2>Application specific properties <span style=\"font-size:smaller\">(" + applicationProperties + ")</span> </h2>");
                try {
                    projectProperties.load(new FileInputStream(applicationProperties.toString()));
                    printProperties(out, projectProperties);
                } catch (IOException io) {
                    out.println("<p style=\"color: red;\">Could not find application specific properties file <i>'" + applicationProperties + "'</i> in the current directory </p>");
                }
            }
            out.println();
            out.println("<h2><a href=\"/makumba-docs/\"> Makumba</a></h2>");
            out.println("<table border=\"0\" cellspacing=\"3\" cellpadding=\"3\">");
            out.println("  <tr bgcolor=\"#cccccc\"> <th>Property</th> <th>Value</th> </tr>");
            String dbname = TransactionProvider.getInstance().getDefaultDataSourceName();
            line = 0;
            printMakumbaPropertyRow(out, "<a href=\"/makumba-docs/CHANGELOG.txt\">version</a>", MakumbaSystem.getVersion());
            printMakumbaPropertyRow(out, "Default database name", dbname);
            printMakumbaPropertyRow(out, "DBSV", MakumbaSystem.getDatabaseProperty(dbname, "dbsv"));
            ;
            printMakumbaPropertyRow(out, "Number of connections open", MakumbaSystem.getDatabaseProperty(dbname, "jdbc_connections"));
            printMakumbaPropertyRow(out, "SQL engine and version", MakumbaSystem.getDatabaseProperty(dbname, "sql_engine.name"));
            printMakumbaPropertyRow(out, "JDBC driver and version", MakumbaSystem.getDatabaseProperty(dbname, "jdbc_driver.name") + " " + MakumbaSystem.getDatabaseProperty(dbname, "jdbc_driver.version"));
            out.println("</table>");
            out.println("<h3>Makumba caches: </h3>");
            out.println("<table border=\"0\" cellspacing=\"5\" cellpadding=\"3\">");
            out.println("  <tr> <th>Name</th> <th>size</th> <th>hits</th> <th>misses</th> </tr>");
            line = 0;
            Map<String, int[]> m = MakumbaSystem.getCacheInfo();
            TreeSet<String> treeSet = new TreeSet<String>(m.keySet());
            for (Iterator<String> iterator = treeSet.iterator(); iterator.hasNext(); ) {
                String nm = (String) iterator.next();
                Object o = m.get(nm);
                out.println("  <tr bgcolor=\"#" + ((line++ % 2 == 0) ? "eeeeee" : "ffffff") + "\">");
                out.println("    <td>" + nm + "</td");
                if (o instanceof int[]) {
                    int[] intArray = (int[]) o;
                    for (int i = 0; i < intArray.length; i++) {
                        out.println("    <td align=right><code>" + intArray[i] + "</code></td>");
                    }
                } else {
                    out.println("    <td align=right><code>" + o + "</code></td>");
                }
                out.println("  </tr>");
            }
            out.println("</table>");
            out.println("<h2>User Session</h2>");
            out.print("Created: " + ReadableFormatter.readableAge((new Date()).getTime() - session.getCreationTime()) + " ago ");
            out.println("(" + session.getCreationTime() + ")<br/>");
            out.print("Last Accessed: " + ReadableFormatter.readableAge((new Date()).getTime() - session.getLastAccessedTime()) + " ago ");
            out.println("(" + session.getLastAccessedTime() + ")<br>");
            out.print("Max inactive interval:" + ReadableFormatter.readableAge(session.getMaxInactiveInterval() * 1000));
            out.println("(" + session.getMaxInactiveInterval() + ")<br>");
            Enumeration attribs = session.getAttributeNames();
            out.println("<table border=\"0\" cellspacing=\"3\" cellpadding=\"3\">");
            out.println("  <tr bgcolor=\"#cccccc\"> <th>Attribute</th> <th>Value</th> <th>Class</th> </tr>");
            line = 0;
            while (attribs.hasMoreElements()) {
                String key = (String) attribs.nextElement();
                out.println("  <tr bgcolor=\"#" + ((line++ % 2 == 0) ? "eeeeee" : "ffffff") + "\">");
                out.println("    <td valign=\"top\">" + key + ":</td>");
                out.println("    <td><pre>" + session.getAttribute(key) + "</pre></td>");
                out.println("    <td>" + session.getAttribute(key).getClass().getName() + "</td>");
                out.println("  </tr>");
            }
            out.println("</table>");
            out.println("<h2>Java Virtual Machine properties</h2>");
            printProperties(out, sysprops);
        } catch (java.io.IOException e) {
            throw new JspException(e.getMessage());
        }
        return EVAL_BODY_INCLUDE;
    }
