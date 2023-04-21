    @Override
    public void run() {
        try {
            this.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long t = 0;
        while (state) {
            initWorkSpace();
            Monitor.getI().add("system", Monitor.RAM, new MemoryBean());
            Monitor.getI().add(getName(), Monitor.QUEUE, new LongBean(Base.getI().getRunnerSize(this)));
            while (jobslist.hasMoreElements()) {
                job = jobslist.nextElement();
                if (job != null) {
                    try {
                        t = System.currentTimeMillis() - job.getLastActivete();
                    } catch (Exception e) {
                        System.out.println("ERROR: " + job.getName());
                        System.out.println("FREE MEMORY" + Runtime.getRuntime().freeMemory());
                        System.out.println("TOTAL MEMORY" + Runtime.getRuntime().totalMemory());
                        System.out.println("MAX MEMORY" + Runtime.getRuntime().maxMemory());
                        e.printStackTrace();
                    }
                    if (t > property_jobsleeptime) {
                        actualJob = job.getId();
                        Base.getI().writeServiceLogg("job action=\"start\" state=\"" + job.getStatus() + "\" running=\"" + job.getName() + "\" thread=\"" + getName() + "\"", 1);
                        try {
                            job.runJobAction(this);
                        } catch (Exception e) {
                            System.out.println("ERROR: " + job.getName());
                            System.out.println("FREE MEMORY" + Runtime.getRuntime().freeMemory());
                            System.out.println("TOTAL MEMORY" + Runtime.getRuntime().totalMemory());
                            System.out.println("MAX MEMORY" + Runtime.getRuntime().maxMemory());
                            e.printStackTrace();
                        }
                        Base.getI().writeServiceLogg("job action=\"stop\" state=\"" + job.getStatus() + "\" running=\"" + job.getName() + "\" thread=\"" + getName() + "\"", 1);
                        worked++;
                        actualJob = "";
                    }
                }
            }
            if (worked == 0) {
                Monitor.getI().add(getName(), Monitor.INFO, new StringBean("sleep:" + property_noworkjoblistsleeptime + "ms"));
                try {
                    sleep(property_noworkjoblistsleeptime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
