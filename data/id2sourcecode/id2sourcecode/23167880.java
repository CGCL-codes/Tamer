    @Override
    public User createUser(User aUser) throws ServiceException, UserAlreadyExistsException {
        if (aUser.getLogin().isEmpty() || aUser.getNachname().isEmpty() || aUser.getVorname().isEmpty() || aUser.getOU() == null || aUser.getOU().getName().isEmpty() || aUser.getPasswort().isEmpty()) {
            throw new IllegalArgumentException("User muss komplett gefüllt sein!");
        }
        if (!aUser.getOU().isPersisted()) {
            throw new NotPersistentLDAPModelException(OrganizationalUnit.class);
        }
        Attributes container = new BasicAttributes();
        container.put(new UserAttributeClass());
        container.put(new BasicAttribute("sAMAccountName", aUser.getLogin()));
        container.put(new BasicAttribute("userPrincipalName", aUser.getLogin() + "@" + LDAPContext.DOMAIN_NAME));
        container.put(new BasicAttribute("uid", aUser.getLogin()));
        String md5PasswordHash = "";
        try {
            md5PasswordHash = new String(MessageDigest.getInstance("MD5").digest(aUser.getPasswort().getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e1) {
            throw new IllegalStateException(e1);
        } catch (UnsupportedEncodingException e1) {
            throw new IllegalStateException(e1);
        }
        container.put(new BasicAttribute("userPassword", "{MD5}" + md5PasswordHash));
        container.put(new BasicAttribute("givenName", aUser.getVorname()));
        container.put(new BasicAttribute("sn", aUser.getNachname()));
        container.put(new BasicAttribute("cn", aUser.getVorname() + " " + aUser.getNachname()));
        container.put(new BasicAttribute("userAccountControl", "512"));
        container.put(new BasicAttribute("homeDrive", "H:"));
        container.put(new BasicAttribute("homeDirectory", "\\\\S215-03\\SchülerHomeDir$\\" + aUser.getLogin()));
        aUser.setDistinguishedName(generateUserDN(aUser, aUser.getOU()));
        try {
            LDAP.createSubcontext(aUser.getDistinguishedName(), container);
        } catch (AuthenticationException e) {
            throw new UserAlreadyExistsException();
        } catch (NamingException e) {
            throw new IllegalStateException("Fehler! Benutzer konnte nicht angelegt werden!", e);
        }
        aUser.setPersisted(true);
        return aUser;
    }
