    public void applyTemplate(String templateName, long projectId) {
        List<Person> projectAdmins = em.createNamedQuery("Project.findProjectAdmins").setParameter("projectId", projectId).getResultList();
        clearSecuritySettings(projectId);
        Project project = em.find(Project.class, projectId);
        Action createAction = getActionByName(Action.CREATE);
        Action readAction = getActionByName(Action.READ);
        Action updateAction = getActionByName(Action.UPDATE);
        Action deleteAction = getActionByName(Action.DELETE);
        GroupRole adminGroupRole = new GroupRole();
        adminGroupRole.setGrp(project.getGrp());
        adminGroupRole.setName(PROJECT_ADMIN);
        em.persist(adminGroupRole);
        for (Resource r : getResources()) {
            createRoleGrant(createAction, r, adminGroupRole);
            createRoleGrant(readAction, r, adminGroupRole);
            createRoleGrant(updateAction, r, adminGroupRole);
            createRoleGrant(deleteAction, r, adminGroupRole);
        }
        if (ALL_ADMIN_TEMPLATE.equals(templateName)) {
        } else if (ADMIN_AND_EMPOWERED_MEMBERS_TEMPLATE.equals(templateName)) {
            GroupRole memberGroupRole = new GroupRole();
            memberGroupRole.setGrp(project.getGrp());
            memberGroupRole.setName("Project member");
            em.persist(memberGroupRole);
            for (Resource r : getResources()) {
                createRoleGrant(readAction, r, memberGroupRole);
                if (!Resource.SECURITY_SETTING.equals(r.getName())) {
                    createRoleGrant(createAction, r, memberGroupRole);
                    createRoleGrant(updateAction, r, memberGroupRole);
                    createRoleGrant(deleteAction, r, memberGroupRole);
                }
            }
        } else if (ADMIN_AND_MEMBERS_TEMPLATE.equals(templateName)) {
            createMemberGroupRole(project.getGrp(), "Project member", readAction, createAction, updateAction, deleteAction);
        } else if (BUNCH_OF_ROLES_TEMPLATE.equals(templateName)) {
            createMemberGroupRole(project.getGrp(), "Architect", readAction, createAction, updateAction, deleteAction);
            createMemberGroupRole(project.getGrp(), "Analyst", readAction, createAction, updateAction, deleteAction);
            createMemberGroupRole(project.getGrp(), "Developer", readAction, createAction, updateAction, deleteAction);
            createMemberGroupRole(project.getGrp(), "Tester", readAction, createAction, updateAction, deleteAction);
            createMemberGroupRole(project.getGrp(), "Documentation writer", readAction, createAction, updateAction, deleteAction);
            createMemberGroupRole(project.getGrp(), "Graphics artist", readAction, createAction, updateAction, deleteAction);
        } else if (ADMIN_AND_READ_ONLY_MEMBERS_TEMPLATE.equals(templateName)) {
            GroupRole memberGroupRole = new GroupRole();
            memberGroupRole.setGrp(project.getGrp());
            memberGroupRole.setName("Project member");
            em.persist(memberGroupRole);
            for (Resource r : getResources()) {
                createRoleGrant(readAction, r, memberGroupRole);
            }
        } else {
        }
        for (Person projectAdmin : projectAdmins) {
            PersonRole pr = new PersonRole();
            pr.setRole(adminGroupRole);
            pr.setPerson(projectAdmin);
            em.persist(pr);
        }
        em.flush();
        events.raiseEvent("SecurityChanged");
    }
