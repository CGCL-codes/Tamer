        public void run() {
            try {
                logger.info("Performing connection THREAD =" + Thread.currentThread().getName() + " in runnable :: " + "Comm Connection " + Conf.getInstance().getCommPortName() + " bauds = " + Conf.getInstance().getDeviceBaudRate() + " PORT NAME =" + Conf.getInstance().getCommPortName() + "_ " + " DEVICE BAUD RATE =" + Conf.getInstance().getDeviceBaudRate() + "_" + "COMM PORT NAME= " + Conf.getInstance().getCommPortName() + "_");
                CommPort commPort = port.open("Comm Connection ", Conf.getInstance().getDeviceBaudRate());
                logger.info("openning port");
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                SerialPort sPort = (SerialPort) commPort;
                logger.info("Performing connection THREAD =" + Thread.currentThread().getName() + " obtained serial port ");
                conn.setSPort(sPort);
                InputStream is = sPort.getInputStream();
                OutputStream os = sPort.getOutputStream();
                logger.info("Performing connection THREAD =" + Thread.currentThread().getName() + " obtained stream to port " + sPort.getName());
                conn.setPortInputStream(is);
                conn.setPortOutputstream(os);
                logger.info("Performing connection THREAD =" + Thread.currentThread().getName() + " IO streams loaded ");
                logger.info("Serial port reader and writer S E T ! ");
                conn.setState(CommState.CONNECTED, "You are connected to " + sPort.getName() + " with baud rate " + sPort.getBaudRate(), conn.sConnected);
                conn.openCommunication();
            } catch (PortInUseException e) {
                conn.setState(CommState.CONNECTION_FAILURE, "Port is in use.");
                e.printStackTrace();
            } catch (IOException e) {
                conn.setState(CommState.CONNECTION_FAILURE, "IO Exception.");
                e.printStackTrace();
            } catch (UnsupportedCommOperationException e) {
                conn.setState(CommState.CONNECTION_FAILURE, "Unsupported operation excption.");
                e.printStackTrace();
            }
            if (conn.state instanceof CSConnected) {
                logger.info("Performing connection THREAD =" + Thread.currentThread().getName() + " YOU ARE CONECTED");
            } else if (conn.state instanceof CSDisconnected) {
                logger.info("Performing connection THREAD =" + Thread.currentThread().getName() + " Time out ");
                conn.setState(CommState.CONNECTION_FAILURE, "Time out");
            }
        }
