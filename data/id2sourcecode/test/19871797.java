    public void testSalesForceChannelSpecification() {
        final String user = "user1";
        final String password = "password";
        final String timeout = "6000";
        final String salesForceBizDriverDocument = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<xa:bizDriver xmlns=\"http://xaware.org/xas/ns1\"" + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" + " xmlns:xa=\"http://xaware.org/xas/ns1\"" + " xsi:schemaLocation=\"http://xaware.org/xas/ns1 file:/C:/XAwareSource/XAware51/server/XSD/JDBCBizDriver.xsd\" xa:bizdrivertype=\"SF\" xa:version=\"5.1\">" + "<xa:description>description0</xa:description>" + "<xa:input>" + "<!-- As many params as necessary -->" + "<xa:param xa:name=\"param1\" xa:datatype=\"string\" xa:default=\"value1\" xa:description=\"\" />" + "</xa:input>" + "<xa:connection>" + "<xa:user>" + user + "</xa:user>" + "<xa:pwd>" + password + "</xa:pwd>" + "<xa:timeout>" + timeout + "</xa:timeout>" + "</xa:connection>" + "</xa:bizDriver>";
        try {
            assertNotNull("Failed to create the BizDriverFactory", bdFactory);
            SAXBuilder sb = new SAXBuilder();
            Document jdom = null;
            jdom = sb.build(new ByteArrayInputStream(salesForceBizDriverDocument.getBytes()));
            assertNotNull("Failed to parse and get the JDOM structure", jdom);
            SalesForceBizDriver sfBizDriver = new SalesForceBizDriver();
            sfBizDriver.setChannelSpecification(new SalesForceChannelSpecification());
            sfBizDriver.setBizDriverIdentifier("testSalesForceChannelSpecification");
            sfBizDriver.setJdomDocument(jdom);
            sfBizDriver.setupContext(sfBizDriver.getBizDriverIdentifier(), new HashMap<String, Object>(), null);
            assertNotNull("Failed to get Salesforce biz driver spec", sfBizDriver);
            SalesForceChannelSpecification channel = (SalesForceChannelSpecification) sfBizDriver.getChannelSpecification();
            assertNotNull("Failed to get Salesforce channel spec", channel);
            String url = channel.getProperty(XAwareConstants.BIZDRIVER_URL);
            assertEquals("Url was expected to be blank, but it was " + url, "", url);
            String cTimeout = channel.getProperty(XAwareConstants.BIZDRIVER_TIMEOUT);
            assertEquals("Timeout was expected to be " + timeout + " but it was " + cTimeout, timeout, cTimeout);
            String cUser = channel.getProperty(XAwareConstants.BIZDRIVER_USER);
            assertEquals("User was expected to be " + user + " but it was " + cUser, cUser, user);
            String cPwd = channel.getProperty(XAwareConstants.BIZDRIVER_PWD);
            assertEquals("Password was expected to be " + password + " but it was " + cPwd, cPwd, password);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            fail(e);
        }
    }
