    private static void internalRun(ExternalRepository _er, int _interval, String branch, File lastModificationFile, File lastGroupListFile, File lastUserListFile, File log, List<String> report_emails, boolean update) throws Exception {
        Configuration _c = new Configuration(new File(WBSAgnitioConfiguration.getConfigurationFile()));
        RuleEngine _re = new RuleEngine(null);
        RoleManager _rm = new RoleManager();
        UserManager _um = new UserManager(_c);
        GroupManager _gm = new GroupManager(_c);
        ProfileManager _pm = new ProfileManager();
        try {
            writeLine(log, "Listener started");
            for (Thread.sleep(_interval); true; Thread.sleep(_interval)) {
                try {
                    List<String> _new_users = new ArrayList<String>();
                    List<String> _modified_users = new ArrayList<String>();
                    List<String> _removed_users = new ArrayList<String>();
                    Map<String, String> _added_profiles = new HashMap<String, String>();
                    Map<String, String> _removed_profiles = new HashMap<String, String>();
                    List<String> _new_groups = new ArrayList<String>();
                    List<String> _modified_groups = new ArrayList<String>();
                    List<String> _removed_groups = new ArrayList<String>();
                    Calendar _lastModification = Calendar.getInstance();
                    Calendar _cal = getLastModificationCalendar(lastModificationFile);
                    List<String> _users = new ArrayList<String>();
                    List<String> _groups = new ArrayList<String>();
                    List<String> _old_users = getLastList(lastUserListFile);
                    List<String> _old_groups = getLastList(lastGroupListFile);
                    writeLine(log, "Searching users modified since: " + getStringCalendar(_cal));
                    List<AttributeSet> _entries = _er.getUserEntries(_cal, _users);
                    for (AttributeSet _attributes : _entries) {
                        if (!_um.userExists(_attributes.getAttributeFirstStringValue("uid"))) {
                            writeLine(log, "Found a new user: " + _attributes.getAttributeFirstStringValue("uid"));
                            if (!report_emails.isEmpty()) {
                                _new_users.add(_attributes.getAttributeFirstStringValue("uid"));
                            }
                            if (update) {
                                try {
                                    _attributes.setAttribute("branch", branch);
                                    _re.writeEntry(RuleEngine.USER_ADD, _attributes);
                                    writeLine(log, "Added user: " + _attributes.getAttributeFirstStringValue("uid"));
                                } catch (Exception _ex) {
                                    writeLine(log, "Add user error: " + _ex.getMessage());
                                }
                            }
                        } else {
                            if (!report_emails.isEmpty()) {
                                try {
                                    AttributeSet _user_attributes = new AttributeSet(_um.getUserEntry(_attributes.getAttributeFirstStringValue("uid")));
                                    List<String> keys = _attributes.getAttributeNames();
                                    for (String entry : keys) {
                                        if (!"password".equals(entry) && !"userpassword".equals(entry) && _attributes.getAttribute(entry) != null && _attributes.getAttribute(entry)[0] != null && (!_attributes.getAttribute(entry)[0].equals(_user_attributes.getAttribute(entry)[0]))) {
                                            writeLine(log, "Found attribute differences for this user " + _attributes.getAttributeFirstStringValue("uid"));
                                            _modified_users.add(_attributes.getAttributeFirstStringValue("uid"));
                                            break;
                                        }
                                    }
                                } catch (Exception _ex) {
                                    writeLine(log, "Attribute consistency check error: " + _ex.getMessage());
                                }
                            }
                            writeLine(log, "Found an existing user: " + _attributes.getAttributeFirstStringValue("uid"));
                            if (update) {
                                try {
                                    _attributes.setAttribute("branch", branch);
                                    _re.writeEntry(RuleEngine.USER_UPDATE, _attributes);
                                    writeLine(log, "Updated user: " + _attributes.getAttributeFirstStringValue("uid"));
                                } catch (Exception _ex) {
                                    writeLine(log, "Update user error: " + _ex.getMessage());
                                }
                            }
                        }
                        writeLine(log, "Searching user profiles: ");
                        try {
                            Entry _e_user = _um.getUserEntry(_attributes.getAttributeFirstStringValue("uid"));
                            List<String> _activeProfiles = _er.getProfiles(_attributes.getAttributeFirstStringValue("uid"));
                            for (Map<String, String> _profileRepository : _pm.getProfiles(_er.getRepositoryName())) {
                                if (_activeProfiles.contains(_profileRepository.get("name"))) {
                                    for (String _role : _pm.getProfileRoles(_profileRepository.get("name"))) {
                                        writeLine(log, "Profile is associated with the role: " + _role);
                                        for (String _group : _rm.getGroups(_role)) {
                                            Entry _e_group = _gm.getGroupEntry(_group);
                                            if (!_gm.isGroupMember(_e_group.getID(), _e_user.getID())) {
                                                if (!report_emails.isEmpty()) {
                                                    _added_profiles.put(_role, _attributes.getAttributeFirstStringValue("uid"));
                                                }
                                                if (update) {
                                                    writeLine(log, "Adding user to role group: " + _group);
                                                    _re.writeMembershipEntry(RuleEngine.GROUP_USER_ADD, _e_group, _e_user);
                                                }
                                            } else {
                                                writeLine(log, "User is already member of role group: " + _group);
                                            }
                                        }
                                    }
                                } else {
                                    for (String _role : _pm.getProfileRoles(_profileRepository.get("name"))) {
                                        writeLine(log, "Profile is not associated with the role: " + _role);
                                        for (String _group : _rm.getGroups(_role)) {
                                            Entry _e_group = _gm.getGroupEntry(_group);
                                            if (_gm.isGroupMember(_e_group.getID(), _e_user.getID())) {
                                                if (!report_emails.isEmpty()) {
                                                    _removed_profiles.put(_role, _attributes.getAttributeFirstStringValue("uid"));
                                                }
                                                if (update) {
                                                    writeLine(log, "Removing user to role group: " + _group);
                                                    _re.writeMembershipEntry(RuleEngine.GROUP_USER_REMOVE, _e_group, _e_user);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception _ex) {
                            writeLine(log, "Profile search error: " + _ex.toString());
                        }
                    }
                    if (_old_users != null) {
                        _old_users.removeAll(_users);
                        for (String _user : _old_users) {
                            AttributeSet _attributes = new AttributeSet();
                            _attributes.setAttribute("uid", _user);
                            writeLine(log, "Found a removed user: " + _attributes.getAttributeFirstStringValue("uid"));
                            if (!report_emails.isEmpty()) {
                                _removed_users.add(_user);
                            }
                            if (update) {
                                try {
                                    _re.writeEntry(RuleEngine.USER_REMOVE, _attributes);
                                    writeLine(log, "Removed user: " + _attributes.getAttributeFirstStringValue("uid"));
                                } catch (Exception _ex) {
                                    writeLine(log, "Remove user error: " + _ex.getMessage());
                                }
                            }
                        }
                    }
                    writeLine(log, "Searching groups modified since: " + getStringCalendar(_cal));
                    _entries = _er.getGroupEntries(_cal, _groups);
                    for (AttributeSet _attributes : _entries) {
                        if (!_gm.groupExists(_attributes.getAttributeFirstStringValue("cn"))) {
                            writeLine(log, "Found a new group: " + _attributes.getAttributeFirstStringValue("cn"));
                            if (!report_emails.isEmpty()) {
                                _new_groups.add(_attributes.getAttributeFirstStringValue("cn"));
                            }
                            if (update) {
                                try {
                                    _re.writeEntry(RuleEngine.GROUP_ADD, _attributes);
                                    writeLine(log, "Added group: " + _attributes.getAttributeFirstStringValue("cn"));
                                } catch (Exception _ex) {
                                    writeLine(log, "Add group error: " + _ex.getMessage());
                                }
                            }
                        } else {
                            writeLine(log, "Found an existing group: " + _attributes.getAttributeFirstStringValue("cn"));
                            if (!report_emails.isEmpty()) {
                                AttributeSet _group_attributes = new AttributeSet(_gm.getGroupEntry(_attributes.getAttributeFirstStringValue("cn")));
                                if (!_attributes.equals(_group_attributes)) {
                                    writeLine(log, "Found attribute differences for this group");
                                    _modified_groups.add(_attributes.getAttributeFirstStringValue("cn"));
                                }
                            }
                            if (update) {
                                try {
                                    _re.writeEntry(RuleEngine.GROUP_UPDATE, _attributes);
                                    writeLine(log, "Updated group: " + _attributes.getAttributeFirstStringValue("cn"));
                                } catch (Exception _ex) {
                                    writeLine(log, "Update group error: " + _ex.getMessage());
                                }
                            }
                        }
                    }
                    if (_old_groups != null) {
                        _old_groups.removeAll(_groups);
                        for (String _group : _old_groups) {
                            AttributeSet _attributes = new AttributeSet();
                            _attributes.setAttribute("cn", _group);
                            writeLine(log, "Found a removed group: " + _attributes.getAttributeFirstStringValue("cn"));
                            if (!report_emails.isEmpty()) {
                                _removed_groups.add(_group);
                            }
                            if (update) {
                                try {
                                    _re.writeEntry(RuleEngine.GROUP_REMOVE, _attributes);
                                    writeLine(log, "Removed group: " + _attributes.getAttributeFirstStringValue("cn"));
                                } catch (Exception _ex) {
                                    writeLine(log, "Remove group error: " + _ex.getMessage());
                                }
                            }
                        }
                    }
                    writeLastModification(lastModificationFile, _lastModification);
                    writeLastList(lastGroupListFile, _groups);
                    writeLastList(lastUserListFile, _users);
                    if (!report_emails.isEmpty()) {
                        Map<String, String> _fields = new HashMap<String, String>();
                        _fields.put("Repository", _er.getRepositoryName());
                        if (!_new_users.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following users have been created in the repository and are not integrated into the management database:<br/>\n");
                            for (String _user : _new_users) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_user);
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Added users", _sb.toString());
                        }
                        if (!_modified_users.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following users have been modified in the repository and are not modified into the management database:<br/>\n");
                            for (String _user : _modified_users) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_user);
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Modified users", _sb.toString());
                        }
                        if (!_removed_users.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following users have been removed in the repository and are not removed into the management database:<br/>\n");
                            for (String _user : _removed_users) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_user);
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Removed users", _sb.toString());
                        }
                        if (!_added_profiles.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following profiles have been added in the repository and are not modified into the management database:<br/>\n");
                            for (String _profile : _added_profiles.keySet()) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_profile);
                                _sb.append("</strong> / <strong>");
                                _sb.append(_added_profiles.get(_profile));
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Added profiles", _sb.toString());
                        }
                        if (!_removed_profiles.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following profiles have been removed in the repository and are not modified into the management database:<br/>\n");
                            for (String _profile : _removed_profiles.keySet()) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_profile);
                                _sb.append("</strong> / <strong>");
                                _sb.append(_removed_profiles.get(_profile));
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Removed profiles", _sb.toString());
                        }
                        if (!_new_groups.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following groups have been created in the repository and are not integrated into the management database:<br/>\n");
                            for (String _group : _new_groups) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_group);
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Added groups", _sb.toString());
                        }
                        if (!_modified_groups.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following groups have been modified in the repository and are not modified into the management database:<br/>\n");
                            for (String _group : _modified_groups) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_group);
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Modified groups", _sb.toString());
                        }
                        if (!_removed_groups.isEmpty()) {
                            StringBuilder _sb = new StringBuilder();
                            _sb.append("The following groups have been removed in the repository and are not integrated into the management database:<br/>\n");
                            for (String _group : _removed_groups) {
                                _sb.append("&nbsp;<strong>");
                                _sb.append(_group);
                                _sb.append("</strong><br/>\n");
                            }
                            _fields.put("Removed groups", _sb.toString());
                        }
                        if (!_fields.isEmpty()) {
                            CustomTransport _mt = new CustomTransport();
                            _mt.setMailFields(_fields);
                            _mt.sendMail(report_emails, "WBSAGNITIO ENFORCEMENT COMPLIANCE REPORT");
                        }
                    }
                } catch (IllegalStateException _ex) {
                    writeLine(log, "Listener invalid application state: exiting ... (maybe the application has been updated?)");
                    Thread.currentThread().interrupt();
                } catch (NullPointerException _ex) {
                    writeLine(log, "Listener invalid application state: exiting ... (maybe the application has been updated?)");
                    Thread.currentThread().interrupt();
                } catch (Exception _ex) {
                    writeLine(log, "Listener error: " + _ex.getMessage());
                }
            }
        } catch (InterruptedException _ex) {
        } catch (IllegalStateException _ex) {
            writeLine(log, "Listener invalid application state: exiting ... (maybe the application has been updated?)");
        }
        writeLine(log, "Listener stopped");
    }
