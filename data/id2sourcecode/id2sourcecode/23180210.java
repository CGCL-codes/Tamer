        private void loadCsvData() throws RuntimeException {
            List<List<String>> data = new ArrayList<List<String>>();
            HashMap<String, Person> personHashMap = new HashMap<String, Person>();
            try {
                FileInputStream fileInputStream = new FileInputStream("database" + File.separator + "personen.csv");
                ByteArrayOutputStream byteArrayOutputStream;
                int read = fileInputStream.read();
                while (read != -1) {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    while ((read != -1) && (read != '\n')) {
                        byteArrayOutputStream.write(read);
                        read = fileInputStream.read();
                    }
                    String stringData = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                    data.add(splitCsvDataLine(stringData));
                    read = fileInputStream.read();
                }
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
            for (List<String> entry : data) {
                if ((entry.size() > 0) && (!entry.get(0).equalsIgnoreCase("\"id\""))) {
                    Transaction transaction = HibernateUtil.beginTransaction();
                    Surname surname = surnameDao.findOrCreate(StringUtil.normalizeName(entry.get(1)));
                    Firstname firstname = firstnameDao.findOrCreate(StringUtil.normalizeName(entry.get(2)));
                    Address address;
                    if ((entry.get(8).length() >= 4) && (entry.get(8).substring(1, 3).equalsIgnoreCase("be"))) {
                        address = addressDao.findOrCreate("BE", entry.get(6), entry.get(7), entry.get(3), entry.get(4), entry.get(5));
                    } else if ((entry.get(8).length() >= 4) && (entry.get(8).substring(1, 3).equalsIgnoreCase("ne"))) {
                        address = addressDao.findOrCreate("NL", entry.get(6), entry.get(7), entry.get(3), entry.get(4), entry.get(5));
                    } else if ((entry.get(8).length() >= 4) && (entry.get(8).substring(1, 3).equalsIgnoreCase("du"))) {
                        address = addressDao.findOrCreate("DE", entry.get(6), entry.get(7), entry.get(3), entry.get(4), entry.get(5));
                    } else if ((entry.get(8).length() >= 4) && (entry.get(8).substring(1, 3).equalsIgnoreCase("lu"))) {
                        address = addressDao.findOrCreate("LU", entry.get(6), entry.get(7), entry.get(3), entry.get(4), entry.get(5));
                    } else if ((entry.get(8).length() >= 4) && (entry.get(8).substring(1, 3).equalsIgnoreCase("fr"))) {
                        address = addressDao.findOrCreate("FR", entry.get(6), entry.get(7), entry.get(3), entry.get(4), entry.get(5));
                    } else {
                        address = addressDao.findOrCreate("ZZ", entry.get(6), entry.get(7), entry.get(3), entry.get(4), entry.get(5));
                    }
                    Person person = new Person();
                    person.setSurname(surname);
                    person.setAlternativeName(firstname);
                    String sexe = entry.get(31);
                    if (sexe == null) {
                        sexe = "U";
                    }
                    sexe = sexe.trim();
                    if (sexe.length() == 0) {
                        sexe = "U";
                    }
                    if (sexe.charAt(0) == '"') {
                        sexe = sexe.substring(1, sexe.length());
                    }
                    if (sexe.charAt(sexe.length() - 1) == '"') {
                        sexe = sexe.substring(0, sexe.length() - 1);
                    }
                    sexe = sexe.trim();
                    if (sexe.length() == 0) {
                        sexe = "U";
                    }
                    if (sexe.equalsIgnoreCase("f")) {
                        person.setSexe(Sexe.FEMALE);
                    } else if (sexe.equalsIgnoreCase("m")) {
                        person.setSexe(Sexe.MALE);
                    } else {
                        person.setSexe(Sexe.UNKNOWN);
                    }
                    String birth = entry.get(15);
                    if ((birth != null) && (birth.length() > 0)) {
                        String[] birthSplit = birth.split("/");
                        if ((birthSplit != null) && (birthSplit.length == 3)) {
                            int year = Integer.parseInt(birthSplit[2]);
                            int month = Integer.parseInt(birthSplit[1]);
                            int day = Integer.parseInt(birthSplit[0]);
                            if ((year > 1700) && (year < 2100)) {
                                person.setDateOfBirthYear(year);
                            }
                            person.setDateOfBirthMonth(month);
                            person.setDateOfBirthDay(day);
                        }
                    }
                    String death = entry.get(16);
                    if ((death != null) && (death.length() > 0)) {
                        String[] deathSplit = death.split("/");
                        if ((deathSplit != null) && (deathSplit.length == 3)) {
                            int year = Integer.parseInt(deathSplit[2]);
                            int month = Integer.parseInt(deathSplit[1]);
                            int day = Integer.parseInt(deathSplit[0]);
                            if ((year > 1700) && (year < 2100)) {
                                person.setDateOfDeathYear(year);
                            }
                            person.setDateOfDeathMonth(month);
                            person.setDateOfDeathDay(day);
                        }
                    }
                    if ((entry.get(9) != null) && (entry.get(9).length() > 0)) {
                        Communication communication = new Communication();
                        communication.setNumber(Long.parseLong(entry.get(9)));
                        communication.setCategory(Category.get("COMMUNICATION", "Primary Personal Phone Number"));
                        person.getCommunicationSet().add(communication);
                    }
                    if ((entry.get(10) != null) && (entry.get(10).length() > 0)) {
                        Communication communication = new Communication();
                        communication.setNumber(Long.parseLong(entry.get(10)));
                        communication.setCategory(Category.get("COMMUNICATION", "Primary Business Phone Number"));
                        person.getCommunicationSet().add(communication);
                    }
                    if ((entry.get(11) != null) && (entry.get(11).length() > 0)) {
                        Communication communication = new Communication();
                        communication.setNumber(Long.parseLong(entry.get(11)));
                        communication.setCategory(Category.get("COMMUNICATION", "Primary Personal Mobile Number"));
                        person.getCommunicationSet().add(communication);
                    }
                    if ((entry.get(12) != null) && (entry.get(12).length() > 0)) {
                        Communication communication = new Communication();
                        communication.setNumber(Long.parseLong(entry.get(12)));
                        communication.setCategory(Category.get("COMMUNICATION", "Primary Personal Fax Number"));
                        person.getCommunicationSet().add(communication);
                    }
                    if ((entry.get(13) != null) && (entry.get(13).length() > 2)) {
                        String[] emails = entry.get(13).substring(1, entry.get(13).length() - 1).split(" ");
                        boolean first = true;
                        for (String email : emails) {
                            Communication communication = new Communication();
                            communication.setString(email);
                            if (first) {
                                communication.setCategory(Category.get("COMMUNICATION", "Primary Personal E-Mail"));
                                first = false;
                            } else {
                                communication.setCategory(Category.get("COMMUNICATION", "Auxiliary Personal E-Mail"));
                            }
                            person.getCommunicationSet().add(communication);
                        }
                    }
                    if ((entry.get(14) != null) && (entry.get(14).length() > 2)) {
                        String[] emails = entry.get(14).substring(1, entry.get(14).length() - 1).split(" ");
                        boolean first = true;
                        for (String email : emails) {
                            Communication communication = new Communication();
                            communication.setString(email);
                            if (first) {
                                communication.setCategory(Category.get("COMMUNICATION", "Primary Business E-Mail"));
                                first = false;
                            } else {
                                communication.setCategory(Category.get("COMMUNICATION", "Auxiliary Business E-Mail"));
                            }
                            person.getCommunicationSet().add(communication);
                        }
                    }
                    Set<Communication> communicationSet = person.getCommunicationSet();
                    Communication communicationPrimaryPersonalAddress = new Communication();
                    communicationPrimaryPersonalAddress.setCategory(Category.get("COMMUNICATION", "Primary Personal Address"));
                    communicationPrimaryPersonalAddress.setAddress(address);
                    communicationSet.add(communicationPrimaryPersonalAddress);
                    if ((entry.get(23) != null) && (entry.get(23).length() == 1) && (entry.get(23).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Genodigde mis huwelijk Leen en Wim"));
                    }
                    if ((entry.get(24) != null) && (entry.get(24).length() == 1) && (entry.get(24).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Genodigde receptie huwelijk Leen en Wim"));
                    }
                    if ((entry.get(25) != null) && (entry.get(25).length() == 1) && (entry.get(25).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Genodigde diner huwelijk Leen en Wim"));
                    }
                    if ((entry.get(26) != null) && (entry.get(26).length() == 1) && (entry.get(26).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Uitnodiging verstuurd huwelijk Leen en Wim"));
                    }
                    if ((entry.get(27) != null) && (entry.get(27).length() == 1) && (entry.get(27).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Komt naar huwelijk Leen en Wim"));
                    }
                    if ((entry.get(27) != null) && (entry.get(27).length() == 1) && (entry.get(27).charAt(0) == '0')) {
                        person.getCategorySet().add(Category.get("PERSON", "Komt niet naar huwelijk Leen en Wim"));
                    }
                    if ((entry.get(28) != null) && (entry.get(28).length() == 1) && (entry.get(28).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Genodigde surprise party 30 jaar Els Van Hoeck"));
                    }
                    if ((entry.get(29) != null) && (entry.get(29).length() == 1) && (entry.get(29).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Krijgt een kerstkaart van ons"));
                    }
                    if ((entry.get(30) != null) && (entry.get(30).length() > 0)) {
                        String[] givennames = entry.get(30).split(" ");
                        int listIndex = 0;
                        for (String givenNameString : givennames) {
                            Firstname givenFirstname = firstnameDao.findOrCreate(StringUtil.normalizeName(givenNameString));
                            Givenname givenname = new Givenname();
                            givenname.setPerson(person);
                            givenname.setFirstname(givenFirstname);
                            givenname.setListIndex(listIndex);
                            person.getGivennameList().add(givenname);
                            listIndex++;
                        }
                    }
                    if ((entry.get(32) != null) && (entry.get(32).length() > 4)) {
                        String[] entrySplit = entry.get(32).split(": ");
                        if (entrySplit.length > 1) {
                            Category category = Category.get("PERSON", entrySplit[0].substring(1));
                            if (category != null) {
                                person.getCategorySet().add(category);
                            } else {
                                category = Category.get("PERSONEXTRAINFO", entrySplit[0].substring(1));
                                if (category != null) {
                                    ExtraInfo extraInfo = new ExtraInfo();
                                    extraInfo.setCategory(category);
                                    extraInfo.setValue(entrySplit[1].substring(0, entrySplit[1].length() - 1));
                                    person.getExtraInfoSet().add(extraInfo);
                                }
                            }
                        }
                    }
                    if ((entry.get(33) != null) && (entry.get(33).length() == 1) && (entry.get(33).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Krijgt suikerbonen van ons"));
                    }
                    if ((entry.get(34) != null) && (entry.get(34).length() == 1) && (entry.get(34).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Krijgt geboortekaart jef van ons"));
                    }
                    if ((entry.get(35) != null) && (entry.get(35).length() > 4)) {
                        String[] entrySplit = entry.get(35).split(": ");
                        if (entrySplit.length > 1) {
                            Category category = Category.get("PERSONEXTRAINFO", "Cadeau Jef");
                            if (category != null) {
                                ExtraInfo extraInfo = new ExtraInfo();
                                extraInfo.setCategory(category);
                                extraInfo.setValue(entrySplit[1].substring(0, entrySplit[1].length() - 1));
                                person.getExtraInfoSet().add(extraInfo);
                            }
                        }
                    }
                    if ((entry.get(36) != null) && (entry.get(36).length() == 1) && (entry.get(36).charAt(0) == '1')) {
                        person.getCategorySet().add(Category.get("PERSON", "Krijgt geboortekaart emma van ons"));
                    }
                    personDao.save(person);
                    transaction.commit();
                    personHashMap.put(entry.get(0), person);
                }
            }
            for (List<String> entry : data) {
                if ((entry.size() > 0) && (!entry.get(0).equalsIgnoreCase("\"id\""))) {
                    Person person = personHashMap.get(entry.get(0));
                    if ((entry.get(17) != null) && (entry.get(17).length() > 0)) {
                        person.setMother(personHashMap.get(entry.get(17)));
                    }
                    if ((entry.get(19) != null) && (entry.get(19).length() > 0)) {
                        person.setFather(personHashMap.get(entry.get(19)));
                    }
                    if ((entry.get(21) != null) && (entry.get(21).length() > 0)) {
                        person.setSpouse(personHashMap.get(entry.get(21)));
                    }
                    Transaction transaction = HibernateUtil.beginTransaction();
                    personDao.update(person);
                    transaction.commit();
                }
            }
        }
