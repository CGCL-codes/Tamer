    public XBeanDriver getDriver(Request request, DriverInfo driverInfo) throws Exception {
        String driverKey = driverInfo.getClassName();
        if (driverInfo.getChannel() != null) driverKey = driverKey.concat(K.COLON).concat(driverInfo.getChannel());
        XBeanDriver retVal = driversMap.get(driverKey);
        if (retVal == null) {
            try {
                retVal = (XBeanDriver) Class.forName(driverInfo.getClassName()).newInstance();
            } catch (ClassNotFoundException notFound) {
                retVal = (XBeanDriver) Class.forName(DRIVER_PACKAGE.concat(driverInfo.getClassName())).newInstance();
            }
            if (request != null) retVal.open(request, driverInfo.getChannel()); else retVal.open(driverInfo.getChannel());
            driversMap.put(driverKey, retVal);
            drivers.addElement(retVal);
            if (log.isInfo()) log.info("Instatiated XBeanDriver '" + driverKey + "' (" + retVal + ")");
        } else {
            if (log.isInfo()) log.info("Reusing XBeanDriver '" + driverKey + "' (" + retVal + ")");
        }
        return retVal;
    }
