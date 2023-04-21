    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int songid = Integer.parseInt(request.getParameter("songid"));
        System.out.println(songid);
        DAO dao = new DAO();
        BufferedInputStream inp = new BufferedInputStream(dao.downloadSongResource(songid));
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        int read;
        byte[] array = new byte[1024];
        while ((read = inp.read(array)) != -1) {
            out.write(array, 0, read);
        }
        out.close();
        inp.close();
        dao.close();
    }
