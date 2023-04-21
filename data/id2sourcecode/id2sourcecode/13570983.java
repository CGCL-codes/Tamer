    public static Node updateProject(ViewContext context, Node project, String title, String description, List<String> managerUserURLs, List<String> writerUserURLs, List<String> readerUserURLs, List<String> managerGroupURLs, List<String> writerGroupURLs, List<String> readerGroupURLs) throws RepositoryException {
        updateProject(context, project, title, description);
        String[] managerUsers = NodeUtils.buildUUIDArray(context, managerUserURLs);
        if ((managerUsers.length == 0) && !project.getSession().getUserID().equals("admin")) {
            Node currentUser = (Node) project.getSession().getItem("/users/" + project.getSession().getUserID());
            managerUsers = new String[] { currentUser.getIdentifier() };
        }
        String[] writerUsers = NodeUtils.buildUUIDArray(context, writerUserURLs, managerUsers);
        String[] readerUsers = NodeUtils.buildUUIDArray(context, readerUserURLs, managerUsers, writerUsers);
        String[] managerGroups = NodeUtils.buildUUIDArray(context, managerGroupURLs);
        String[] writerGroups = NodeUtils.buildUUIDArray(context, writerGroupURLs, managerGroups);
        String[] readerGroups = NodeUtils.buildUUIDArray(context, readerGroupURLs, managerGroups, writerGroups);
        project.getNode("managers").setProperty("users", managerUsers);
        project.getNode("managers").setProperty("groups", managerGroups);
        project.getNode("writers").setProperty("users", writerUsers);
        project.getNode("writers").setProperty("groups", writerGroups);
        project.getNode("readers").setProperty("users", readerUsers);
        project.getNode("readers").setProperty("groups", readerGroups);
        return project;
    }
