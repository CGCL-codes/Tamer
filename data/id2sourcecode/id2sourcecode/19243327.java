    @SuppressWarnings("unchecked")
    public void saveData() throws Exception {
        Table table = new Table(PFSP);
        table.setSortComparator(new FieldSetComparator(new String[] { "uid" }));
        EntityManager session = Util.getCurrentEntityManager(true);
        EntityTransaction trans = session.getTransaction();
        trans.begin();
        try {
            List<?> results = session.createQuery("select p from Person p ").setHint("org.hibernate.readOnly", true).getResultList();
            for (Iterator<?> it = results.iterator(); it.hasNext(); ) {
                Person person = (Person) it.next();
                PojoFieldSet<Person> fs = PFSP.clone();
                fs.setPojo(person);
                table.load(fs, false);
            }
        } finally {
            if (trans.isActive()) trans.rollback();
        }
        table.loaded();
        assertEquals("al", table.getCurrent().get("uid").getValue());
        table.edit();
        {
            table.getCurrent().get("uid").setValue("");
            try {
                table.post(false);
                assertTrue(false);
            } catch (InvalidFieldSetException ex) {
            }
            table.cancel();
            assertEquals("al", table.getCurrent().get("uid").getValue());
        }
        IField<String> lastName = table.edit().<String>get("lastName");
        final String newValue = "Loka".equalsIgnoreCase(lastName.getValue()) ? "Locarini" : "Loka";
        lastName.setValue(newValue);
        table.post(false);
        assertFalse(table.isNew(table.getCurrent()));
        assertTrue(table.isChanged(table.getCurrent()));
        {
            PojoFieldSet<Person> fs = table.append(PFSP);
            fs.get("uid").setText("fdn");
            fs.get("firstName").setText("Fabio");
            fs.get("lastName").setText("Di Natale");
            fs.get("age").setText("35");
            fs.get("male").setText("true");
            fs.get("contacted").setText("TRUE");
            table.post(false);
        }
        trans = session.getTransaction();
        trans.begin();
        try {
            table.beginSaving();
            ITable<ATableFieldSet> changed = table.getChangedFieldSets();
            for (int i = 0; i < changed.getSize(); i++) {
                PojoFieldSet<Person> fs = (PojoFieldSet<Person>) changed.get(i);
                assertEquals("al", fs.getPojo().getUid());
                fs.setPojo(session.merge(fs.getPojo()));
            }
            ITable<ATableFieldSet> created = table.getNewFieldSets();
            for (int i = 0; i < created.getSize(); i++) {
                PojoFieldSet<Person> fs = (PojoFieldSet<Person>) created.get(i);
                assertEquals("fdn", fs.getPojo().getUid());
                assertNull(fs.getPojo().getId());
                assertNull(fs.get("id").getValue());
                session.createQuery("delete Person p where p.uid = :uid").setParameter("uid", "fdn").executeUpdate();
                session.persist(fs.getPojo());
                fs.setPojo(fs.getPojo());
                assertNotNull(fs.getPojo().getId());
                assertNotNull(fs.get("id").getValue());
            }
            trans.commit();
            assertNull(table.saved());
        } finally {
            if (trans.isActive()) trans.rollback();
            table.cancel();
        }
    }
