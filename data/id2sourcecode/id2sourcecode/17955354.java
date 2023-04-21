    @PUT
    @Consumes("application/x-www-form-urlencoded")
    public void putForm(@FormParam("title") String title, @FormParam("description") String description, @FormParam("manager-users") List<String> managerUserURLs, @FormParam("writer-users") List<String> writerUserURLs, @FormParam("reader-users") List<String> readerUserURLs, @FormParam("manager-groups") List<String> managerGroupURLs, @FormParam("writer-groups") List<String> writerGroupURLs, @FormParam("reader-groups") List<String> readerGroupURLs) throws RepositoryException {
        List<Node> users = new ArrayList<Node>();
        if (!getAccessPolicy().canUpdateProject(project.getPath()) || (!getAccessPolicy().canUpdateProjectAccessRights(project.getPath()) && (managerUserURLs != null || writerUserURLs != null || readerUserURLs != null))) {
            throw ExceptionFactory.forbidden();
        }
        if (title == null || title.equals("")) {
            throw ExceptionFactory.missingRequiredFields();
        }
        if (getAccessPolicy().canUpdateProjectAccessRights(project.getPath())) {
            if ((managerUserURLs == null || managerUserURLs.isEmpty()) && (managerGroupURLs == null || managerGroupURLs.isEmpty())) {
                throw ExceptionBuilder.badRequest().error("You must specify at least one user or group as manager.").build();
            }
            NodeIterator managerUsers = ProjectUtils.getUsers(project, "managers").getNodes();
            while (managerUsers.hasNext()) {
                Node user = (Node) managerUsers.next();
                if (!users.contains(user)) {
                    users.add(user);
                }
            }
            NodeIterator writerUsers = ProjectUtils.getUsers(project, "writers").getNodes();
            while (writerUsers.hasNext()) {
                Node user = (Node) writerUsers.next();
                if (!users.contains(user)) {
                    users.add(user);
                }
            }
            ProjectUtils.updateProject(getViewContext(), project, title, description, managerUserURLs, writerUserURLs, readerUserURLs, managerGroupURLs, writerGroupURLs, readerGroupURLs);
        } else {
            ProjectUtils.updateProject(getViewContext(), project, title, description);
        }
        getSession().save();
        String q = "/jcr:root" + project.getPath() + "/experiments/element(*, experiment) order by @lastModified descending";
        @SuppressWarnings("deprecation") Query query = getViewContext().getSession().getWorkspace().getQueryManager().createQuery(q, Query.XPATH);
        NodeIterator experiments = query.execute().getNodes();
        while (experiments.hasNext()) {
            Node experiment = experiments.nextNode();
            UploadUtils.updateCollection(getViewContext(), experiment, users);
        }
        getSession().save();
    }
