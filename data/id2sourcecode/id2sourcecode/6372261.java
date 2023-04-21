    protected void internalCreateDefaultGroups(CmsDbContext dbc, String ouFqn, String ouDescription, boolean webuser) throws CmsException {
        String rootAdminRole = CmsRole.ROOT_ADMIN.getGroupName();
        try {
            if ((CmsOrganizationalUnit.getParentFqn(ouFqn) != null) || ((CmsOrganizationalUnit.getParentFqn(ouFqn) == null) && !existsGroup(dbc, rootAdminRole))) {
                Iterator itRoles = CmsRole.getSystemRoles().iterator();
                while (itRoles.hasNext()) {
                    CmsRole role = (CmsRole) itRoles.next();
                    if (webuser && (role != CmsRole.ACCOUNT_MANAGER)) {
                        continue;
                    }
                    if (role.isOrganizationalUnitIndependent() && (CmsOrganizationalUnit.getParentFqn(ouFqn) != null)) {
                        continue;
                    }
                    String groupName = ouFqn + role.getGroupName();
                    int flags = I_CmsPrincipal.FLAG_ENABLED | I_CmsPrincipal.FLAG_GROUP_ROLE;
                    if ((role == CmsRole.WORKPLACE_USER) || (role == CmsRole.PROJECT_MANAGER)) {
                        flags |= I_CmsPrincipal.FLAG_GROUP_PROJECT_USER;
                    }
                    if (role == CmsRole.PROJECT_MANAGER) {
                        flags |= I_CmsPrincipal.FLAG_GROUP_PROJECT_MANAGER;
                    }
                    createGroup(dbc, CmsUUID.getConstantUUID(groupName), groupName, "A system role group", flags, null);
                }
                if ((CmsOrganizationalUnit.getParentFqn(ouFqn) == null) && CmsLog.INIT.isInfoEnabled()) {
                    CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_SYSTEM_ROLES_CREATED_0));
                }
            }
        } catch (CmsException e) {
            if ((CmsOrganizationalUnit.getParentFqn(ouFqn) == null) && CmsLog.INIT.isErrorEnabled()) {
                CmsLog.INIT.error(Messages.get().getBundle().key(Messages.INIT_SYSTEM_ROLES_CREATION_FAILED_0), e);
            }
            throw new CmsInitException(Messages.get().container(Messages.ERR_INITIALIZING_USER_DRIVER_0), e);
        }
        if (webuser) {
            return;
        }
        String administratorsGroup = ouFqn + OpenCms.getDefaultUsers().getGroupAdministrators();
        String guestGroup = ouFqn + OpenCms.getDefaultUsers().getGroupGuests();
        String usersGroup = ouFqn + OpenCms.getDefaultUsers().getGroupUsers();
        String projectmanagersGroup = ouFqn + OpenCms.getDefaultUsers().getGroupProjectmanagers();
        String guestUser = ouFqn + OpenCms.getDefaultUsers().getUserGuest();
        String adminUser = ouFqn + OpenCms.getDefaultUsers().getUserAdmin();
        String exportUser = ouFqn + OpenCms.getDefaultUsers().getUserExport();
        String deleteUser = ouFqn + OpenCms.getDefaultUsers().getUserDeletedResource();
        if (existsGroup(dbc, administratorsGroup)) {
            if (CmsOrganizationalUnit.getParentFqn(ouFqn) == null) {
                internalUpdateRoleGroup(dbc, administratorsGroup, CmsRole.ROOT_ADMIN);
                internalUpdateRoleGroup(dbc, usersGroup, CmsRole.WORKPLACE_USER.forOrgUnit(ouFqn));
                internalUpdateRoleGroup(dbc, projectmanagersGroup, CmsRole.PROJECT_MANAGER.forOrgUnit(ouFqn));
            }
            return;
        }
        String parentOu = CmsOrganizationalUnit.getParentFqn(ouFqn);
        String parentGroup = null;
        if (parentOu != null) {
            parentGroup = parentOu + OpenCms.getDefaultUsers().getGroupUsers();
        }
        String groupDescription = (CmsStringUtil.isNotEmptyOrWhitespaceOnly(ouDescription) ? CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTGROUP_OU_USERS_DESCRIPTION_1, new String[] { ouDescription }) : CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTGROUP_ROOT_USERS_DESCRIPTION_0, null));
        createGroup(dbc, CmsUUID.getConstantUUID(usersGroup), usersGroup, groupDescription, I_CmsPrincipal.FLAG_ENABLED | I_CmsPrincipal.FLAG_GROUP_PROJECT_USER | CmsRole.WORKPLACE_USER.getVirtualGroupFlags(), parentGroup);
        if (parentOu != null) {
            return;
        }
        CmsGroup guests = createGroup(dbc, CmsUUID.getConstantUUID(guestGroup), guestGroup, CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTGROUP_ROOT_GUESTS_DESCRIPTION_0, null), I_CmsPrincipal.FLAG_ENABLED, null);
        int flags = CmsRole.ROOT_ADMIN.getVirtualGroupFlags();
        createGroup(dbc, CmsUUID.getConstantUUID(administratorsGroup), administratorsGroup, CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTGROUP_ROOT_ADMINS_DESCRIPTION_0, null), I_CmsPrincipal.FLAG_ENABLED | I_CmsPrincipal.FLAG_GROUP_PROJECT_MANAGER | flags, null);
        parentGroup = ouFqn + OpenCms.getDefaultUsers().getGroupUsers();
        createGroup(dbc, CmsUUID.getConstantUUID(projectmanagersGroup), projectmanagersGroup, CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTGROUP_ROOT_PROJMANS_DESCRIPTION_0, null), I_CmsPrincipal.FLAG_ENABLED | I_CmsPrincipal.FLAG_GROUP_PROJECT_MANAGER | I_CmsPrincipal.FLAG_GROUP_PROJECT_USER | CmsRole.PROJECT_MANAGER.getVirtualGroupFlags(), parentGroup);
        CmsUser guest = createUser(dbc, CmsUUID.getConstantUUID(guestUser), guestUser, OpenCms.getPasswordHandler().digest(""), " ", " ", " ", 0, I_CmsPrincipal.FLAG_ENABLED, 0, Collections.singletonMap(CmsUserSettings.ADDITIONAL_INFO_DESCRIPTION, CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTUSER_ROOT_GUEST_DESCRIPTION_0, null)));
        createUserInGroup(dbc, guest.getId(), guests.getId());
        CmsUser admin = createUser(dbc, CmsUUID.getConstantUUID(adminUser), adminUser, OpenCms.getPasswordHandler().digest("admin"), " ", " ", " ", 0, I_CmsPrincipal.FLAG_ENABLED, 0, Collections.singletonMap(CmsUserSettings.ADDITIONAL_INFO_DESCRIPTION, CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTUSER_ROOT_ADMIN_DESCRIPTION_0, null)));
        createUserInGroup(dbc, admin.getId(), CmsUUID.getConstantUUID(CmsRole.ROOT_ADMIN.getGroupName()));
        createUserInGroup(dbc, admin.getId(), CmsUUID.getConstantUUID(administratorsGroup));
        if (!OpenCms.getDefaultUsers().getUserExport().equals(OpenCms.getDefaultUsers().getUserAdmin()) && !OpenCms.getDefaultUsers().getUserExport().equals(OpenCms.getDefaultUsers().getUserGuest())) {
            CmsUser export = createUser(dbc, CmsUUID.getConstantUUID(exportUser), exportUser, OpenCms.getPasswordHandler().digest((new CmsUUID()).toString()), " ", " ", " ", 0, I_CmsPrincipal.FLAG_ENABLED, 0, Collections.singletonMap(CmsUserSettings.ADDITIONAL_INFO_DESCRIPTION, CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTUSER_ROOT_EXPORT_DESCRIPTION_0, null)));
            createUserInGroup(dbc, export.getId(), guests.getId());
        }
        if (!OpenCms.getDefaultUsers().getUserDeletedResource().equals(OpenCms.getDefaultUsers().getUserAdmin()) && !OpenCms.getDefaultUsers().getUserDeletedResource().equals(OpenCms.getDefaultUsers().getUserGuest()) && !OpenCms.getDefaultUsers().getUserDeletedResource().equals(OpenCms.getDefaultUsers().getUserExport())) {
            createUser(dbc, CmsUUID.getConstantUUID(deleteUser), deleteUser, OpenCms.getPasswordHandler().digest((new CmsUUID()).toString()), " ", " ", " ", 0, I_CmsPrincipal.FLAG_ENABLED, 0, Collections.singletonMap(CmsUserSettings.ADDITIONAL_INFO_DESCRIPTION, CmsMacroResolver.localizedKeyMacro(Messages.GUI_DEFAULTUSER_ROOT_DELETED_DESCRIPTION_0, null)));
        }
        if (CmsLog.INIT.isInfoEnabled()) {
            CmsLog.INIT.info(Messages.get().getBundle().key(Messages.INIT_DEFAULT_USERS_CREATED_0));
        }
    }
