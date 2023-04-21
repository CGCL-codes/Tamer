    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (!VPHelpers.validate(req, res, this)) {
            return;
        }
        VPUser user = UserAuthentication.getUser(req);
        req.setAttribute("VPUser", user);
        StudentSession sess = (StudentSession) req.getSession().getAttribute("StudentSession");
        String placement = sess.getPlacement().getName();
        String tutor = sess.getTutor();
        String phase = sess.getPhase();
        String file = (String) req.getParameter("file");
        String forward = "../show-popup.jsp";
        String phaseVal = "";
        if (phase == null) {
            phase = "";
        }
        if (!phase.equals("")) {
            phaseVal = phase + "/";
        }
        if (file == null) {
            file = "";
        }
        if (file.equals("")) {
            file = "index.html";
        }
        String data = "";
        File indexFile = new File(settings.getString("vp.datadir") + "/placements/" + placement + "/" + phaseVal + file);
        if (!indexFile.exists()) {
            VPHelpers.error(req, res, "Placement file '" + placement + "/" + phaseVal + file + "' does not exist");
            return;
        }
        if (!indexFile.isFile() || !indexFile.canRead()) {
            VPHelpers.error(req, res, "Placement file '" + placement + "/" + phaseVal + file + "' cannot be read");
            return;
        }
        if (!file.toLowerCase().endsWith("html") && !file.toLowerCase().endsWith("htm")) {
            streamFile(req, res, indexFile);
            return;
        }
        FileReader dataSource = new FileReader(indexFile);
        StringWriter dataBuff = new StringWriter();
        try {
            while (dataSource.ready()) dataBuff.write(dataSource.read());
        } catch (IOException ioe) {
            VPHelpers.error(req, res, "Error reading file '" + placement + phaseVal + file + "'");
            return;
        }
        data = dataBuff.toString();
        data = resolveLinks(data, placement, tutor, phase);
        data = resolveImages(data, placement, tutor, phase);
        req.setAttribute("data", data);
        RequestDispatcher rd = req.getRequestDispatcher(forward);
        try {
            rd.forward(req, res);
        } catch (ServletException e) {
            VPHelpers.error(req, res, "Error forwarding to: " + forward + ".");
            return;
        }
    }
