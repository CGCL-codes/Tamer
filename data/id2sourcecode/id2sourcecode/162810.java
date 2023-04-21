    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String load_url = "";
        try {
            load_url = request.getParameter("URL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        URL url = new URL(load_url);
        URLConnection connection = url.openConnection();
        connection.connect();
        response.setContentType(connection.getContentType());
        InputStream in = connection.getInputStream();
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        READ: while (true) {
            read = in.read(buffer);
            if (read > 0) {
                out.write(buffer, 0, read);
            } else {
                break READ;
            }
        }
    }
