    public ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        String op = PortletRequestUtils.getStringParameter(request, WebKeys.URL_OPERATION, "");
        if (!WebHelper.isUserLoggedIn(request)) {
            Map model = new HashMap();
            Map statusMap = new HashMap();
            statusMap.put(WebKeys.AJAX_STATUS_NOT_LOGGED_IN, new Boolean(true));
            model.put(WebKeys.AJAX_STATUS, statusMap);
            if (op.equals(WebKeys.OPERATION_DASHBOARD_HIDE_COMPONENT) || op.equals(WebKeys.OPERATION_DASHBOARD_SHOW_COMPONENT) || op.equals(WebKeys.OPERATION_DASHBOARD_DELETE_COMPONENT) || op.equals(WebKeys.OPERATION_DASHBOARD_SEARCH_MORE)) {
                return new ModelAndView("forum/fetch_url_return", model);
            } else if (op.equals(WebKeys.OPERATION_SHOW_BLOG_REPLIES)) {
                return new ModelAndView("forum/fetch_url_return", model);
            } else if (op.equals(WebKeys.OPERATION_CONFIGURE_FOLDER_COLUMNS) || op.equals(WebKeys.OPERATION_SUBSCRIBE)) {
                return new ModelAndView("forum/fetch_url_return", model);
            } else if (op.equals(WebKeys.OPERATION_UPLOAD_IMAGE_FILE)) {
                return new ModelAndView("forum/fetch_url_return", model);
            }
            response.setContentType("text/xml");
            if (op.equals(WebKeys.OPERATION_UNSEEN_COUNTS)) {
                return new ModelAndView("forum/unseen_counts", model);
            } else if (op.equals(WebKeys.OPERATION_SAVE_COLUMN_POSITIONS)) {
                return new ModelAndView("forum/save_column_positions_return", model);
            } else if (op.equals(WebKeys.OPERATION_SAVE_ENTRY_WIDTH)) {
                return new ModelAndView("forum/save_entry_width_return", model);
            } else if (op.equals(WebKeys.OPERATION_SAVE_ENTRY_HEIGHT)) {
                return new ModelAndView("forum/save_entry_height_return", model);
            } else if (op.equals(WebKeys.OPERATION_GET_FILTER_TYPE) || op.equals(WebKeys.OPERATION_GET_ENTRY_ELEMENTS) || op.equals(WebKeys.OPERATION_GET_ELEMENT_VALUES) || op.equals(WebKeys.OPERATION_GET_ELEMENT_VALUE_DATA) || op.equals(WebKeys.OPERATION_GET_WORKFLOW_STATES)) {
                return new ModelAndView("binder/get_entry_elements", model);
            } else if (op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_FILTER_TYPE) || op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_ENTRY_ELEMENTS) || op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_ELEMENT_VALUES) || op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_ELEMENT_VALUE_DATA)) {
                return new ModelAndView("binder/get_condition_entry_element", model);
            } else if (op.equals(WebKeys.OPERATION_GET_CONDITION_ENTRY_ELEMENTS) || op.equals(WebKeys.OPERATION_GET_CONDITION_ENTRY_VALUE_LIST) || op.equals(WebKeys.OPERATION_GET_CONDITION_ENTRY_VALUE_LIST)) {
                return new ModelAndView("definition_builder/get_condition_element", model);
            } else if (op.equals(WebKeys.OPERATION_WORKSPACE_TREE)) {
                return new ModelAndView("tag_jsps/tree/get_tree_div", model);
            } else if (op.equals(WebKeys.OPERATION_ADD_FAVORITE_BINDER) || op.equals(WebKeys.OPERATION_ADD_FAVORITES_CATEGORY) || op.equals(WebKeys.OPERATION_SAVE_FAVORITES)) {
                return new ModelAndView("forum/favorites_return", model);
            } else if (op.equals(WebKeys.OPERATION_GET_FAVORITES_TREE)) {
                return new ModelAndView("forum/favorites_tree", model);
            } else if (op.equals(WebKeys.OPERATION_SHOW_HELP_PANEL)) {
                return new ModelAndView("forum/ajax_return", model);
            } else if (op.equals(WebKeys.OPERATION_GET_ACCESS_CONTROL_TABLE)) {
                return new ModelAndView("binder/access_control_table", model);
            } else if (op.equals(WebKeys.OPERATION_START_MEETING)) {
                return new ModelAndView("forum/meeting_return", model);
            } else if (op.equals(WebKeys.OPERATION_SCHEDULE_MEETING)) {
                return new ModelAndView("forum/meeting_return", model);
            }
            return new ModelAndView("forum/ajax_return", model);
        }
        if (op.equals(WebKeys.OPERATION_UNSEEN_COUNTS)) {
            return ajaxGetUnseenCounts(request, response);
        } else if (op.equals(WebKeys.OPERATION_ADD_FAVORITE_BINDER) || op.equals(WebKeys.OPERATION_ADD_FAVORITES_CATEGORY) || op.equals(WebKeys.OPERATION_GET_FAVORITES_TREE) || op.equals(WebKeys.OPERATION_SAVE_FAVORITES)) {
            return ajaxGetFavoritesTree(request, response);
        } else if (op.equals(WebKeys.OPERATION_SAVE_COLUMN_POSITIONS)) {
            return new ModelAndView("forum/save_column_positions_return");
        } else if (op.equals(WebKeys.OPERATION_CONFIGURE_FOLDER_COLUMNS)) {
            return ajaxConfigureFolderColumns(request, response);
        } else if (op.equals(WebKeys.OPERATION_SUBSCRIBE)) {
            return ajaxSubscribe(request, response);
        } else if (op.equals(WebKeys.OPERATION_SAVE_ENTRY_WIDTH)) {
            return ajaxSaveEntryWidth(request, response);
        } else if (op.equals(WebKeys.OPERATION_SAVE_ENTRY_HEIGHT)) {
            return ajaxSaveEntryHeight(request, response);
        } else if (op.equals(WebKeys.OPERATION_MODIFY_TAGS)) {
            return ajaxShowTags(request, response);
        } else if (op.equals(WebKeys.OPERATION_USER_LIST_SEARCH)) {
            return ajaxUserListSearch(request, response);
        } else if (op.equals(WebKeys.OPERATION_FIND_USER_SEARCH) || op.equals(WebKeys.OPERATION_FIND_PLACES_SEARCH) || op.equals(WebKeys.OPERATION_FIND_ENTRIES_SEARCH) || op.equals(WebKeys.OPERATION_FIND_TAG_SEARCH)) {
            return ajaxFindUserSearch(request, response);
        } else if (op.equals(WebKeys.OPERATION_GET_FILTER_TYPE) || op.equals(WebKeys.OPERATION_GET_ENTRY_ELEMENTS) || op.equals(WebKeys.OPERATION_GET_ELEMENT_VALUES) || op.equals(WebKeys.OPERATION_GET_ELEMENT_VALUE_DATA) || op.equals(WebKeys.OPERATION_GET_WORKFLOW_STATES)) {
            return ajaxGetFilterData(request, response);
        } else if (op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_FILTER_TYPE) || op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_ENTRY_ELEMENTS) || op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_ELEMENT_VALUES) || op.equals(WebKeys.OPERATION_GET_SEARCH_FORM_ELEMENT_VALUE_DATA)) {
            return ajaxGetSearchFormData(request, response);
        } else if (op.equals(WebKeys.OPERATION_GET_CONDITION_ENTRY_ELEMENTS) || op.equals(WebKeys.OPERATION_GET_CONDITION_ENTRY_OPERATIONS) || op.equals(WebKeys.OPERATION_GET_CONDITION_ENTRY_VALUE_LIST)) {
            return ajaxGetConditionData(request, response);
        } else if (op.equals(WebKeys.OPERATION_WORKSPACE_TREE)) {
            return ajaxGetWorkspaceTree(request, response);
        } else if (op.equals(WebKeys.OPERATION_DASHBOARD_HIDE_COMPONENT) || op.equals(WebKeys.OPERATION_DASHBOARD_SHOW_COMPONENT) || op.equals(WebKeys.OPERATION_DASHBOARD_DELETE_COMPONENT)) {
            return ajaxGetDashboardComponent(request, response);
        } else if (op.equals(WebKeys.OPERATION_DASHBOARD_SEARCH_MORE)) {
            return ajaxGetDashboardSearchMore(request, response);
        } else if (op.equals(WebKeys.OPERATION_SHOW_BLOG_REPLIES)) {
            return ajaxGetBlogReplies(request, response);
        } else if (op.equals(WebKeys.OPERATION_SAVE_RATING)) {
            return ajaxGetEntryRating(request, response);
        } else if (op.equals(WebKeys.OPERATION_SHOW_HELP_PANEL)) {
            return ajaxShowHelpPanel(request, response);
        } else if (op.equals(WebKeys.OPERATION_ADD_TAB)) {
            return ajaxAddTab(request, response);
        } else if (op.equals(WebKeys.OPERATION_DELETE_TAB)) {
            return ajaxDeleteTab(request, response);
        } else if (op.equals(WebKeys.OPERATION_SET_CURRENT_TAB)) {
            return ajaxSetCurrentTab(request, response);
        } else if (op.equals(WebKeys.OPERATION_GET_ACCESS_CONTROL_TABLE)) {
            return ajaxGetAccessControlTable(request, response);
        } else if (op.equals(WebKeys.OPERATION_UPLOAD_IMAGE_FILE)) {
            return ajaxGetUploadImageFile(request, response);
        } else if (op.equals(WebKeys.OPERATION_ADD_ATTACHMENT_OPTIONS)) {
            return addAttachmentOptions(request, response);
        } else if (op.equals(WebKeys.OPERATION_RELOAD_ENTRY_ATTACHMENTS)) {
            return reloadEntryAttachment(request, response);
        } else if (op.equals(WebKeys.OPERATION_OPEN_WEBDAV_FILE)) {
            return openWebDAVFile(request, response);
        } else if (op.equals(WebKeys.OPERATION_ADD_FOLDER_ATTACHMENT_OPTIONS)) {
            return addFolderAttachmentOptions(request, response);
        } else if (op.equals(WebKeys.OPERATION_START_MEETING)) {
            return ajaxStartMeeting(request, response, ICBroker.REGULAR_MEETING);
        } else if (op.equals(WebKeys.OPERATION_SCHEDULE_MEETING)) {
            return ajaxStartMeeting(request, response, ICBroker.SCHEDULED_MEETING);
        } else if (op.equals(WebKeys.OPERATION_GET_TEAM_MEMBERS)) {
            return ajaxGetTeamMembers(request, response);
        } else if (op.equals(WebKeys.OPERATION_GET_CLIPBOARD_USERS)) {
            return ajaxGetClipboardUsers(request, response);
        } else if (op.equals(WebKeys.OPERATION_SET_BINDER_OWNER_ID)) {
            return ajaxGetBinderOwner(request, response);
        }
        return ajaxReturn(request, response);
    }
