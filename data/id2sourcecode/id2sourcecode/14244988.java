    public void handle(Object o) {
        Job job = (Job) o;
        try {
            if (job.in != null) copy(job.in, job.out, -1); else copy(job.read, job.write, -1);
        } catch (IOException e) {
            LogSupport.ignore(log, e);
            try {
                if (job.out != null) job.out.close();
                if (job.write != null) job.write.close();
            } catch (IOException e2) {
                LogSupport.ignore(log, e2);
            }
        }
    }
