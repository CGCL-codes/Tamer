    public static Agent createRandomAgent(final World world) {
        Position anywhere = world.getPlaces().get(0).getPos();
        Agent a = new Agent(anywhere, "HumanGreen", world);
        boolean hasCar = false;
        if (rand.nextFloat() < PROB_HAS_CAR) {
            hasCar = true;
        }
        int age = MIN_AGE + rand.nextInt(MAX_AGE - MIN_AGE);
        EasyTime workStart = new EasyTime(AVG_WORK_START, 0);
        EasyTime sleepEnd = new EasyTime(workStart).shift(-1, 0);
        EasyTime sleepStart = new EasyTime(sleepEnd).shift(-AVG_SLEEP_TIME, 0);
        workStart.blur(TWO_HOUR_BLUR);
        sleepEnd.blur(HALF_HOUR_BLUR);
        sleepStart.blur(TWO_HOUR_BLUR);
        a.set(AGE, new IntegerNumber(age));
        a.set(CUISINE, getRandomType(CUISINE_TYPES));
        a.set(LANGUAGE, getRandomType(LANGUAGE_TYPES));
        a.set(GENDER, getRandomType(GENDER_TYPES));
        a.set(PARTY_ANIMAL, getRandomType(PARTY_ANIMAL_TYPES));
        a.set(WORKAHOLIC, getRandomType(WORKAHOLIC_TYPES));
        a.set(HAS_CAR, new BooleanType(hasCar));
        a.set(ACTIVITY, Activity.ASLEEP);
        a.set(WORK_START, workStart);
        a.set(WORK_END, new EasyTime(workStart).shift(AVG_WORK_TIME, 0));
        a.set(SLEEP_PERIOD, new TimePeriod(sleepStart, sleepEnd));
        try {
            a.set(HOME, world.getRandomPlaceOfType("Homes"));
        } catch (PlaceNotFoundException e) {
            throw new RuntimeException("Can't find any homes Places. Did u create them?");
        }
        try {
            a.set(OFFICE, world.getRandomPlaceOfType("Offices"));
        } catch (PlaceNotFoundException e) {
            throw new RuntimeException("Can't find any offices. Did u create them?");
        }
        a.setPos(((Place) a.get(HOME)).getPos());
        return a;
    }
