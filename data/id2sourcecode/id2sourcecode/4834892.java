    @Override
    public void migrateContent() throws Exception {
        session.getRootNode().addNode("groups", "groups");
        String q = "/jcr:root/projects/element(*, project)";
        @SuppressWarnings("deprecation") Query query = session.getWorkspace().getQueryManager().createQuery(q, Query.XPATH);
        NodeIterator projects = query.execute().getNodes();
        while (projects.hasNext()) {
            Node project = projects.nextNode();
            String[] roles = { "managers", "writers", "readers" };
            for (String role : roles) {
                Property property = project.getProperty(role);
                Node node = project.addNode(role, "accessors");
                node.setProperty("users", property.getValues());
                node.setProperty("groups", new Value[] {});
                property.remove();
            }
        }
    }
