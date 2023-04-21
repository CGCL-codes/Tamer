    public Map<Integer, PortHandler> createPortHandlers(Robot robot) {
        final Map<Integer, PortHandler> ports = new HashMap<Integer, PortHandler>();
        mapPort(ports, SPEDOMETER, robot.getThrottle().getSpedometer());
        mapPort(ports, HEAT, getTemperatureSensor(robot.getHeatSinks()));
        mapPort(ports, COMPASS, getCompass(robot));
        mapPort(ports, TURRET_OFS, robot.getTurretOffsetSensor());
        mapPort(ports, TURRET_ABS, getCompass(robot.getTurret()));
        mapPort(ports, DAMAGE, robot.getArmor().getSensor());
        mapPort(ports, SCAN, robot.getTurret().getScanner().getScanPort());
        mapPort(ports, ACCURACY, robot.getTurret().getScanner().getAccuracyPort());
        mapPort(ports, RADAR, robot.getRadar().getScanPort());
        mapPort(ports, RANDOM, createRandomNumberGenerator());
        mapPort(ports, THROTTLE, robot.getThrottle().getActuator());
        mapPort(ports, OFS_TURRET, getRotationPort(robot.getTurret().getHeading()));
        mapPort(ports, ABS_TURRET, robot.getAimTurretPort());
        mapPort(ports, STEERING, getRotationPort(robot.getDesiredHeading()));
        mapPort(ports, WEAP, robot.getTurret().getMissileLauncher().getActuator());
        mapPort(ports, SONAR, robot.getSonar().getScanPort());
        mapPort(ports, ARC, robot.getTurret().getScanner().getScanArcLatchPort());
        mapPort(ports, OVERBURN, robot.getOverburnLatchPort());
        mapPort(ports, TRANSPONDER, getTransponderLatchPort(robot.getTransponder()));
        mapPort(ports, SHUTDOWN, robot.getShutdownLevelLatchPort());
        mapPort(ports, CHANNEL, robot.getTransceiver().getChannelLatchPort());
        mapPort(ports, MINELAYER, robot.getMineLayer().getMineBayPort());
        mapPort(ports, MINETRIGGER, robot.getMineLayer().getPlacedMinePort());
        mapPort(ports, SHIELD, robot.getShield().getLatch());
        connectPortHandlers(ports.values(), robot.getComputer());
        return new MapWithDefaultValue<Integer, PortHandler>(Collections.unmodifiableMap(ports), robot.getComputer().createDefaultPortHandler());
    }
