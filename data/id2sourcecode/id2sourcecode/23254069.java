    public void loadXml(String xml, CMessageWriter writer) {
        CXmlDataReader reader = new CXmlDataReader(this.resourceService, this.genomeService, this.tagService, this.userService, writer);
        try {
            reader.loadXml(xml);
        } catch (Exception e) {
            CFileHelper.writeFile("c:/setup.xml", xml);
            throw new CVardbException(e);
        }
    }
