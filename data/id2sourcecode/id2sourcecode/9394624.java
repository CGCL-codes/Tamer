    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof HandledRuntimeException && e.getCause() != null) {
            Channel c = ((HandledRuntimeException) e).getChannel();
            Throwable cause = e.getCause();
            List<Throwable> list = exceptions.get(t);
            if (list == null) {
                list = new ArrayList<Throwable>();
                list.add(cause);
                list = exceptions.putIfAbsent(c, list);
                if (list != null) list.add(cause);
            } else {
                list.add(cause);
            }
            attach(t, c);
        } else {
            if (Thread.getDefaultUncaughtExceptionHandler() != null) Thread.getDefaultUncaughtExceptionHandler().uncaughtException(t, e); else throw new RuntimeException(e);
        }
    }
