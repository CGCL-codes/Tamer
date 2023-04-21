    public Blog createBlog(String name, List<Role> readRoles, List<Role> writeRoles, List<Role> commentRoles) {
        Group group = mgblGroup();
        Blog blog = new Blog();
        blog.setName(name);
        blog.setGroup(group);
        blog.setCode("blogcode" + (new Date().getTime()));
        PermissionManager permissionManager = new PermissionManager(locale, session);
        blog.setAccessControlledClass(permissionManager.getAccessControlledClass("BLOG"));
        blog.setCreatedAt(new Date());
        session.save(blog);
        for (Role readRole : readRoles) {
            permissionManager.setRolePermission(blog, readRole, PermissionManager.READ_PERMISSION);
        }
        for (Role writeRole : writeRoles) {
            permissionManager.setRolePermission(blog, writeRole, PermissionManager.WRITE_PERMISSION);
        }
        for (Role commentRole : commentRoles) {
            permissionManager.setRolePermission(blog, commentRole, PermissionManager.ADD_COMMENT_PERMISSION);
        }
        return blog;
    }
