    public static MRole createRole(Properties ctx, int orgId, String roleName, boolean isAccessAllOrgs, BigDecimal userDiscount, boolean overwritePriceLimit, boolean isDiscountAllowedOnTotal, boolean isDiscountUptoLimitPrice, boolean canCreateOrder, boolean canAlterOrder, boolean canViewOrder, String trxName) throws OperationException, RoleAlreadyExistsException {
        String whereClause = "ad_client_id = " + Env.getAD_Client_ID(ctx) + " and ad_org_id in (" + Env.getContext(ctx, UdiConstants.ROLE_EDITABLE_ORGS_CTX_PARAM) + ") and name = '" + roleName + "'";
        int id[] = MRole.getAllIDs(MRole.Table_Name, whereClause, trxName);
        if (id.length > 0) {
            throw new RoleAlreadyExistsException("Please enter another name for the role. Role " + roleName + "already exists.");
        }
        Properties newCtx = (Properties) ctx.clone();
        Env.setContext(newCtx, "#AD_User_ID", 100);
        MRole role = new MRole(newCtx, 0, trxName);
        role.setName(roleName);
        role.setUserDiscount(BigDecimal.valueOf(0.00));
        role.setIsPersonalAccess(true);
        role.setIsAccessAllOrgs(isAccessAllOrgs);
        role.setUserDiscount(userDiscount);
        role.setOverwritePriceLimit(overwritePriceLimit);
        role.setIsDiscountAllowedOnTotal(isDiscountAllowedOnTotal);
        role.setIsDiscountUptoLimitPrice(isDiscountUptoLimitPrice);
        role.setAD_Org_ID(orgId);
        role.setIsAccessAllOrgs(isAccessAllOrgs);
        role.setUserLevel(MRole.USERLEVEL_Organization);
        PoManager.save(role);
        MRoleOrgAccess orgAccess = new MRoleOrgAccess(role, orgId);
        PoManager.save(orgAccess);
        return role;
    }
