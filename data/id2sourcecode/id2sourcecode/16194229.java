    @POST
    @Consumes("application/x-www-form-urlencoded")
    public Response postForm(@HeaderParam("Slug") String name, @FormParam("title") String title, @FormParam("description") String description, @FormParam("manager-users") List<String> managerUserURLs, @FormParam("writer-users") List<String> writerUserURLs, @FormParam("reader-users") List<String> readerUserURLs, @FormParam("manager-groups") List<String> managerGroupURLs, @FormParam("writer-groups") List<String> writerGroupURLs, @FormParam("reader-groups") List<String> readerGroupURLs) throws RepositoryException {
        if (name != null && !getAccessPolicy().canCreateProject(parent.getPath() + "/" + name)) {
            throw ExceptionFactory.forbidden();
        }
        if (name.startsWith("filemonitor-")) {
            throw ExceptionBuilder.conflict().error("Projects cannot start with 'filemonitor-'.").build();
        }
        if (name == null || name.equals("") || title == null || title.equals("")) {
            throw ExceptionFactory.missingRequiredFields();
        }
        if (!URIUtils.isValidSlug(name)) {
            throw ExceptionFactory.invalidSlug(name);
        }
        if (parent.hasNode(name)) {
            throw ExceptionFactory.itemExists(parent.getPath() + "/" + name);
        }
        if ((managerUserURLs == null || managerUserURLs.isEmpty()) && (managerGroupURLs == null || managerGroupURLs.isEmpty())) {
            throw ExceptionBuilder.badRequest().error("You must specify at least one user or group as manager.").build();
        }
        Node project = ProjectUtils.createProject(getViewContext(), parent, name, title, description, managerUserURLs, writerUserURLs, readerUserURLs, managerGroupURLs, writerGroupURLs, readerGroupURLs);
        getSession().save();
        return Response.created(URI.create(getViewContext().getAppURL(project.getPath()))).build();
    }
