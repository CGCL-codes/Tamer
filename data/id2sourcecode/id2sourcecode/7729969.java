    private void check(String endpointURL, long startTime, int controlValue) throws DynasoarExceptionType {
        System.out.println("TomcatWarInstaller.check, endpointURL = " + endpointURL);
        try {
            long newTime = System.currentTimeMillis();
            URL url = new URL(endpointURL);
            url.openStream();
            System.out.println("FINISH " + (newTime - startTime) / 1000);
        } catch (IOException e) {
            System.out.println(e.toString());
            long time = System.currentTimeMillis();
            System.out.println((time - startTime) / 1000);
            do {
                time = System.currentTimeMillis();
            } while (time < startTime + controlValue);
            if (controlValue < timeoutMillis) {
                check(endpointURL, startTime, (controlValue + 2000));
            } else {
                throw new DynasoarExceptionType();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            throw new DynasoarExceptionType();
        }
    }
