    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            String sid = httpServletRequest.getParameter("sid");
            if (sid == null) {
                sid = "default";
            }
            System.out.println("sid: " + sid);
            Long users_id = Sessionmanagement.getInstance().checkSession(sid);
            long user_level = Usermanagement.getInstance().getUserLevelByID(users_id);
            if (user_level > 0) {
                String room = httpServletRequest.getParameter("room");
                if (room == null) {
                    room = "default";
                }
                String domain = httpServletRequest.getParameter("domain");
                if (domain == null) domain = "default";
                String filename = httpServletRequest.getParameter("fileName");
                if (filename == null) filename = "default";
                String roomName = domain + "_" + room;
                roomName = StringUtils.deleteWhitespace(roomName);
                String current_dir = getServletContext().getRealPath("/");
                System.out.println("Current_dir: " + current_dir);
                String working_dir = current_dir + "desktop" + File.separatorChar + roomName + File.separatorChar;
                String full_path = working_dir + filename;
                System.out.println("full_path: " + full_path);
                RandomAccessFile rf = new RandomAccessFile(full_path, "r");
                httpServletResponse.reset();
                httpServletResponse.resetBuffer();
                OutputStream out = httpServletResponse.getOutputStream();
                httpServletResponse.setContentType("APPLICATION/OCTET-STREAM");
                httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                httpServletResponse.setHeader("Content-Length", "" + rf.length());
                httpServletResponse.setHeader("Cache-Control", "no-cache");
                httpServletResponse.setHeader("Pragma", "no-cache");
                byte[] buffer = new byte[1024];
                int readed = -1;
                while ((readed = rf.read(buffer, 0, buffer.length)) > -1) {
                    out.write(buffer, 0, readed);
                }
                rf.close();
                out.flush();
                out.close();
            }
        } catch (Exception er) {
            System.out.println("Error downloading: " + er);
        }
    }
