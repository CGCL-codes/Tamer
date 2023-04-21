    public void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        final PrintWriter out = res.getWriter();
        String serverAddress = req.getParameter("serverAddress");
        int serverQueryPort = Integer.parseInt(req.getParameter("serverQueryPort"));
        int serverUDPPort = Integer.parseInt(req.getParameter("serverUDPPort"));
        String serverPassword = req.getParameter("serverPassword");
        String webLoginname = req.getParameter("webLoginname");
        String pictureBase = req.getParameter("pictureBase");
        ts.setInfo(serverAddress, serverQueryPort, serverUDPPort, serverPassword, webLoginname, pictureBase);
        ts.getInfo();
        boolean isMistake = false;
        if ((ts.getChannel() == null) && (ts.getUser() == null)) {
            isMistake = true;
        }
        out.println("<div id=\"teamspeak\">");
        out.println("	<div id=\"ts2MainTable\">");
        out.println("		<div id=\"ts2Picture\"></div>");
        out.println("		<div id=\"ts2Title\">&nbsp;Teamspeak 2 Server</div>");
        if (!isMistake) {
            final String result2 = ts.getSubChannel(-1, 0);
            out.println(result2);
        } else {
            out.println("		<div id=\"ts2Offline\">Offline</div>");
        }
        out.println("	</div>");
        out.println("</div>");
        out.flush();
    }
