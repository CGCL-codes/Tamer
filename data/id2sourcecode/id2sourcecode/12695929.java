    @Test
    public void testConcurrency() {
        logger.info("testConcurrency");
        int count = 1000;
        Collection<Person> people = Person.generatePeople(count);
        TimeElapser te = new TimeElapser();
        final T mic = createMultiIndexContainer(people);
        logger.info("Elapsed time to add " + count + " people is " + te.end() + " ms");
        te.start();
        addIndexForBirthYear(mic);
        logger.info("Elapsed time to create index for birthYear  for " + count + " people is " + te.end() + " ms");
        te.start();
        addIndexForSex(mic);
        logger.info("Elapsed time to create index for sex for " + count + " people is " + te.end() + " ms");
        te.start();
        int minBirthYear = 1960;
        int maxBirthYear = 1980;
        boolean shouldBeMan = true;
        final Junction<Person, Integer, L> lookupRules = mic.conjunction().add(createBetweenBirthYear(mic, minBirthYear, maxBirthYear)).add(createEqSex(mic, shouldBeMan));
        int howManyThreads = 100;
        final int howManyPeopleToAdd = 1000;
        Thread[] threads = new Thread[howManyThreads];
        for (int i = 0; i < howManyThreads; i++) {
            final int y = i;
            if (y % 2 == 0) {
                threads[y] = new Thread(new Runnable() {

                    public void run() {
                        logger.info("Thread " + y + " (writer) started");
                        mic.addAll(Person.generatePeople(howManyPeopleToAdd));
                        logger.info("Thread " + y + " (writer) " + howManyPeopleToAdd + " people added");
                        logger.info("Thread " + y + " (writer) ended");
                    }
                }, "Writer-" + y);
            } else {
                threads[y] = new Thread(new Runnable() {

                    public void run() {
                        logger.info("Thread " + y + " (reader) started");
                        logger.info("Thread " + y + " (reader) ... " + mic.find(lookupRules).size() + " people found");
                        logger.info("Thread " + y + " (reader) ended");
                    }
                }, "Reader-" + y);
            }
        }
        for (int i = 0; i < howManyThreads; i++) {
            threads[i].start();
        }
        logger.info("Threads finished");
        try {
            Thread.sleep(15 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(true);
    }
