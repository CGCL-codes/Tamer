    public void print(Context context, String deviceName, InputStream input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String[] deviceData = deviceName.split(ParameterKeys.ATTRIBUTE_SET_SEPARATOR);
        MobileDeviceInfo deviceInfo = MobileDevices.INSTANCE.getDevices().get(deviceData[2]);
        String deviceAddress = deviceData[1];
        try {
            byte[] b = new byte[16 * 1024];
            int read;
            while ((read = input.read(b)) != -1) {
                output.write(b, 0, read);
            }
            byte[] printData = output.toByteArray();
            for (MobileClientConversionStep step : deviceInfo.getClientSteps()) {
                if (step.equals(MobileClientConversionStep.OUTPUT_BYTES)) {
                    printBytes(deviceInfo, deviceAddress, printData);
                } else if (step.equals(MobileClientConversionStep.OUTPUT_SM_BYTES)) {
                    printStarMicronicsByteArray(deviceInfo, deviceAddress, printData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
