    public static TimeZonePatchList getTimeZonePatchList() throws TimeZonePatchListFactoryException {
        JOXBeanInputStream joxIn = null;
        ;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource(XML_FILE);
            joxIn = new JOXBeanInputStream(url.openStream());
            TimeZonePatchList joxTimeZonePatchList = (TimeZonePatchList) joxIn.readObject(TimeZonePatchList.class);
            joxTimeZonePatchList.valid();
            return joxTimeZonePatchList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TimeZonePatchListFactoryException("not able to read xml file properly. " + e.getMessage());
        } finally {
            try {
                joxIn.close();
            } catch (Exception e) {
            }
        }
    }
