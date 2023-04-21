    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(FenixWebConfig.getFenixDir());
        ServletContext servletContext = this.getServletConfig().getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        System.out.println(request.getQueryString());
        String szUrl = request.getParameter("url");
        System.out.println(szUrl);
        URL url;
        File domains = new File("");
        try {
            url = new URL(szUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.getContent();
            ServletOutputStream sout = response.getOutputStream();
            InputStream is = con.getInputStream();
            byte buff[] = new byte[1024];
            int r;
            while ((r = is.read(buff)) != -1) {
                sout.write(buff, 0, r);
            }
            sout.flush();
            is.close();
            sout.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
