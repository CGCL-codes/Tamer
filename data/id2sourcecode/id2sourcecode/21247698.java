    public static boolean copyFile(File source, File dest, Job job) throws IOException {
        boolean done = false;
        while (!done) {
            try {
                FileUtils.copyFile(source, dest);
                done = true;
            } catch (IOException ioe) {
                JobFailureDescription desc = new JobFailureDescription();
                desc.setCause(ioe);
                desc.setDescription("An error occured during file copy. \n" + "Please make sure the source file and destination directory is accessable and not full.");
                List<JobFailureOption> options = new LinkedList<JobFailureOption>();
                options.add(JobFailureOption.Retry);
                if (canRollback(job)) {
                    options.add(JobFailureOption.Rollback);
                }
                options.add(JobFailureOption.Cancel);
                desc.setOptions(options);
                desc.setRespond(JobFailureOption.Retry);
                job.requestFailureHandling(desc);
                switch(desc.getRespond()) {
                    case Retry:
                        done = false;
                        break;
                    case Rollback:
                        job.rollback();
                        throw ioe;
                    case Cancel:
                        job.cancel();
                        throw ioe;
                    case Ignore:
                    case IgnoreAll:
                        return false;
                }
            }
        }
        return true;
    }
