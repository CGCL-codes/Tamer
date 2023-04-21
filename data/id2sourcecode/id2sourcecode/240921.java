    @ThreadedTest
    public void runUserCacheMultiThreaded() throws Exception {
        createCache();
        resetMocks();
        db.update(USER);
        expect(db.getAvatar(USER)).andReturn(AVATAR2);
        context.draw(AVATAR2);
        replay(db, context);
        cache.updateUser();
        final Script<UserCache> main = new Script<UserCache>(cache);
        final Script<UserCache> second = new Script<UserCache>(main);
        final UserCache control = main.object();
        final UserDb dbTarget = main.createTarget(UserDb.class);
        final RenderingContext contextTarget = main.createTarget(RenderingContext.class);
        final ReadWriteLock lockTarget = main.createTarget(ReadWriteLock.class);
        control.updateCache();
        main.inLastMethod().beforeCalling(dbTarget.getAvatar("")).releaseTo(second);
        control.drawUserAvatar();
        main.inLastMethod();
        contextTarget.draw("");
        main.beforeCallingLastMethod().releaseTo(second);
        main.addTask(new ScriptedTask<UserCache>() {

            @Override
            public void execute() {
                cache.drawUserAvatar();
            }
        });
        second.addTask(new ScriptedTask<UserCache>() {

            @Override
            public void execute() {
                ReentrantReadWriteLock lock = cache.rwl;
                System.out.printf("First release - write = %s, num readers = %d\n", lock.isWriteLocked(), lock.getReadLockCount());
                releaseTo(main);
                System.out.printf("Second release - write = %s, num readers = %d\n", lock.isWriteLocked(), lock.getReadLockCount());
                releaseTo(main);
            }
        });
        new Scripter<UserCache>(main, second).execute();
    }
