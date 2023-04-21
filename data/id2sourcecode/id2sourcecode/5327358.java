    public long importSessionPartData(String filename, String channel, boolean allowDuplicatePerson, boolean allowDuplicateSessionPart) throws UserException {
        impTxn = DB.beginTransaction();
        log.info("Started import transaction...");
        audit = new Audit();
        try {
            personDAO.setTransaction(impTxn);
            partDAO.setTransaction(impTxn);
            sessionDAO.setTransaction(impTxn);
            epochDAO.setTransaction(impTxn);
            auditDAO.setTransaction(impTxn);
            validateFilename(filename);
            file = new File(filename);
            parser = new EEGParser(file, channel);
            audit.setFilename(FilenameUtils.getName(filename));
            audit.setFileSize((int) file.length() / 1024 / 1024);
            audit.setImportStartTime(new Date());
            audit.setRecordsCount(FileUtil.getRecordsCount(file));
            audit.setSamplingRate(parser.getSamplingRate().value());
            Person p = parsePerson();
            if (!personDAO.exists(p)) {
                return importNewPersonData(p);
            }
            Person personInDB = personDAO.find(p.getFirstName(), p.getLastName());
            if (allowDuplicatePerson) {
                return importNewPersonData(p);
            }
            Date sleepStart = parser.getSessionStartDate();
            SleepSession sleepInDB = sessionDAO.findSleepSession(sleepStart, personInDB.getId());
            if (sleepInDB == null) {
                return importNewSleepSessionData(new SleepSession(sleepStart, parser.getSamplingRate(), personInDB.getId()));
            }
            Date partStart = parser.getPartStartDate();
            SessionPart sessionPart = new SessionPart(partStart, parser.getChannel(), sleepInDB.getId());
            if (!partDAO.existsSessionPart(partStart, sessionPart.getChannel(), sleepInDB.getId())) {
                return importNewSessionPartData(sessionPart);
            }
            if (allowDuplicateSessionPart) {
                return importNewSessionPartData(sessionPart);
            }
        } catch (ApplicationException e) {
            DB.abort(impTxn);
            throw e;
        }
        DB.abort(impTxn);
        throw new UserException(ErrorMessages.FILE_IMPORTED);
    }
