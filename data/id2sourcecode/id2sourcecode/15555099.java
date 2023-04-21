    public void testSetAccessModeWithBadMode() throws Exception {
        try {
            maintenanceManager.setDatabaseAccessMode(MaintenanceManager.ACCESS_MODE_READ_ONLY | MaintenanceManager.ACCESS_MODE_READ_WRITE);
            fail("Access mode must be either read-only or read-write");
        } catch (IllegalArgumentException e) {
        }
    }
