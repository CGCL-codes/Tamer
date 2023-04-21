    public void testMixedOrdering() throws Exception {
        Persister persister = new Persister();
        StringWriter writer = new StringWriter();
        Person person = new Person();
        person.setFirst("Jack");
        person.setSurname("Daniels");
        person.setNickname("JD");
        person.setDate("19/01/1912");
        persister.write(person, writer);
        Person recovered = persister.read(Person.class, writer.toString());
        assertEquals(recovered.getFirst(), person.getFirst());
        assertEquals(recovered.getSurname(), person.getSurname());
        assertEquals(recovered.getNickname(), person.getNickname());
        assertEquals(recovered.getDate(), person.getDate());
        validate(person, persister);
        assertElementHasValue(writer.toString(), "/person/name[1]/first", "Jack");
        assertElementHasValue(writer.toString(), "/person/name[1]/surname", "Daniels");
        assertElementHasValue(writer.toString(), "/person/name[2]/nickname", "JD");
        assertElementHasValue(writer.toString(), "/person/age[1]/date", "19/01/1912");
    }
