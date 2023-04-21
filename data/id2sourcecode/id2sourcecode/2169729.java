    private DriverInfo elementToDriver(Element element) throws Exception {
        DriverInfo retVal = null;
        if (element != null) {
            retVal = new DriverInfo();
            if (log.isInfo()) log.info("reading <driver>");
            retVal.setClassName(element.attributeValue("class-name"));
            retVal.setChannel(element.attributeValue("channel"));
            if (retVal.getClassName() == null || retVal.getClassName().length() == 0) throw new Exception("<driver class-name> is required");
            if (retVal.getChannel() == null || retVal.getChannel().length() == 0) if (log.isWarning()) log.warning("<driver channel> not defined");
        }
        return retVal;
    }
