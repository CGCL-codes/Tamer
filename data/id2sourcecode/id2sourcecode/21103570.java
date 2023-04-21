    @Override
    public void updateDatabase(ModelRequest req, Logger logger, Connection connection, PersistentFactory pf, ModuleVersion currentVersion, ModuleVersion newVersion) throws Exception {
        if (currentVersion.between("0.0.0", "2.1.2")) {
            currentVersion.setVersion("2.1.2");
        }
        if (currentVersion.lessThan("2.2.1")) {
            createPrimaryKeySequenceFromIdTable("AkteraGroup", "akteraGroupId");
            createPrimaryKeySequenceFromIdTable("AkteraGroupEntry", "id");
            createPrimaryKeySequenceFromIdTable("keelusers", "uniqid");
            addColumn("keelusers", "ldapname", "varchar(120)");
            updateColumnType("keelusers", "passwd", "varchar(255)");
            for (Map<String, Object> row : (List<Map<String, Object>>) query("SELECT uniqid, userName FROM keelusers", new MapListHandler())) {
                update("UPDATE keelusers SET passwd = '" + StringTools.digest(row.get("userName")) + "' WHERE uniqid = " + row.get("uniqid"));
            }
            for (Map<String, Object> row : (List<Map<String, Object>>) query("SELECT id, name FROM IritgoUser", new MapListHandler())) {
                update("UPDATE IritgoUser SET password = '" + StringTools.digest(row.get("name")) + "' WHERE id = " + row.get("id"));
            }
            for (Map<String, Object> row : (List<Map<String, Object>>) query("SELECT id, name FROM AktarioUser", new MapListHandler())) {
                update("UPDATE AktarioUser SET password = '" + StringTools.digest(row.get("name")) + "' WHERE id = " + row.get("id"));
            }
            currentVersion.setVersion("2.2.1");
        }
        if (currentVersion.lessThan("2.3.1")) {
            updateColumnType("keelusers", "uniqid", "int");
            renameIdColumn("AkteraGroup", "akteraGroupId", "id");
            updateColumnType("AkteraGroup", "name", "varchar(255)");
            addColumn("AkteraGroup", "visible", "boolean");
            addColumn("AkteraGroup", "title", "varchar(255)");
            update("UPDATE AkteraGroup SET visible = true");
            update("UPDATE AkteraGroup SET protect = false where protect is null");
            if (count("AkteraGroup", "name = '" + AkteraGroup.GROUP_NAME_ADMINISTRATOR + "'") == 0) {
                insert("AkteraGroup", "name", "'" + AkteraGroup.GROUP_NAME_ADMINISTRATOR + "'", "protect", "true", "visible", "false", "title", "'$Aktera:administrators'");
                long adminGroupId = NumberTools.toLong(selectString("AkteraGroup", "id", "name = '" + AkteraGroup.GROUP_NAME_ADMINISTRATOR + "'"), -1);
                if (adminGroupId != -1) {
                    long adminUserId = NumberTools.toLong(selectString("keelusers", "uniqid", "username = '" + AkteraUser.USER_NAME_ADMINISTRATOR + "'"), -1);
                    if (adminUserId != -1) {
                        insert("AkteraGroupEntry", "groupId", String.valueOf(adminGroupId), "userId", String.valueOf(adminUserId), "position", "1");
                    }
                }
            }
            if (count("AkteraGroup", "name = '" + AkteraGroup.GROUP_NAME_MANAGER + "'") == 0) {
                insert("AkteraGroup", "name", "'" + AkteraGroup.GROUP_NAME_MANAGER + "'", "protect", "true", "visible", "false", "title", "'$Aktera:managers'");
                long managerGroupId = NumberTools.toLong(selectString("AkteraGroup", "id", "name = '" + AkteraGroup.GROUP_NAME_MANAGER + "'"), -1);
                if (managerGroupId != -1) {
                    long managerUserId = NumberTools.toLong(selectString("keelusers", "uniqid", "username = '" + AkteraUser.USER_NAME_MANAGER + "'"), -1);
                    if (managerUserId != -1) {
                        insert("AkteraGroupEntry", "groupId", String.valueOf(managerGroupId), "userId", String.valueOf(managerUserId), "position", "1");
                    }
                }
            }
            if (count("AkteraGroup", "name = '" + AkteraGroup.GROUP_NAME_USER + "'") == 0) {
                insert("AkteraGroup", "name", "'" + AkteraGroup.GROUP_NAME_USER + "'", "protect", "true", "visible", "false", "title", "'$Aktera:users'");
                long userGroupId = NumberTools.toLong(selectString("AkteraGroup", "id", "name = '" + AkteraGroup.GROUP_NAME_USER + "'"), -1);
                if (userGroupId != -1) {
                    int position = 1;
                    for (Map<String, Object> row : (List<Map<String, Object>>) query("SELECT uniqid FROM keelusers WHERE uniqid > 2", new MapListHandler())) {
                        insert("AkteraGroupEntry", "groupId", String.valueOf(userGroupId), "userId", String.valueOf(row.get("uniqid")), "position", String.valueOf(position++));
                    }
                }
            }
            update("UPDATE AkteraGroup SET protect = true WHERE name = '" + AkteraGroup.GROUP_NAME_ADMINISTRATOR + "'" + " OR name = '" + AkteraGroup.GROUP_NAME_MANAGER + "' OR name = '" + AkteraGroup.GROUP_NAME_USER + "'");
            currentVersion.setVersion("2.3.1");
        }
    }
