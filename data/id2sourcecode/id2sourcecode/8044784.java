    private Map<db.Channel, Set<Presence>> executor_prepareAllClassrooms(final VoidCallback callback) {
        try {
            Session s = begin();
            List classrooms = getAll(s, db.Channel.class);
            final Map<db.Channel, Set<Presence>> classroomMap = new FastMap<db.Channel, Set<Presence>>();
            for (Object o : classrooms) {
                db.Channel c = (db.Channel) o;
                Set<Presence> presenceSet = new HashSet<Presence>();
                for (db.Account a : c.getMembers()) {
                    presenceSet.add(new Presence(a.getAccountName(), false));
                }
                classroomMap.put(c, presenceSet);
                logger.info(String.format("loaded '%s' from db", c.getChannelName()));
            }
            commit(s);
            return classroomMap;
        } catch (final Exception ex) {
            taskScheduler.scheduleTask(new TransactionRunner(new AbstractKernelRunnable() {

                public void run() throws Exception {
                    if (callback != null) {
                        callback.exceptionThrown(ex);
                    }
                }
            }), taskOwner);
            return null;
        }
    }
