    public void testTempTableGenerationIsolation() throws Throwable {
        Session s = openSession();
        s.beginTransaction();
        Truck truck = new Truck();
        truck.setVin("123t");
        truck.setOwner("Steve");
        s.save(truck);
        s.flush();
        s.createQuery("delete from Vehicle").executeUpdate();
        s.getTransaction().rollback();
        s.close();
        s = openSession();
        s.beginTransaction();
        List list = s.createQuery("from Car").list();
        assertEquals("temp table gen caused premature commit", 0, list.size());
        s.createQuery("delete from Car").executeUpdate();
        s.getTransaction().rollback();
        s.close();
    }
