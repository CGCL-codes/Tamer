    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String resource = "";
        String catid = request.getParameter("CatId");
        String libid = request.getParameter("LibId");
        if (request.getParameter("RESOURCE") != null) {
            resource = request.getParameter("RESOURCE");
        }
        FileInputStream fin = new FileInputStream(ejb.bprocess.util.NewGenLibRoot.getAttachmentsPath() + java.io.File.separator + "CatalogueRecords" + java.io.File.separator + "CAT_" + catid + "_" + libid + java.io.File.separator + resource);
        java.nio.channels.FileChannel fC = fin.getChannel();
        int sz = (int) fC.size();
        System.out.println("file name " + resource);
        java.util.Properties prop = System.getProperties();
        prop.load(new FileInputStream(ejb.bprocess.util.NewGenLibRoot.getRoot() + java.io.File.separator + "SystemFiles" + java.io.File.separator + "ContentTypes.properties"));
        String typeOfFile = resource.substring(resource.lastIndexOf('.') + 1);
        typeOfFile = typeOfFile.toLowerCase();
        System.out.println("type of file   " + typeOfFile.trim());
        String s = prop.getProperty(typeOfFile.trim());
        System.out.println("content type" + s);
        response.setContentType(s);
        response.setHeader("Content-Disposition", "inline; filename=" + resource);
        byte digCon[] = new byte[sz];
        fin.read(digCon);
        System.out.println("ouput bytes" + digCon.length);
        try {
            OutputStream out = response.getOutputStream();
            out.write(digCon);
            fin.close();
            out.close();
        } catch (Exception e) {
            System.out.println("exception is " + e.getMessage());
        }
        response.setContentType("text/html;charset=UTF-8");
    }
