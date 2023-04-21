    public void doGetPlatform(HashMap<String, String> args, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/xml");
        res.setHeader("Content-Language", "en");
        PrintWriter out = res.getWriter();
        String[] paramParts = args.get("param").split("\\.");
        try {
            Connection con = null;
            try {
                con = ConnectionPool.getConnection("metadata");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT viewGroup, station FROM platform_vo_mapping WHERE theme = '" + paramParts[0] + "' AND (platform = '" + paramParts[1] + "' OR platform = '*')");
                if (rs.next()) {
                    String group = rs.getString(1);
                    String station = rs.getString(2);
                    if ("*".equals(station)) station = paramParts[1];
                    String spidrServerName = Settings.get("sites.localSite");
                    String spidrServerUrl = Settings.get("sites." + spidrServerName + ".url");
                    String metadataCollection = Settings.get("viewGroups." + group + ".metadataCollection");
                    String fileUrl = spidrServerUrl + (spidrServerUrl.endsWith("/") ? "" : "/") + "osproxy.do?specialRequest=document&docId=" + metadataCollection + station;
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
