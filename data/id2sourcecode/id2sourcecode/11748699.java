    public float cRadiationFactor(float tL, float bL, float lL, float rL) {
        float result;
        float aveLat = (tL + bL) / 2;
        float aveLon = (lL + rL) / 2;
        orbitX = MAJOR_AXIS_LENGTH * Math.cos(Math.toRadians((minuteOfYear - MINUTE_VERNAL_EQUINOX) * ORBIT_DEGREE_MINUTE));
        orbitY = minorAxisLength * Math.cos(Math.toRadians((minuteOfYear - MINUTE_VERNAL_EQUINOX) * ORBIT_DEGREE_MINUTE));
        latRadiation = Math.abs(aveLat + tiltOfEarth * Math.cos(Math.toRadians(SPIN_DEGREE_MINUTE * minuteOfDay + Math.atan(orbitY / orbitX) - equinoxAngle + aveLon)));
        double lonRadiation = Math.cos(Math.toRadians(aveLon - this.longitude));
        double distance = Math.sqrt(orbitX * orbitX / 2 + orbitY * orbitY / 2);
        if (lonRadiation < 0) {
            return 0;
        } else {
            result = (float) (latRadiation * lonRadiation * (SUN_SURFACE_INTENSITY / Math.pow(distance / SUN_RADIUS, 2) * Math.pow(10, 10)));
            return result;
        }
    }
