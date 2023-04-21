    public HowToInit(String[] args) throws Exception {
        args = initialize(args);
        DatabaseTransaction transaction = getDatabase().createTransaction(getIndex());
        HowToManager howToManager = (HowToManager) transaction.getManager(HowTo.class);
        UserManager userManager = (UserManager) transaction.getManager(User.class);
        Date createdOn = new Date();
        User createdBy = userManager.create(transaction, "John Smith", "jsmith", MD5.digest(""), "jsmith@ingenta.com", createdOn, null);
        for (int i = 0; i < 100; i++) {
            getLogger().info("creating how-to #" + i);
            String title = "title " + i;
            String content = CONTENT + "<p>content " + i + "</p>";
            HowTo howto = howToManager.create(transaction, title, content, createdOn, createdBy);
        }
        transaction.commit();
    }
