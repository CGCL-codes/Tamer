    @Before
    public void testSetup() throws Exception {
        category = new Category();
        category.setType(Category.COM);
        driver = new Driver();
        driver.setName("智能鱼缸");
        user = new User();
        user.setName("bill");
        device = new Device();
        device.setDefault(false);
        device.setStatus(Device.OPENED);
        variable = new Variable();
        variable.setName("鱼缸水温");
        variable.setType("read_write_discrete");
        item = new Item();
        item.setText("12°C");
        item.setValue("23810".getBytes());
        deviceAPI = new DeviceAPI("localhost", "8888", "service");
        variableAPI = new VariableAPI("localhost", "8888", "service");
        itemAPI = new ItemAPI("localhost", "8888", "service");
        categoryAPI = new CategoryAPI("localhost", "8888", "service");
        driverAPI = new DriverAPI("localhost", "8888", "service");
        userAPI = new UserAPI("localhost", "8888", "service");
        APIResponse response = categoryAPI.create(category);
        if (response.isDone()) {
            category = (Category) response.getMessage();
            assertEquals(Category.COM, category.getType());
        } else {
            logger.log(Level.SEVERE, response.getMessage().toString());
        }
        driver.setCategory(category);
        response = driverAPI.create(driver);
        if (response.isDone()) {
            driver = (Driver) response.getMessage();
            assertEquals("智能鱼缸", driver.getName());
        } else {
            logger.log(Level.SEVERE, response.getMessage().toString());
        }
        response = userAPI.create(user);
        if (response.isDone()) {
            user = (User) response.getMessage();
            assertEquals("bill", user.getName());
        } else {
            logger.log(Level.SEVERE, response.getMessage().toString());
        }
        device.setDriver(driver);
        device.setUser(user);
    }
