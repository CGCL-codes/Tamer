    public void doGetParam(HashMap<String, String> args, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/xml");
        res.setHeader("Content-Language", "en");
        PrintWriter out = res.getWriter();
        String param = args.get("param");
        try {
            Connection con = null;
            try {
                con = ConnectionPool.getConnection("metadata");
                String spidrServerName = Settings.get("sites.localSite");
                String spidrServerUrl = Settings.get("sites." + spidrServerName + ".url");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT docname FROM param_vo_mapping WHERE param = '" + param + "'");
                if (rs.next()) {
                    String docname = rs.getString(1);
                    String fileUrl = spidrServerUrl + (spidrServerUrl.endsWith("/") ? "" : "/") + "osproxy.do?specialRequest=document&docId=" + docname;
                    URL url = new URL(fileUrl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) out.println(inputLine);
                    in.close();
                }
            } finally {
                ConnectionPool.releaseConnection(con);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
