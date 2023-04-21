    public void stop() {
        queueAsynchronousAction(new Runnable() {

            public void run() {
                throw new TerminateException();
            }
        });
        try {
            thread.join();
            List throwables = thread.getThrowables();
            int count = throwables.size();
            if (count == 1) {
                Throwable t = (Throwable) throwables.get(0);
                if (t != null) {
                    if (t instanceof Error) {
                        throw (Error) t;
                    } else if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else {
                        throw new UndeclaredThrowableException(t);
                    }
                }
            } else if (count > 1) {
                StringWriter writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                for (Iterator i = throwables.iterator(); i.hasNext(); ) {
                    Throwable t = (Throwable) i.next();
                    t.printStackTrace(printWriter);
                }
                throw new RuntimeException("The following errors occurred in background thread\n" + writer.getBuffer());
            }
        } catch (InterruptedException e) {
        }
    }
