    public void run() {
        try {
            try {
                if (!target.exists()) {
                    if (target.createNewFile()) {
                        synchronized (this) {
                            session.getConnection().getResultWriter().write("\nSAW>SAWFILECREATE:File '" + target.getPath() + "' created on server file system!\nSAW>");
                            session.getConnection().getResultWriter().flush();
                            finished = true;
                        }
                    } else {
                        synchronized (this) {
                            session.getConnection().getResultWriter().write("\nSAW>SAWFILECREATE:File '" + target.getPath() + "' cannot be created on server file system!\nSAW>");
                            session.getConnection().getResultWriter().flush();
                            finished = true;
                        }
                    }
                } else {
                    synchronized (this) {
                        session.getConnection().getResultWriter().write("\nSAW>SAWFILECREATE:Path '" + target.getPath() + "' already exists on server file system!\nSAW>");
                        session.getConnection().getResultWriter().flush();
                        finished = true;
                    }
                }
            } catch (SecurityException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECREATE:Security error detected!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (IOException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECREATE:File '" + target.getPath() + "' cannot be created on server file system!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            } catch (NullPointerException e) {
                synchronized (this) {
                    session.getConnection().getResultWriter().write("\nSAW>SAWFILECREATE:File '" + target.getPath() + "' cannot be created on server file system!\nSAW>");
                    session.getConnection().getResultWriter().flush();
                    finished = true;
                }
            }
        } catch (Exception e) {
        }
        finished = true;
    }
