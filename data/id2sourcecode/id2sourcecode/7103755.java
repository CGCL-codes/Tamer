    public String getEmailProposal(String cn) {
        String result = "";
        result = ch.unibe.id.se.a3ublogin.persistence.readersandwriters.UniLdapReader.getInstance().getEmail(cn);
        if (result == null) {
            result = "";
        }
        return result;
    }
