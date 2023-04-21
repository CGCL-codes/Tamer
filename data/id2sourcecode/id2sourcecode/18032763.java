    private SerialPort connect(String portName) throws Exception {
        SerialPort serialPort = null;
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            state = 9;
            message += "Error: '" + portName + "'Port is currently in use. May be an other application is using the serial communication.";
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if (commPort instanceof SerialPort) {
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                reader = new Thread(new SerialReader(in));
                writer = new Thread(new SerialWriter(out));
                reader.start();
                writer.start();
            } else {
                message += "No serial or USB port found.";
            }
        }
        return serialPort;
    }
