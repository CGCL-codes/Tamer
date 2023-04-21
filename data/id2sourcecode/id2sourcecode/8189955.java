    protected void reportServiceInfo(HttpServletResponse response, PrintWriter writer, SOAPService service, String serviceName) {
        response.setContentType("text/html");
        ServletContext sctx = getServletConfig().getServletContext();
        if (sctx != null) try {
            String index = sctx.getInitParameter("index." + serviceName);
            if (index == null) index = "index.html";
            index = sctx.getRealPath(index);
            File file = new File(index);
            if (file.exists()) {
                String s;
                DataInputStream ds = new DataInputStream(new FileInputStream(file));
                while ((s = ds.readLine()) != null) writer.println(s);
                ds.close();
            }
        } catch (Exception e) {
        }
        writer.println("<h1>" + service.getName() + "</h1>");
        writer.println("<p>" + Messages.getMessage("axisService00") + "</p>");
        writer.println("<i>" + Messages.getMessage("perhaps00") + "</i>");
        writer.println("<br> service name: " + serviceName);
    }
