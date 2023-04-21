    public void rawService(HttpServletRequest request, HttpServletResponse response, LogSink log, boolean readwrite) throws Exception {
        PrintWriter out = new PrintWriter(response.getWriter());
        String todo = request.getParameter("todo");
        if ("fetch".equals(todo)) {
            DBWrapper.fetch(out, request.getParameter("db"), request.getParameter("document"), request.getParameter("pattern"), request.getParameter("fetch-childs"), request.getParameter("fetch-attributes"), request.getParameter("flags"), log);
        } else if ("archive".equals(todo)) {
            int id = 0;
            String tmp = request.getParameter("id");
            if (tmp != null) {
                id = Integer.parseInt(tmp);
            }
            DBWrapper.fetchArchive(out, request.getParameter("db"), request.getParameter("document"), id, request.getParameter("flags"), log);
        } else if ("write".equals(todo) && readwrite) {
            int pos = 0;
            String[] owner = null;
            if (request.getParameter("owner") != null) {
                owner = new String[1];
                owner[0] = request.getParameter("owner");
            }
            String tmp = request.getParameter("position");
            if (tmp != null) {
                pos = Integer.parseInt(tmp);
            }
            String[] id = DBWrapper.write(request.getParameter("db"), request.getParameter("document"), request.getParameter("xml"), owner, pos, log);
            out.println("<ok plid='" + id[0] + "'/>");
        } else if ("remove".equals(todo) && readwrite) {
            String[] id = null;
            if (request.getParameter("id") != null) {
                id = new String[1];
                id[0] = request.getParameter("id");
            }
            int version = DBWrapper.remove(request.getParameter("db"), request.getParameter("document"), id, log);
            out.println("<ok plversion='" + version + "'/>");
        } else if (todo != null) {
            throw new Exception("illegal request: " + todo + " write=" + readwrite);
        }
    }
