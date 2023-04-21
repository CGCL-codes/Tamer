    @Test
    public void testToXMLAndBack() {
        System.out.println("toXMLAndBack");
        XMLBuilder xmlb = new XMLBuilder();
        DatabaseHandle database = new DatabaseHandle(pio.features);
        database.writeDatabase(dbRewritePath);
        database.readDatabase(dbRewritePath);
        assertTrue(database.featuresEquals(pio.features));
        UserHandle user = new UserHandle(pio.sq, pio.p);
        user.writeUser(userRewritePath);
        user.readUser(userRewritePath);
        assertTrue(pio.p.equals(user.getUser()));
    }
