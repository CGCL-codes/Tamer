    protected void startApp() throws MIDletStateChangeException {
        RadiogramConnection rCon = null;
        Datagram dg = null;
        String ourAddress = System.getProperty("IEEE_ADDRESS");
        long now = 0L;
        IBattery batt = Spot.getInstance().getPowerController().getBattery();
        int reading = 0;
        ITriColorLED[] leds = EDemoBoard.getInstance().getLEDs();
        System.out.println("Starting sensor sampler application on " + ourAddress + " ...");
        try {
            rCon = (RadiogramConnection) Connector.open("radiogram://broadcast:" + HOST_PORT);
            dg = rCon.newDatagram(50);
        } catch (Exception e) {
            System.err.println("Caught " + e + " in connection initialization.");
            System.exit(1);
        }
        TSensors = new Sensors();
        TSensors.start();
        TConnexion = new TestConnexion();
        TConnexion.start();
        while (true) {
            try {
                now = System.currentTimeMillis();
                reading = batt.getBatteryLevel();
                leds[7].setRGB(255, 0, 0);
                leds[7].setOn();
                dg.reset();
                dg.writeLong(now);
                dg.writeInt(reading);
                dg.writeDouble(TSensors.valeur_telemetrie);
                if (TSensors.valeur_telemetrie > SEUIL_PAS) {
                    if (TConnexion.connected) {
                        nbPas++;
                        nbPas = 0;
                    } else {
                        nbPas++;
                    }
                } else {
                }
                TSensors.valeur_telemetrie = 0;
                rCon.send(dg);
                System.out.println("Battery level = " + reading);
                leds[7].setOff();
                Utils.sleep(SAMPLE_PERIOD - (System.currentTimeMillis() - now));
            } catch (Exception e) {
                System.err.println("Caught " + e + " while collecting/sending sensor sample.");
            }
        }
    }
