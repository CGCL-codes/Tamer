        public boolean runCommand(String name, int priority, Object[] command, Object[] envArray, String workingDirectory, Object[] depends, Object[] readLocks, Object[] writeLocks) {
            Map<String, String> env = new TreeMap<String, String>();
            for (Object x : envArray) {
                Object[] o = (Object[]) x;
                if (o.length != 2) throw new RuntimeException();
                env.put((String) o[0], (String) o[1]);
            }
            logger.info("Running command %s [%s] (priority %d); read=%s, write=%s; environment={%s}", name, Arrays.toString(command), priority, Arrays.toString(readLocks), Arrays.toString(writeLocks), Output.toString(", ", env.entrySet()));
            String[] commandArgs = new String[command.length];
            for (int i = command.length; --i >= 0; ) commandArgs[i] = command[i].toString();
            CommandLineTask job = new CommandLineTask(taskManager, name, commandArgs, env, new File(workingDirectory));
            for (Object depend : depends) {
                Resource resource = taskManager.getResource((String) depend);
                if (resource == null) throw new RuntimeException("Resource " + depend + " was not found");
                job.addDependency(resource, LockType.GENERATED);
            }
            for (Object readLock : readLocks) {
                Resource resource = taskManager.getResource((String) readLock);
                if (resource == null) throw new RuntimeException("Resource " + readLock + " was not found");
                job.addDependency(resource, LockType.READ_ACCESS);
            }
            for (Object writeLock : writeLocks) {
                final String id = (String) writeLock;
                Resource resource = taskManager.getResource(id);
                if (resource == null) {
                    resource = new SimpleData(taskManager, id, LockMode.EXCLUSIVE_WRITER, false);
                    resource.register(job);
                }
                job.addDependency(resource, LockType.WRITE_ACCESS);
            }
            taskManager.add(job);
            return true;
        }
