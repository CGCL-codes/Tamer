    protected void InitCustomCICSEntriesFromRules(CBaseEntityFactory factory) {
        int nb = m_RulesManager.getNbRules("environmentVariableFPac");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("environmentVariableFPac", i);
            String name = e.getVal("name");
            String read = e.getVal("methodeRead");
            String write = e.getVal("methodeWrite");
            boolean bNumeric = e.getValAsBoolean("Numeric");
            factory.NewEntityEnvironmentVariable(name, read, write, bNumeric);
        }
        nb = m_RulesManager.getNbRules("routineEmulation");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("routineEmulation", i);
            String name = e.getVal("routine");
            String method = e.getVal("method");
            String csRequiredToolsLib = e.getVal("requiredToolsLib", null);
            factory.m_ProgramCatalog.RegisterRoutineEmulation(name, method, csRequiredToolsLib);
        }
        nb = m_RulesManager.getNbRules("routineEmulationExternal");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("routineEmulation", i);
            String name = e.getVal("routine");
            String method = e.getVal("method");
            factory.m_ProgramCatalog.RegisterRoutineEmulation(name, method, true);
        }
    }
