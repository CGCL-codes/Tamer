    public void actionDialog() throws JspException, ServletException, IOException {
        switch(getAction()) {
            case ACTION_IMPORT:
                List users = getUsers();
                Iterator itUsers = users.iterator();
                while (itUsers.hasNext()) {
                    CmsUser user = (CmsUser) itUsers.next();
                    if (((m_reasons == null) || !m_reasons.containsKey(user.getName())) && !isAlreadyAvailable(user.getName())) {
                        String password = user.getPassword();
                        if (password.indexOf("_") == -1) {
                            try {
                                password = OpenCms.getPasswordHandler().digest(password);
                            } catch (CmsPasswordEncryptionException e) {
                                throw new CmsRuntimeException(Messages.get().container(Messages.ERR_DIGEST_PASSWORD_0), e);
                            }
                        } else {
                            password = password.substring(password.indexOf("_") + 1);
                        }
                        CmsUser createdUser;
                        try {
                            createdUser = getCms().importUser(new CmsUUID().toString(), getParamOufqn() + user.getName(), password, user.getFirstname(), user.getLastname(), user.getEmail(), user.getFlags(), System.currentTimeMillis(), user.getAdditionalInfo());
                        } catch (CmsException e) {
                            throw new CmsRuntimeException(Messages.get().container(Messages.ERR_IMPORT_USER_0), e);
                        }
                        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(getParamGroups())) {
                            List groups = CmsStringUtil.splitAsList(getParamGroups(), ",");
                            Iterator itGroups = groups.iterator();
                            while (itGroups.hasNext()) {
                                try {
                                    getCms().addUserToGroup(createdUser.getName(), (String) itGroups.next());
                                } catch (CmsException e) {
                                    throw new CmsRuntimeException(Messages.get().container(Messages.ERR_ADD_USER_TO_GROUP_0), e);
                                }
                            }
                        }
                        if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(getParamRoles())) {
                            List roles = CmsStringUtil.splitAsList(getParamRoles(), ",");
                            Iterator itRoles = roles.iterator();
                            while (itRoles.hasNext()) {
                                try {
                                    OpenCms.getRoleManager().addUserToRole(getCms(), CmsRole.valueOfGroupName((String) itRoles.next()), createdUser.getName());
                                } catch (CmsException e) {
                                    throw new CmsRuntimeException(Messages.get().container(Messages.ERR_ADD_USER_TO_ROLE_0), e);
                                }
                            }
                        }
                    }
                }
                setAction(ACTION_CANCEL);
                actionCloseDialog();
                break;
            default:
                super.actionDialog();
        }
    }
