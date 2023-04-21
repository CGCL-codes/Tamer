    public void render(ServiceContext serviceContext) throws Exception {
        byte[] buffer = new byte[1024 * 64];
        if (serviceContext.getTemplateName() == null) throw new Exception("no Template defined for service: " + serviceContext.getServiceInfo().getRefName());
        File f = new File(serviceContext.getTemplateName());
        serviceContext.getResponse().setContentLength((int) f.length());
        InputStream is = new FileInputStream(f);
        for (int n = 0; -1 != (n = is.read(buffer)); ) serviceContext.getResponse().getOutputStream().write(buffer, 0, n);
        is.close();
    }
