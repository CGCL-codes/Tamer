                    public void run() {
                        logger.info("Thread " + y + " (writer) started");
                        mic.addAll(Person.generatePeople(howManyPeopleToAdd));
                        logger.info("Thread " + y + " (writer) " + howManyPeopleToAdd + " people added");
                        logger.info("Thread " + y + " (writer) ended");
                    }
