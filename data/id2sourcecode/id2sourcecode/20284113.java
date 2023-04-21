    public void setMinimalPuts(boolean minimalPuts) throws HibernateException {
        if (minimalPuts) throw new HibernateException("minimal puts not supported for read-write cache");
    }
