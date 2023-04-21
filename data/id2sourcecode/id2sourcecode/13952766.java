    private void InitCustomCICSEntriesFromRules(CBaseEntityFactory factory) {
        int nb = m_RulesManager.getNbRules("ignoreEntity");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("ignoreEntity", i);
            String name = e.getVal("name");
            factory.NewIgnoreEntity(name);
        }
        nb = m_RulesManager.getNbRules("environmentVariable");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("environmentVariable", i);
            String name = e.getVal("name");
            String read = e.getVal("methodeRead");
            String write = e.getVal("methodeWrite");
            boolean bNumeric = e.getValAsBoolean("Numeric");
            factory.NewEntityEnvironmentVariable(name, read, write, bNumeric);
        }
        nb = m_RulesManager.getNbRules("keyPressed");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("keyPressed", i);
            String key = e.getVal("keyName");
            String alias = e.getVal("CICSAlias");
            factory.NewEntityKeyPressed(alias, key);
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
            Tag e = m_RulesManager.getRule("routineEmulationExternal", i);
            String name = e.getVal("routine");
            String method = e.getVal("method");
            factory.m_ProgramCatalog.RegisterRoutineEmulation(name, method, true);
        }
        nb = m_RulesManager.getNbRules("NoExportResource");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("NoExportResource", i);
            String name = e.getVal("program");
            m_cat.RegisterNotExportingResource(name);
        }
        nb = m_RulesManager.getNbRules("SpecialConstantValue");
        for (int i = 0; i < nb; i++) {
            Tag e = m_RulesManager.getRule("SpecialConstantValue", i);
            String value = e.getVal("value");
            String constant = e.getVal("constant");
            char[] arr = value.toCharArray();
            char[] b = new char[arr.length / 2];
            for (int j = 0; j < arr.length; j += 2) {
                String s = "" + arr[j] + arr[j + 1];
                Integer in = Integer.valueOf(s, 16);
                b[j / 2] = (char) in.intValue();
            }
            String text = new String(b);
            factory.addSpecialConstantValue(text, constant);
        }
    }
