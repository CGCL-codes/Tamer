    private FoafomaticState checkFoafRegistration(FieldValueWrapperMap reqparam, Map<String, Object> mapPerson, PersonWrapper wperson) {
        String password_sha1sum = reqparam.getValueS("password_sha1sum");
        String mbox = reqparam.getValueS("mbox");
        String mboxsha1sum = Sha1sum.getInstance().calc((mbox.startsWith("mailto:") ? mbox : ("mailto:" + mbox)));
        FileItem fiFoafFile = reqparam.getValueFI("foaffile");
        String foafurl = reqparam.getValueS("foafurl");
        Person pPerson = null;
        if (PersonFactory.isPersonRegistered(null, mboxsha1sum)) {
            return USER_EXISTS;
        }
        if (fiFoafFile != null) {
            try {
                java.io.InputStream isFoafFile = fiFoafFile.getInputStream();
                PersonFactory.readXml(mbox, isFoafFile);
                isFoafFile.close();
            } catch (IOException ioex) {
                log.log(Level.WARNING, "An error occured when loading foaffile", ioex);
            } catch (Exception peex) {
                log.log(Level.INFO, "An error occured when loading foaffile", peex);
                return USER_ERRORREG;
            }
        } else if (foafurl != null && !"".equals(foafurl)) {
            try {
                java.net.URL url = new java.net.URL(foafurl);
                java.io.InputStream isFoafUrl = url.openStream();
                PersonFactory.readXml(mbox, isFoafUrl);
                isFoafUrl.close();
            } catch (java.net.MalformedURLException muex) {
                log.log(Level.INFO, "An error occured when registering new user with FOAF file", muex);
            } catch (java.io.IOException ioex) {
                log.log(Level.INFO, "An error occured when registering new user with FOAF file", ioex);
            } catch (Exception peex) {
                log.log(Level.INFO, "An error occured when loading FOAF file", peex);
                return USER_ERRORREG;
            }
        } else {
            String foaftext = reqparam.get("foaftext").getValueS();
            try {
                PersonFactory.readXml(mbox, foaftext);
            } catch (Exception peex) {
                log.log(Level.INFO, "An error occured when loading foaftext", peex);
                return USER_ERRORREG;
            }
        }
        pPerson = PersonFactory.getPersonIfExists(mbox, mboxsha1sum);
        if (pPerson != null) {
            pPerson.setPassword_sha1sum(password_sha1sum);
            if (pPerson.getMbox() == null) {
                wperson.setMbox(mbox);
            }
            createRegistrationFoaf(wperson, pPerson);
            pPerson.setTemporalRegcode(wperson.getRegcode());
            return USER_SUCCESSREG1;
        }
        return USER_NOTFOUNDREG;
    }
