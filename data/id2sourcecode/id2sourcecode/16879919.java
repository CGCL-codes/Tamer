    private void overrideTBL(DataRow rowTBLENTRIES, ContactEntry entry) throws Exception {
        if (entry.getName() == null) {
            return;
        }
        String FULLNAME = getValue(entry.getName().getFullName());
        String FIRSTNAME = getValue(entry.getName().getGivenName());
        String MIDDLENAME = getValue(entry.getName().getAdditionalName());
        String LASTNAME = getValue(entry.getName().getFamilyName());
        String NICKNAME = getValue(entry.getNickname());
        String NAMESUFFIX = getValue(entry.getName().getNameSuffix());
        if ((NullStatus.isNull(FIRSTNAME)) && (NullStatus.isNotNull(FULLNAME))) {
            if (FULLNAME.indexOf(" ") > 0) {
                FIRSTNAME = FULLNAME.substring(0, FULLNAME.indexOf(" "));
            } else {
                FIRSTNAME = FULLNAME;
            }
        }
        if ((NullStatus.isNull(LASTNAME)) && (NullStatus.isNotNull(FULLNAME))) {
            if (FULLNAME.indexOf(" ") > 0) {
                LASTNAME = FULLNAME.substring(FULLNAME.indexOf(" ") + 1);
            }
        }
        rowTBLENTRIES.set("FIRSTNAME", FIRSTNAME);
        rowTBLENTRIES.set("MIDDLENAME", MIDDLENAME);
        rowTBLENTRIES.set("LASTNAME", LASTNAME);
        rowTBLENTRIES.set("NICKNAME", NICKNAME);
        rowTBLENTRIES.set("NAMESUFFIX", NAMESUFFIX);
        int counter = 0;
        for (Email email : entry.getEmailAddresses()) {
            counter++;
            rowTBLENTRIES.set("EMAIL" + counter, email.getAddress());
            rowTBLENTRIES.set("EMAILTYPE" + counter, getRel(email.getRel()));
            if (counter == 4) {
                break;
            }
        }
        counter = 0;
        for (PhoneNumber phoneNumber : entry.getPhoneNumbers()) {
            counter++;
            rowTBLENTRIES.set("PHONE" + counter, phoneNumber.getPhoneNumber());
            rowTBLENTRIES.set("PHONETYPE" + counter, getRel(phoneNumber.getRel()));
            if (counter == 8) {
                break;
            }
        }
        counter = 0;
        for (Im im : entry.getImAddresses()) {
            counter++;
            rowTBLENTRIES.set("INST" + counter, im.getAddress());
            rowTBLENTRIES.set("INSTTYPE" + counter, getUnDashedValue(im.getProtocol()));
            if (counter == 4) {
                break;
            }
        }
        for (Website website : entry.getWebsites()) {
            if (Rel.HOME.equals(website.getRel())) {
                rowTBLENTRIES.set("PERSPAGE", website.getHref());
            } else if (Rel.WORK.equals(website.getRel())) {
                rowTBLENTRIES.set("BUSSPAGE", website.getHref());
            } else if (Rel.BLOG.equals(website.getRel())) {
                rowTBLENTRIES.set("WEBLOG", website.getHref());
            }
        }
        for (Organization organization : entry.getOrganizations()) {
            if (organization.getPrimary()) {
                if (organization.getOrgTitle() != null) {
                    rowTBLENTRIES.set("JOBTITLE", organization.getOrgTitle().getValue());
                }
                if (organization.getOrgDepartment() != null) {
                    rowTBLENTRIES.set("DEPARTMENT", organization.getOrgDepartment().getValue());
                }
            }
        }
        try {
            Link photoLink = entry.getContactPhotoLink();
            if (photoLink != null) {
                if (photoLink.getEtag() != null) {
                    GDataRequest request = service.createLinkQueryRequest(photoLink);
                    request.execute();
                    InputStream in = request.getResponseStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    for (int read = 0; (read = in.read(buffer)) != -1; out.write(buffer, 0, read)) ;
                    rowTBLENTRIES.set("PHOTO", out.toByteArray());
                    in.close();
                    request.end();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        rowTBLENTRIES.set("BIRTHDAY", Converter.asDate(getValue(entry.getBirthday())));
        for (PostalAddress postalAddress : entry.getPostalAddresses()) {
            if ("HOME".equals(getRel(postalAddress.getRel()))) {
                rowTBLENTRIES.set("HOMEADDR", getValue(postalAddress));
            } else if ("WORK".equals(getRel(postalAddress.getRel()))) {
                rowTBLENTRIES.set("WORKADDR", getValue(postalAddress));
            } else if ("OTHER".equals(getRel(postalAddress.getRel()))) {
                rowTBLENTRIES.set("OTHRADDR", getValue(postalAddress));
            }
        }
    }
