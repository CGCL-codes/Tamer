    public static Node createProject(ViewContext context, Node parent, String name, String title, String description, List<String> managerUserURLs, List<String> writerUserURLs, List<String> readerUserURLs, List<String> managerGroupURLs, List<String> writerGroupURLs, List<String> readerGroupURLs) throws RepositoryException {
        Node project = parent.addNode(name, "project");
        project.setProperty("published", false);
        project.addNode("managers", "accessors");
        project.addNode("writers", "accessors");
        project.addNode("readers", "accessors");
        project.addNode("experiments", "experiments");
        return updateProject(context, project, title, description, managerUserURLs, writerUserURLs, readerUserURLs, managerGroupURLs, writerGroupURLs, readerGroupURLs);
    }
