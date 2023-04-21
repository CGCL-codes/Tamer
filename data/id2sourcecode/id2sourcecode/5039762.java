    private void importUserEntries(String branch, File log) {
        FileOutputStream _log = null;
        try {
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
                    AttributeSet attributes = getUserAttributes(_u);
                    try {
                        write(_log, "Importing user [");
                        write(_log, attributes.getAttributeFirstStringValue("uid"));
                        write(_log, "] .. ");
                        _um.addUserEntry(attributes, branch);
                        writeLine(_log, "done");
                        if (this.delete) {
                            _remove_users.remove(attributes.getAttributeFirstStringValue("uid"));
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
