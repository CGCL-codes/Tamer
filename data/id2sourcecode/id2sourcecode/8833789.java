    public double addReading(double reading) {
        double averageReading = (reading + lastReading) / 2;
        long millis = System.currentTimeMillis();
        double timeInterval = (double) (millis - lastReadingMillis) / 1000d;
        lastReadingMillis = millis;
        integral += (averageReading * timeInterval);
        lastReading = reading;
        return integral;
    }
