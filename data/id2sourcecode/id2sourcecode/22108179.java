    void run() throws MigrationException {
        final MigrationTarget target = migration.getTarget();
        final MigrationSource source = migration.getSource();
        try {
            target.setAutoCommit(slice == 1);
            logger.info(name + ": autoCommit:" + (slice == 1));
            rownum = 0;
            row = new Object[this.fields.size()];
            skipped = 0;
            readErrors = 0;
            writeErrors = 0;
            inserted = 0;
            initVariables();
            if (source instanceof CallbackMigrationSource) {
                ((CallbackMigrationSource) source).startParse();
            } else if (target instanceof CallbackMigrationTarget) {
                ((CallbackMigrationTarget) target).startMigration();
            } else {
                while (step()) {
                }
            }
            if (slice != 1) {
                try {
                    target.commit(stepLog);
                } catch (MigrationException e) {
                    logger.severe("COMMIT ROWS(" + inserted + ")");
                    throw e;
                }
            }
            logger.info(name + ": Inserted rows: " + (inserted) + (where != null ? ". Skipped by where condition: " + skipped : "") + (readErrors > 0 ? ". Skipped by errors in MigrationSource:" + readErrors : "") + (writeErrors > 0 ? ". Skipped by errors in MigrationTarget:" + writeErrors : ""));
        } catch (MigrationException e) {
            e.setStepName(name);
            e.setRowInSource(rownum + 1);
            throw e;
        }
    }
