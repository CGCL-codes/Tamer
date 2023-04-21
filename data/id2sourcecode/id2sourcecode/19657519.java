    @Override
    public User register(String emailAddress, String password, String fullName) throws NoSuchGroupException, AlreadyRegisteredException {
        logger.info("Registering user: " + emailAddress);
        User user = userFacade.findByEmail(emailAddress);
        if (user != null) {
            throw new AlreadyRegisteredException(user);
        }
        Group group = getGroup(USERGROUP);
        if (group == null) {
            group = groupFacade.create(USERGROUP);
            groupFacade.create(ADMINGROUP);
        }
        String digestedPassword = TextUtil.digest(password);
        if (digestedPassword == null) {
            throw new RuntimeException();
        }
        String digestedActivationKey = TextUtil.digest(emailAddress + ":" + fullName + ":" + TextUtil.getRandomString(10));
        if (digestedActivationKey == null) {
            throw new RuntimeException();
        }
        user = userFacade.createUser(emailAddress, digestedPassword, fullName, group, digestedActivationKey);
        Set<ImageSet> sets = user.getImageSets();
        if (sets == null) {
            sets = new HashSet<ImageSet>();
        }
        ImageSet set = imageSetFacade.createImageSet(user, ImageSetFacadeLocal.DEFAULT, ImageSetFacadeLocal.DEFAULT);
        sets.add(set);
        user.setImageSets(sets);
        userFacade.modify(user);
        logger.info("User registered: " + emailAddress);
        return user;
    }
