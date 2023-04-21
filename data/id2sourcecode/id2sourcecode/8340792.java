    public String getEmailFromCnOutOfLdap(String cn) {
        return ch.unibe.id.se.a3ublogin.persistence.readersandwriters.UniLdapReader.getInstance().getEmail(cn);
    }
