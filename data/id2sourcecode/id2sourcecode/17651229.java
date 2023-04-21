    private PagedResult getPagedProjects(int offset, int limit) throws RepositoryException {
        return new DefaultPagedResult(getSession(), "/jcr:root/projects/element(*, project)[\n" + "  managers/@users = '" + user.getIdentifier().replaceAll("'", "''") + "'" + " or\n" + "  writers/@users = '" + user.getIdentifier().replaceAll("'", "''") + "'" + " or\n" + "  readers/@users = '" + user.getIdentifier().replaceAll("'", "''") + "'\n" + "]\n" + "order by @lastModified descending", offset, limit);
    }
