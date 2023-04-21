    private void prepareForKeyTable() throws KETLThreadException {
        String template = null;
        synchronized (this.mKeySource) {
            try {
                Statement mStmt = this.mcDBConnection.createStatement();
                template = this.getStepTemplate(this.getGroup(), "CHECKFORKEYTABLE", true);
                template = EngineConstants.replaceParameterV2(template, "TABLENAME", this.mstrKeyTableName);
                template = EngineConstants.replaceParameterV2(template, "SCHEMANAME", this.mstrSchemaName);
                boolean exists = false;
                try {
                    ResultSet rs = mStmt.executeQuery(template);
                    while (rs.next()) {
                        exists = true;
                    }
                    rs.close();
                } catch (Exception e) {
                    mStmt.getConnection().rollback();
                }
                if (exists == false) {
                    template = this.getStepTemplate(this.getGroup(), "CREATEKEYTABLE", true);
                    template = EngineConstants.replaceParameterV2(template, "KEYTABLENAME", this.mstrKeyTableName);
                    template = EngineConstants.replaceParameterV2(template, "TABLENAME", this.mstrTableName);
                    template = EngineConstants.replaceParameterV2(template, "SCHEMANAME", this.mstrSchemaName);
                    template = EngineConstants.replaceParameterV2(template, "PK_COLUMNS", this.mstrPrimaryKeyColumns);
                    template = EngineConstants.replaceParameterV2(template, "SK_COLUMNS", this.mstrSourceKeyColumns);
                    mStmt.executeUpdate(template);
                    template = this.getStepTemplate(this.getGroup(), "CREATEKEYTABLEPKINDEX", true);
                    template = EngineConstants.replaceParameterV2(template, "COLUMNS", this.mstrPrimaryKeyColumns);
                    template = EngineConstants.replaceParameterV2(template, "TABLENAME", this.mstrKeyTableName);
                    template = EngineConstants.replaceParameterV2(template, "SCHEMANAME", this.mstrSchemaName);
                    mStmt.executeUpdate(template);
                    template = this.getStepTemplate(this.getGroup(), "CREATEKEYTABLESKINDEX", true);
                    template = EngineConstants.replaceParameterV2(template, "COLUMNS", this.mstrSourceKeyColumns);
                    template = EngineConstants.replaceParameterV2(template, "TABLENAME", this.mstrKeyTableName);
                    template = EngineConstants.replaceParameterV2(template, "SCHEMANAME", this.mstrSchemaName);
                    mStmt.executeUpdate(template);
                }
            } catch (SQLException e) {
                throw new KETLThreadException("Error executing statement " + template, e, this);
            }
        }
    }
