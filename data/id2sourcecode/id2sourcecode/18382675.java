    private void insertLogLine(String logLine) throws NoSuchAlgorithmException, SQLException {
        LOG.finest("The passwordDigest is: '" + authn.getPassword() + "'");
        int digestIndex = logLine.lastIndexOf(FIELD_SEP) + FIELD_SEP.length();
        String logLineDigest = logLine.substring(digestIndex);
        LOG.finest("The logLineDigest is: '" + logLineDigest + "'");
        String restOfLogLine = logLine.substring(0, digestIndex);
        restOfLogLine += authn.getPassword();
        LOG.finest("The string to digest is: '" + restOfLogLine + "'");
        LOG.finest("The length of the string to digest is: '" + restOfLogLine.length() + "'");
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        byte[] digestBytes = md5Digest.digest(restOfLogLine.getBytes());
        String localLogLineDigest = Base64.encodeBytes(digestBytes);
        LOG.finest("The digest of the previous string is: '" + localLogLineDigest + "'");
        LOG.finest("The resultant string is: '" + logLine.substring(0, digestIndex) + localLogLineDigest + "'");
        LOG.finest("The length of the resultant string is: '" + (logLine.substring(0, digestIndex) + localLogLineDigest).length() + "'");
        boolean validDigest = true;
        if (!logLineDigest.equals(localLogLineDigest)) {
            IllegalArgumentException ex = new IllegalArgumentException(file.getAbsolutePath() + ": the two MD5 digests do not match-> file:'" + logLineDigest + "' and calculated:'" + localLogLineDigest + "'");
            LOG.severe(ex.getMessage());
            ex.printStackTrace();
            validDigest = false;
        }
        String[] logLineParts = logLine.split(FIELD_SEP);
        if ("SYSTEM_ID".equals(logLineParts[0])) {
            LOG.finest("Need to insert a SYSTEM_ID record: " + logLine + "'");
            Version version = new Version();
            version.setName(logLineParts[2]);
            Object rowExists = DBClient.orm.queryForObject("findVersionByName", version);
            if (rowExists == null) {
                long versionRowKey = DBClient.nextRowKey("Version");
                version.setId(versionRowKey);
                DBClient.orm.insert("insertVersion", version);
            } else {
                version = (Version) rowExists;
            }
            Computer computer = new Computer();
            computer.setComputerName(logLineParts[3]);
            computer.setComputerUsername(logLineParts[4]);
            computer.setMacAddress(logLineParts[5]);
            computer.setIpAddress(logLineParts[6]);
            computer.setUserId(authn.getUserId());
            rowExists = DBClient.orm.queryForObject("findUniqueComputer", computer);
            if (rowExists == null) {
                long computerRowKey = DBClient.nextRowKey("Computer");
                computer.setId(computerRowKey);
                DBClient.orm.insert("insertComputer", computer);
            } else {
                computer = (Computer) rowExists;
            }
            Timezone timezone = new Timezone();
            timezone.setName(logLineParts[8]);
            timezone.setDaylightName(logLineParts[9]);
            timezone.setGmtOffset(null);
            timezone.setDaylightGMTOffset(null);
            rowExists = DBClient.orm.queryForObject("findTimezoneByName", timezone);
            if (rowExists == null) {
                long timezoneRowKey = DBClient.nextRowKey("Timezone");
                timezone.setId(timezoneRowKey);
                DBClient.orm.insert("insertTimezone", timezone);
            } else {
                timezone = (Timezone) rowExists;
            }
            SystemId systemId = (SystemId) DBClient.orm.queryForObject("findSystemIdByMD5Digest", logLineParts[11]);
            if (systemId == null) {
                systemId = new SystemId();
                long systemIdRowKey = DBClient.nextRowKey("SystemId");
                LOG.finest("The systemIdRowKey is: " + systemIdRowKey);
                systemId.setId(systemIdRowKey);
                systemId.setVersionId(version.getId());
                systemId.setComputerId(computer.getId());
                systemId.setTimezoneId(timezone.getId());
                systemId.setMd5Digest(logLineParts[11]);
                systemId.setValidDigest(validDigest);
                systemId.setProcessId(Long.parseLong(logLineParts[10]));
                systemId.setStartTime(new Date(Long.parseLong(logLineParts[1])));
                DBClient.orm.insert("insertSystemId", systemId);
                currentSystemId = systemIdRowKey;
            } else {
                currentSystemId = systemId.getId();
            }
        } else if ("IDLE".equals(logLineParts[0])) {
            LOG.finest("Need to insert an IDLE record: " + logLine + "'");
            if (currentSystemId < 0) {
                currentSystemId = getCurrentSystemId();
            }
            Idle idle = (Idle) DBClient.orm.queryForObject("findIdleByMd5Digest", logLineParts[4]);
            if (idle == null) {
                Category category = (Category) DBClient.orm.queryForObject("findUniqueCategory", logLineParts[3]);
                if (category == null) {
                    category = new Category();
                    long categoryRowKey = DBClient.nextRowKey("Category");
                    category.setId(categoryRowKey);
                    category.setName(logLineParts[3]);
                    DBClient.orm.insert("insertCategory", category);
                }
                idle = new Idle();
                long idleRowKey = DBClient.nextRowKey("Idle");
                idle.setId(idleRowKey);
                idle.setStartTime(new Date(Long.parseLong(logLineParts[1])));
                idle.setEndTime(new Date(Long.parseLong(logLineParts[2])));
                idle.setCategoryId(category.getId());
                idle.setMd5Digest(logLineParts[4]);
                idle.setValidDigest(validDigest);
                idle.setSystemId(currentSystemId);
                DBClient.orm.insert("insertIdle", idle);
            }
        } else {
            LOG.finest("Need to insert an application record: '" + logLine + "'");
            if (currentSystemId < 0) {
                currentSystemId = getCurrentSystemId();
            }
            BeenDoin beendoin = (BeenDoin) DBClient.orm.queryForObject("findBeenDoinByMd5Digest", logLineParts[7]);
            if (beendoin == null) {
                Category category = (Category) DBClient.orm.queryForObject("findUniqueCategory", logLineParts[6]);
                if (category == null) {
                    category = new Category();
                    long categoryRowKey = DBClient.nextRowKey("Category");
                    category.setId(categoryRowKey);
                    category.setName(logLineParts[6]);
                    DBClient.orm.insert("insertCategory", category);
                }
                Application app = new Application();
                app.setName(logLineParts[0]);
                app.setWindowType(logLineParts[1]);
                Object rowExists = DBClient.orm.queryForObject("findAppByNameAndType", app);
                if (rowExists == null) {
                    long appRowKey = DBClient.nextRowKey("Application");
                    app.setId(appRowKey);
                    DBClient.orm.insert("insertApplication", app);
                } else {
                    app = (Application) rowExists;
                }
                beendoin = new BeenDoin();
                long beenDoinRowKey = DBClient.nextRowKey("BeenDoin");
                beendoin.setId(beenDoinRowKey);
                beendoin.setStartTime(new Date(Long.parseLong(logLineParts[4])));
                beendoin.setEndTime(new Date(Long.parseLong(logLineParts[5])));
                beendoin.setCategoryId(category.getId());
                beendoin.setAppId(app.getId());
                beendoin.setMd5Digest(logLineParts[7]);
                beendoin.setValidDigest(validDigest);
                beendoin.setSystemId(currentSystemId);
                beendoin.setWindowTitle(logLineParts[2]);
                beendoin.setProcessId(Long.parseLong(logLineParts[3]));
                DBClient.orm.insert("insertBeenDoin", beendoin);
            }
        }
    }
