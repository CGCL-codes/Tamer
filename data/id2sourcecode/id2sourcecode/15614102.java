    public String getEmail(A3ubLoginBucketBean_v01 bean) {
        if (bean == null) {
            return "";
        }
        String mail = ch.unibe.id.se.a3ublogin.persistence.readersandwriters.UniLdapReader.getInstance().getEmail(bean.getLoginbean().getCommonName());
        return mail;
    }
