    protected final UsbDevice findUSB(String portName) throws IOException {
        NDC.push("findUsb");
        logger.info("Looking for device '" + portName + "'");
        try {
            if (System.currentTimeMillis() - lastDetect < 1000) {
                throw new IOException("Polling too fast");
            }
            lastDetect = System.currentTimeMillis();
            Set<UsbDevice> found = new HashSet<UsbDevice>();
            try {
                find(portName, virtualRootHub, found, true);
            } catch (BootException bex) {
                throw new IllegalStateException("BootException shouldn't have propagated here", bex);
            }
            if (found.size() == 1) {
                return (UsbDevice) found.toArray()[0];
            }
            if (portName == null) {
                if (found.isEmpty()) {
                    throw new IOException("No compatible devices found. Make sure you have /proc/bus/usb read/write permissions.");
                } else {
                    tooManyDevices(found);
                }
            } else {
                if (found.isEmpty()) {
                    throw new IOException("Device with a serial number '" + portName + "' is not connected");
                } else {
                    tooManyDevices(found);
                }
            }
            throw new IOException("No device found with serial number " + portName);
        } catch (UnsatisfiedLinkError ule) {
            logger.debug("\nMake sure you have the directory containing libJavaxUsb.so in your LD_LIBRARY_PATH\n");
            throw ule;
        } catch (UsbException usbex) {
            throw (IOException) new IOException("USB failure").initCause(usbex);
        } finally {
            NDC.pop();
        }
    }
