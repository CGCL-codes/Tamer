        FillingTask() throws Exception {
            File dev_zero = new File("/dev/zero");
            readChannel = new FileInputStream(dev_zero).getChannel();
        }
