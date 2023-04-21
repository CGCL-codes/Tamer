    @Test
    public void testSpeed() {
        List<Strength> strenghts = new ArrayList<Strength>();
        strenghts.add(new Strength(settings.KICK_POWER, 10));
        strenghts.add(new Strength(settings.KICK_REACH, 10));
        strenghts.add(new Strength(settings.PUNCH_POWER, 10));
        strenghts.add(new Strength(settings.PUNCH_REACH, 10));
        Personality p = new Personality(strenghts);
        int weight = (10 + 10) / 2;
        int height = (10 + 10) / 2;
        float speed = ((height - weight) / 2);
        assertEquals(p.getSpeed(), speed);
    }
