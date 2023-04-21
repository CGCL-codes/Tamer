    private void importUserEntries(File log) {
        FileOutputStream _log = null;
        try {
            NetworkManager _nm = new NetworkManager(this._local_c);
            EntryManager _em = new EntryManager(this._local_c);
            UserManager _um = new UserManager(this._local_c);
            _log = new FileOutputStream(log, true);
            List<String> _remove_users = new ArrayList<String>();
            if (this.delete) {
                write(_log, "Reading users from local directory .. ");
                for (Entry _u : _em.getAllEntries(EntryManager.USER)) {
                    if (_u.hasAttribute("uid")) {
                        String _uid = String.valueOf(_u.getAttribute("uid")[0]);
                        if (!_uid.toLowerCase().equals("administrator") && !_uid.toLowerCase().equals("administrador")) {
                            _remove_users.add(_uid);
                        }
                    }
                }
                writeLine(_log, "done");
            }
            this._eb.setScope(LDAPConnection.ONE_SCOPE);
            Query _q = new Query();
            _q.addCondition("objectclass", "top", Query.EXACT);
            write(_log, "Reading remote base entries .. ");
            List<Entry> _containers = this._eb.search(_q);
            writeLine(_log, "done");
            for (Entry _ec : _containers) {
                writeLine(_log, "Processing container: " + _ec.getID());
                this._eb.setScope(LDAPConnection.SUBTREE_SCOPE);
                _q = new Query();
                _q.addCondition("objectclass", "person", Query.EXACT);
                _q.addCondition("objectclass", "computer", Query.NOT_EXACT);
                if (_ec.hasAttribute("ou")) {
                    _q.addCondition("ou", String.valueOf(_ec.getAttribute("ou")[0]), Query.BRANCH);
                } else if (_ec.hasAttribute("cn")) {
                    _q.addCondition("cn", String.valueOf(_ec.getAttribute("cn")[0]), Query.BRANCH);
                }
                write(_log, "Reading entries from remote directory .. ");
                List<Entry> _results = this._eb.search(_q);
                writeLine(_log, "done");
                for (Entry _u : _results) {
                    if (!_u.hasAttribute("sAMAccountName") || !_u.hasAttribute("cn") || this._sc.getAdministrativeUser().equals(_u.getAttribute("sAMAccountName")[0])) {
                        continue;
                    }
                    if (_um.userExists(String.valueOf(_u.getAttribute("sAMAccountName")[0]))) {
                        write(_log, "User already exists [");
                        write(_log, String.valueOf(_u.getAttribute("sAMAccountName")[0]));
                        writeLine(_log, "] .. ignored");
                        continue;
                    }
                    Map<String, Object> attributes = new HashMap<String, Object>();
                    String chain = null;
                    List<String> _values = new ArrayList<String>();
                    if (_u.hasAttribute("department")) {
                        _values.add(String.valueOf(_u.getAttribute("department")[0]));
                    }
                    if (_u.hasAttribute("memberOf")) {
                        Object[] chain_values = (Object[]) _u.getAttribute("memberOf");
                        for (int j = chain_values.length; --j >= 0; ) {
                            chain = (String) chain_values[j];
                            if (chain.toLowerCase().indexOf("ou=groups") == -1) {
                                while (chain.toLowerCase().startsWith("ou=") || chain.toLowerCase().startsWith("cn=")) {
                                    if (_values.indexOf(chain.substring(0, chain.indexOf(",")).substring(chain.indexOf("=") + 1)) == -1) {
                                        String val_tmp = chain.substring(0, chain.indexOf(",")).substring(chain.indexOf("=") + 1);
                                        _values.add(val_tmp.toLowerCase());
                                    }
                                    chain = chain.substring(chain.indexOf(",") + 1).trim();
                                }
                            }
                        }
                    }
                    if (_values.size() > 0) {
                        attributes.put("ou", _values.toArray());
                    }
                    try {
                        long _UAC = Long.parseLong(String.valueOf(_u.getAttribute("userAccountControl")[0]));
                        if ((_UAC & UF_ACCOUNTDISABLE) == UF_ACCOUNTDISABLE) {
                            attributes.put("accountStatus", "disabled");
                        }
                    } catch (NumberFormatException _ex) {
                    }
                    attributes.put("uid", _u.getAttribute("sAMAccountName")[0]);
                    attributes.put("cn", _u.getAttribute("cn")[0]);
                    attributes.put("gecos", CharacterEncode.toASCII(String.valueOf(_u.getAttribute("cn")[0])));
                    attributes.put("password", "12345");
                    if (_u.hasAttribute("givenName")) {
                        attributes.put("givenName", _u.getAttribute("givenName")[0]);
                    } else {
                        attributes.put("givenName", _u.getAttribute("cn")[0]);
                    }
                    if (_u.hasAttribute("sn")) {
                        attributes.put("sn", _u.getAttribute("sn")[0]);
                    } else {
                        attributes.put("sn", _u.getAttribute("cn")[0]);
                    }
                    if (_u.hasAttribute("employeetype")) {
                        attributes.put("employeeType", _u.getAttribute("employeetype")[0]);
                    }
                    if (_u.hasAttribute("displayName")) {
                        attributes.put("displayName", _u.getAttribute("displayName")[0]);
                    } else {
                        attributes.put("displayName", _u.getAttribute("cn")[0]);
                    }
                    if (_u.hasAttribute("title")) {
                        attributes.put("title", _u.getAttribute("title")[0]);
                    }
                    if (_u.hasAttribute("company")) {
                        attributes.put("o", _u.getAttribute("company")[0]);
                    }
                    if (_u.hasAttribute("description")) {
                        attributes.put("description", _u.getAttribute("description")[0]);
                    }
                    if (_u.hasAttribute("mail")) {
                        attributes.put("maildrop", _u.getAttribute("mail")[0]);
                        attributes.put("mail", _u.getAttribute("mail")[0]);
                    } else {
                        attributes.put("maildrop", attributes.get("uid") + "@" + _nm.getDomain());
                        attributes.put("mail", attributes.get("uid") + "@" + _nm.getDomain());
                    }
                    if (_u.hasAttribute("telephoneNumber")) {
                        attributes.put("telephoneNumber", _u.getAttribute("telephoneNumber")[0]);
                    }
                    if (_u.hasAttribute("facsimileTelephoneNumber")) {
                        attributes.put("facsimileTelephoneNumber", _u.getAttribute("facsimileTelephoneNumber")[0]);
                    }
                    if (_u.hasAttribute("st")) {
                        attributes.put("st", _u.getAttribute("st")[0]);
                    }
                    if (_u.hasAttribute("l")) {
                        attributes.put("l", _u.getAttribute("l")[0]);
                    }
                    if (_u.hasAttribute("homeDrive")) {
                        attributes.put("sambaHomeDrive", _u.getAttribute("homeDrive")[0]);
                    } else {
                        if (this._local_c.getProperty("samba.home.drive") != null) {
                            attributes.put("sambaHomeDrive", this._local_c.getProperty("samba.home.drive"));
                        }
                    }
                    if (_u.hasAttribute("homeDirectory")) {
                        attributes.put("sambaHomePath", _u.getAttribute("homeDirectory")[0]);
                    } else {
                        if (this._local_c.getProperty("samba.home.server") != null) {
                            attributes.put("sambaHomePath", "\\\\" + this._local_c.getProperty("samba.home.server") + "\\" + _u.getAttribute("sAMAccountName")[0]);
                        }
                    }
                    if (_u.hasAttribute("scriptPath")) {
                        attributes.put("sambaLogonScript", _u.getAttribute("scriptPath")[0]);
                    }
                    String _branch = null;
                    if (this._remote_c.getProperty("ldap.replica.container") != null && !this._remote_c.getProperty("ldap.replica.container").isEmpty()) {
                        _branch = this._remote_c.getProperty("ldap.replica.container");
                    }
                    try {
                        write(_log, "Importing user [");
                        write(_log, String.valueOf(attributes.get("uid")));
                        write(_log, "] .. ");
                        _um.addUserEntry(attributes, _branch);
                        writeLine(_log, "done");
                        if (this.delete) {
                            _remove_users.remove(attributes.get("uid"));
                        }
                    } catch (Exception _ex) {
                        writeLine(_log, "error - " + _ex.getMessage());
                    }
                }
            }
            if (this.delete) {
                for (String uid : _remove_users) {
                    write(_log, "Deleting user entry [");
                    write(_log, uid);
                    write(_log, "]: ");
                    try {
                        _um.deleteUserEntry(uid);
                        writeLine(_log, "done");
                    } catch (Exception _ex) {
                        write(_log, "error - ");
                        writeLine(_log, _ex.getMessage());
                    }
                }
            }
        } catch (Exception _ex) {
            writeLine(_log, "error - " + _ex.getMessage());
        } finally {
            if (_log != null) {
                try {
                    _log.close();
                } catch (IOException _ex2) {
                }
            }
        }
    }
