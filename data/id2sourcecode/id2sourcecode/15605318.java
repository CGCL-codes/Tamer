    public void service(CmsObject cms, CmsResource resource, ServletRequest req, ServletResponse res) throws CmsException, IOException {
        res.getOutputStream().write(cms.readFile(resource).getContents());
    }
