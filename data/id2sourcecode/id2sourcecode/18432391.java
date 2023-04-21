    public void executeListMultiActions() throws CmsRuntimeException {
        if (getParamListAction().equals(LIST_MACTION_SELECT)) {
            Iterator itItems = getSelectedItems().iterator();
            while (itItems.hasNext()) {
                CmsListItem listItem = (CmsListItem) itItems.next();
                String userName = listItem.get(LIST_COLUMN_DISPLAY).toString();
                List users = getUsers();
                Iterator itUsers = users.iterator();
                while (itUsers.hasNext()) {
                    CmsUser user = (CmsUser) itUsers.next();
                    try {
                        if (user.getName().equals(userName) && ((m_reasons == null) || !m_reasons.containsKey(userName)) && !isAlreadyAvailable(user.getName())) {
                            String password = user.getPassword();
                            if (password.indexOf("_") == -1) {
                                password = OpenCms.getPasswordHandler().digest(password);
                            } else {
                                password = password.substring(password.indexOf("_") + 1);
                            }
                            CmsUser createdUser = getCms().importUser(new CmsUUID().toString(), getParamOufqn() + user.getName(), password, user.getFirstname(), user.getLastname(), user.getEmail(), user.getFlags(), System.currentTimeMillis(), user.getAdditionalInfo());
                            if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(getParamGroups())) {
                                List groups = CmsStringUtil.splitAsList(getParamGroups(), ",");
                                Iterator itGroups = groups.iterator();
                                while (itGroups.hasNext()) {
                                    getCms().addUserToGroup(createdUser.getName(), (String) itGroups.next());
                                }
                            }
                            if (CmsStringUtil.isNotEmptyOrWhitespaceOnly(getParamRoles())) {
                                List roles = CmsStringUtil.splitAsList(getParamRoles(), ",");
                                Iterator itRoles = roles.iterator();
                                while (itRoles.hasNext()) {
                                    OpenCms.getRoleManager().addUserToRole(getCms(), CmsRole.valueOfGroupName((String) itRoles.next()), createdUser.getName());
                                }
                            }
                            break;
                        }
                    } catch (CmsException e) {
                    }
                }
            }
            Map params = new HashMap();
            params.put(A_CmsOrgUnitDialog.PARAM_OUFQN, getParamOufqn());
            params.put(CmsDialog.PARAM_ACTION, CmsDialog.DIALOG_INITIAL);
            try {
                getToolManager().jspForwardTool(this, getParentPath(), params);
            } catch (ServletException e) {
                throw new CmsRuntimeException(Messages.get().container(Messages.ERR_FORWARDING_TO_PARENT_TOOL_0), e);
            } catch (IOException e) {
                throw new CmsRuntimeException(Messages.get().container(Messages.ERR_FORWARDING_TO_PARENT_TOOL_0), e);
            }
        } else {
            throwListUnsupportedActionException();
        }
        listSave();
    }
