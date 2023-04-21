    private static boolean authenticate(final String resource, final String method, final String username, final String password) throws NamingException {
        if (username == null || password == null || username.length() < 1 || password.length() < 1) {
            throw new AuthenticationException("No authentication available");
        }
        DirContext initialDirContext = null;
        final Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, properties.getProperty(CONTEXT_FACTORY));
        env.put(Context.PROVIDER_URL, properties.getProperty(PROVIDER_URL));
        initialDirContext = new InitialDirContext(env);
        boolean writer = false;
        boolean reader = false;
        final String dn = properties.getProperty(USER_ATTRIBUTE) + "=" + username + "," + properties.getProperty(USER_OU);
        initialDirContext.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
        initialDirContext.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
        try {
            final String adminDN = properties.getProperty(ADMINISTRATOR_GROUP);
            final Attribute admins = initialDirContext.getAttributes(adminDN, new String[] { properties.getProperty(MEMBER) }).get(properties.getProperty(MEMBER));
            if (admins != null && admins.contains(username)) {
                log.debug("User is an admin");
                return true;
            }
            String baseDN = properties.getProperty(CALENDAR_OU);
            BasicAttributes searchTerm = new BasicAttributes(properties.getProperty(ICS), resource);
            String[] attributesToRetrieve = { properties.getProperty(READER), properties.getProperty(WRITER), properties.getProperty(OWNER_REF), properties.getProperty(TYPE) };
            NamingEnumeration searchResults = initialDirContext.search(baseDN, searchTerm, attributesToRetrieve);
            if (!searchResults.hasMoreElements()) {
                log.debug("No calendars found; search for the timesheet entry with this url");
                baseDN = properties.getProperty(TIMESHEET_OU);
                searchResults = initialDirContext.search(baseDN, searchTerm, attributesToRetrieve);
            }
            if (!searchResults.hasMoreElements()) {
                log.debug("no Calendars, no timesheets, perhaps OpsDocument?");
                int index = 0;
                for (int i = 0; i < 6; i++) {
                    index = resource.indexOf("/", index) + 1;
                    if (index == 0) {
                        return false;
                    }
                }
                baseDN = properties.getProperty(PROJECT_OU);
                final String baseURL = resource.substring(0, index);
                searchTerm = new BasicAttributes(properties.getProperty(OPS_DOCUMENT_PATH), baseURL);
                attributesToRetrieve = new String[] { properties.getProperty(MEMBER), properties.getProperty(PROJECT_MANAGER) };
                searchResults = initialDirContext.search(baseDN, searchTerm, attributesToRetrieve);
            }
            if (searchResults.hasMoreElements()) {
                final Attributes attributes = ((SearchResult) searchResults.nextElement()).getAttributes();
                String owner = null;
                String type = null;
                if (attributes.get(properties.getProperty(OWNER_REF)) != null) {
                    owner = (String) attributes.get(properties.getProperty(OWNER_REF)).get();
                }
                final Attribute readers = attributes.get(properties.getProperty(READER));
                final Attribute writers = attributes.get(properties.getProperty(WRITER));
                if (attributes.get(properties.getProperty(TYPE)) != null) {
                    type = (String) attributes.get(properties.getProperty(TYPE)).get();
                }
                final Attribute members = attributes.get(properties.getProperty(MEMBER));
                final Attribute projectManager = attributes.get(properties.getProperty(PROJECT_MANAGER));
                if ((owner != null && owner.equals(username)) || (writers != null && writers.contains(username)) || (members != null && members.contains(username)) || (projectManager != null && projectManager.contains(username))) {
                    log.debug("The user is either a owner or writer of the timesheet/calendar " + "or a member of the project of wich he/she requests a OpsDocument resource");
                    if ((type != null && type.equals(properties.get(CURRENT_TYPE))) || type == null) {
                        log.debug("either type is current or this isn'nt a calendar/timesheet");
                        writer = true;
                    } else {
                        log.debug("the user is a reader");
                        reader = true;
                    }
                } else if (readers != null && readers.contains(username)) {
                    log.debug("the user is a reader");
                    reader = true;
                }
            } else {
                return false;
            }
            if (!writer && !reader) {
                return false;
            } else if (!writer && reader && !method.equalsIgnoreCase(GET) && !method.equalsIgnoreCase("PROPFIND")) {
                return false;
            }
        } catch (final AuthenticationException e) {
            log.debug("Could not authenticate user: " + e);
            return false;
        } finally {
            if (initialDirContext != null) {
                initialDirContext.removeFromEnvironment(Context.SECURITY_PRINCIPAL);
                initialDirContext.removeFromEnvironment(Context.SECURITY_CREDENTIALS);
                initialDirContext.close();
            }
        }
        return true;
    }
