    private void extractUserLines() throws IOException {
        File file = null;
        PrintWriter pwriter = null;
        StringWriter stringWriter = null;
        userImports.clear();
        stringWriter = new StringWriter();
        pwriter = new PrintWriter(stringWriter);
        file = dest.getFile(apiName);
        if (file.exists()) {
            BufferedReader in = null;
            String line = null;
            boolean foundDelimiter = false;
            in = new BufferedReader(new FileReader(file));
            while ((line = in.readLine()) != null) {
                if (line.trim().startsWith("import ")) {
                    userImports.add(line.trim().substring(7, line.trim().length() - 1));
                }
                if (line.trim().equals(HANDWRITTEN_DELIMITER_LINE)) {
                    foundDelimiter = true;
                }
                if (foundDelimiter) {
                    pwriter.println(line);
                }
            }
            if (!foundDelimiter) {
                throw new IOException("The user code delimiter wasn't found.");
            }
        } else {
            pwriter.println("    " + HANDWRITTEN_DELIMITER_LINE);
            pwriter.println("    /**");
            pwriter.println("     * If you already have an existing <code>PersistentManager</code> use");
            pwriter.println("     * this create method.");
            pwriter.println("     *");
            pwriter.println("     * @param  pmanager  The <code>PersistentManager</code> to use with the");
            pwriter.println("     *                   API.");
            pwriter.println("     */");
            pwriter.println("    public static " + apiName + " create(PersistentManager pmanager) throws PersistException {");
            pwriter.println("        if (pmanager.getName() == null) {");
            pwriter.println("            pmanager.setName(\"" + apiName + "\");");
            pwriter.println("        }");
            pwriter.println("        return new " + apiName + "(pmanager);");
            pwriter.println("    }");
            pwriter.println();
            pwriter.println("    /**");
            pwriter.println("     * Create a factory that communicates with JOB via the HTTP ");
            pwriter.println("     * protocol.");
            pwriter.println("     */");
            pwriter.println("    public static " + apiName + " createInHttpMode(String url) ");
            pwriter.println("        throws PersistException {");
            pwriter.println("        PersistentManager pmanager = null;");
            pwriter.println();
            pwriter.println("        pmanager = new PersistentManager(cinfo, url, ");
            pwriter.println("                                         PersistentManager.SERVLET);");
            pwriter.println("        pmanager.setName(\"" + apiName + "\");");
            pwriter.println("        return new " + apiName + "(pmanager);");
            pwriter.println("    }");
            pwriter.println();
            pwriter.println("    /**");
            pwriter.println("     * Create a factory that communicates with JOB via a \"normal\" EJB");
            pwriter.println("     * remote interface (usually this is some variant of RMI).");
            pwriter.println("     */");
            pwriter.println("    public static " + apiName + " createInEjbMode()");
            pwriter.println("        throws PersistException {");
            pwriter.println("        PersistentManager pmanager = null;");
            pwriter.println("        initClassInfo();");
            pwriter.println("        pmanager = new PersistentManager(cinfo, DATASOURCE_JNDI_NAME, ");
            pwriter.println("                                         PersistentManager.EJB);");
            pwriter.println("        pmanager.setName(\"" + apiName + "\");");
            pwriter.println("        return new " + apiName + "(pmanager);");
            pwriter.println("    }");
            pwriter.println();
            pwriter.println("    /**");
            pwriter.println("     * Create a factory that communicates with JOB via EJB remote ");
            pwriter.println("     * interfaces.");
            pwriter.println("     *");
            pwriter.println("     * @param  jndiEnv  The JNDI environment properties.");
            pwriter.println("     */");
            pwriter.println("    public static " + apiName + " createInEjbMode(Hashtable jndiEnv)");
            pwriter.println("        throws PersistException {");
            pwriter.println("        PersistentManager pmanager = null;");
            pwriter.println("        initClassInfo();");
            pwriter.println("        pmanager = new PersistentManager(jndiEnv, DATASOURCE_JNDI_NAME, cinfo);");
            pwriter.println("        pmanager.setName(\"" + apiName + "\");");
            pwriter.println("        return new " + apiName + "(pmanager);");
            pwriter.println("    }");
            pwriter.println();
            pwriter.println("    /**");
            pwriter.println("     * Create a factory that communicates with JOB via in memory ");
            pwriter.println("     * calls.");
            pwriter.println("     *");
            pwriter.println("     */");
            pwriter.println("    public static " + apiName + " createInLocalMode()");
            pwriter.println("        throws PersistException {");
            pwriter.println("        PersistentManager pmanager = null;");
            pwriter.println("        initClassInfo();");
            pwriter.println("        pmanager = new PersistentManager(cinfo, DATASOURCE_JNDI_NAME, PersistentManager.LOCAL);");
            pwriter.println("        return new " + apiName + "(pmanager);");
            pwriter.println("    }");
            pwriter.println();
            pwriter.println("    public static " + apiName + " getApi(HttpServletRequest request) throws Exception {");
            pwriter.println("        String __API_NAME__ = \"__" + apiName + "__\";");
            pwriter.println("        " + apiName + " api = (" + apiName + ")request.getSession().getAttribute(__API_NAME__);");
            pwriter.println("        if ( api == null ) {");
            pwriter.println("            api = createInLocalMode();");
            pwriter.println("            request.getSession().setAttribute(__API_NAME__, api);");
            pwriter.println("        }");
            pwriter.println();
            pwriter.println("        return api;");
            pwriter.println("    }");
            pwriter.println();
            pwriter.println("    public PersistentManager getPersistentManager() throws PersistException {");
            pwriter.println("        return pmanager;");
            pwriter.println("    }");
            pwriter.println();
            pwriter.println("}");
        }
        userLines = stringWriter.toString();
    }
