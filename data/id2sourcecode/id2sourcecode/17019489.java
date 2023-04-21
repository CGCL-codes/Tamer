    public boolean outputDigitalResource(String catid, String libid, javax.servlet.http.HttpServletResponse httpServletResponse, String resource) throws Exception {
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
        httpServletResponse.setContentType(s);
        httpServletResponse.setHeader("Content-Disposition", "inline; filename=" + resource);
        byte digCon[] = new byte[sz];
        fin.read(digCon);
        System.out.println("ouput bytes" + digCon.length);
        try {
            OutputStream out = httpServletResponse.getOutputStream();
            out.write(digCon);
            fin.close();
            out.close();
        } catch (Exception e) {
            System.out.println("exception is " + e.getMessage());
        }
        return true;
    }
