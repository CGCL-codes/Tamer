    public void postHandler() throws Exception {
        final SaveOnPost tph = new SaveOnPost();
        Table table = new Table(PFSP);
        table.setSortComparator(new FieldSetComparator(new String[] { "uid" }));
        EntityManager session = Util.getCurrentEntityManager(true);
        EntityTransaction trans = session.getTransaction();
        trans.begin();
        try {
            session.createQuery("delete Person p where p.uid = :uid").setParameter("uid", "fdn").executeUpdate();
            List<?> results = session.createQuery("select p from Person p ").setHint("org.hibernate.readOnly", true).getResultList();
            for (Iterator<?> it = results.iterator(); it.hasNext(); ) {
                Person person = (Person) it.next();
                PojoFieldSet<Person> fs = PFSP.clone();
                fs.setPojo(person);
                table.load(fs, false);
            }
            trans.commit();
        } finally {
            if (trans.isActive()) trans.rollback();
        }
        table.loaded();
        assertEquals("al", table.getCurrent().get("uid").getValue());
        table.edit();
        {
            table.getCurrent().get("uid").setValue(null);
            try {
                table.post(false, tph);
                assertTrue(false);
            } catch (InvalidFieldSetException ex) {
            }
            table.cancel();
            assertEquals("al", table.getCurrent().get("uid").getValue());
        }
        IField<String> lastName = table.edit().<String>get("lastName");
        final String newValue = "Loka".equalsIgnoreCase(lastName.getValue()) ? "Locarini" : "Loka";
        lastName.setValue(newValue);
        table.post(false, tph);
        assertFalse(table.isNew(table.getCurrent()));
        assertFalse(table.isChanged(table.getCurrent()));
        trans = session.getTransaction();
        {
            PojoFieldSet<Person> fs = table.append(PFSP);
            fs.get("uid").setText("fdn");
            fs.get("firstName").setText("Fabio");
            fs.get("lastName").setText("Di Natale");
            fs.get("age").setText("35");
            fs.get("male").setValue(true);
            fs.get("contacted").setValue(ThreeStateEnum.FALSE);
            assertNull(fs.getPojo());
            assertNull(fs.get("id").getValue());
            table.post(false, tph);
            assertNotNull(fs.getPojo().getId());
            assertNotNull(fs.get("id").getValue());
            LOGGER.info("NEWID: " + fs.get("id").getValue());
        }
        {
            assertEquals("fdn", table.getCurrent().get("uid").getValue());
            table.remove(new RemoveImmediately());
            LOGGER.info("NEWCURRENT: " + table.getCurrent().get("uid").getValue());
            assertEquals("gn", table.getCurrent().get("uid").getValue());
        }
    }
