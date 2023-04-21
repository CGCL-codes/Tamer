    public static void editRole(Properties ctx, int roleId, String roleName, boolean isAccessAllOrgs, Integer[] menuId, BigDecimal userDiscount, boolean overwritePriceLimit, boolean isDiscountUptoLimitPrice, boolean isDiscountAllowedOnTotal, String trxName) throws OperationException, RoleAlreadyExistsException {
        if (menuId == null) {
            throw new NoCheckBoxSelectedException("Please select a role");
        }
        String nameClause = "AD_Client_ID = " + Env.getAD_Client_ID(ctx) + " and Upper(name)='" + roleName.toUpperCase() + "' and AD_Role_ID <> " + roleId;
        int roleIds[] = MRole.getAllIDs(MRole.Table_Name, nameClause, null);
        if (roleIds != null && roleIds.length > 0) throw new RoleAlreadyExistsException("Role with name: " + roleName + " already exists");
        MRole role = loadRole(ctx, roleId, trxName);
        Boolean isEditable = RoleManager.isEditable(ctx, role.getAD_Org_ID());
        if (!isEditable) throw new NoAccessToEditObjectException("You do not have the right organisational access for editing");
        if (userDiscount == null) {
            userDiscount = Env.ZERO;
        }
        role.setName(roleName);
        role.setIsAccessAllOrgs(isAccessAllOrgs);
        role.setUserDiscount(userDiscount);
        role.setOverwritePriceLimit(overwritePriceLimit);
        role.setIsDiscountUptoLimitPrice(isDiscountUptoLimitPrice);
        role.setIsDiscountAllowedOnTotal(isDiscountAllowedOnTotal);
        PoManager.save(role);
        String whereClause = " AD_Role_ID=" + roleId;
        int roleMenuIds[] = MRoleMenu.getAllIDs(MRoleMenu.Table_Name, whereClause, trxName);
        for (int i = 0; i < roleMenuIds.length; i++) {
            MRoleMenu roleMenu = new MRoleMenu(ctx, roleMenuIds[i], trxName);
            roleMenu.delete(true);
        }
        for (int i = 0; i < menuId.length; i++) {
            MRoleMenu roleMenu = new MRoleMenu(ctx, 0, trxName);
            roleMenu.setAD_Role_ID(role.get_ID());
            roleMenu.setU_WebMenu_ID(menuId[i]);
            PoManager.save(roleMenu);
        }
        int defMenuIds[] = MenuManager.getDefaultMenus(ctx);
        for (int i = 0; i < defMenuIds.length; i++) {
            MRoleMenu roleMenu = new MRoleMenu(ctx, 0, trxName);
            roleMenu.setAD_Role_ID(role.get_ID());
            roleMenu.setU_WebMenu_ID(defMenuIds[i]);
            PoManager.save(roleMenu);
        }
        RoleConfigManager.checkRoleConfig(ctx, roleId, trxName);
    }
