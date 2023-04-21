    public void testReservationStore() throws RaplaException {
        facade.login("homer", "duffs".toCharArray());
        {
            Reservation r = facade.newReservation();
            r.getClassification().setValue("name", "test");
            Appointment app = facade.newAppointment(new Date(), new Date());
            Appointment app2 = facade.newAppointment(new Date(), new Date());
            Allocatable resource = facade.newResource();
            r.addAppointment(app);
            r.addAppointment(app2);
            r.addAllocatable(resource);
            r.setRestriction(resource, new Appointment[] { app });
            app.setRepeatingEnabled(true);
            app.getRepeating().setType(Repeating.DAILY);
            app.getRepeating().setNumber(10);
            app.getRepeating().addException(new Date());
            facade.storeObjects(new Entity[] { r, app, app2, resource });
        }
        operator.disconnect();
        operator.connect();
        facade.login("homer", "duffs".toCharArray());
        {
            String defaultReservation = "event";
            ClassificationFilter filter = facade.getDynamicType(defaultReservation).newClassificationFilter();
            filter.addRule("name", new Object[][] { { "contains", "test" } });
            Reservation reservation = facade.getReservationsForAllocatable(null, null, null, new ClassificationFilter[] { filter })[0];
            Appointment[] apps = reservation.getAppointments();
            Allocatable resource = reservation.getAllocatables()[0];
            assertEquals(2, apps.length);
            assertEquals(1, reservation.getAppointmentsFor(resource).length);
            Appointment app = reservation.getAppointmentsFor(resource)[0];
            assertEquals(1, app.getRepeating().getExceptions().length);
            assertEquals(Repeating.DAILY, app.getRepeating().getType());
            assertEquals(10, app.getRepeating().getNumber());
        }
    }
