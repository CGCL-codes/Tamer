    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("test.classloaderhandler.servlet.ClassA");
            throw new RuntimeException("NG");
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("test.classloaderhandler.servlet.ClassB");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputStream is = getClass().getResourceAsStream("resourceA.txt");
        resp.getWriter().write(new BufferedReader(new InputStreamReader(is)).readLine());
    }
