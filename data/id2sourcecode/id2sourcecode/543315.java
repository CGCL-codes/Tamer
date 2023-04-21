    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
            case SerialPortEvent.BI:
                System.out.println("BlueSentry: Break interrupt");
                break;
            case SerialPortEvent.OE:
                System.out.println("BlueSentry: Overrun error");
                break;
            case SerialPortEvent.FE:
                System.out.println("BlueSentry: Framing error");
                break;
            case SerialPortEvent.PE:
                System.out.println("BlueSentry: Parity error");
                break;
            case SerialPortEvent.CD:
                System.out.println("BlueSentry: Carrier detect");
                break;
            case SerialPortEvent.CTS:
                System.out.println("BlueSentry: Clear to send");
                break;
            case SerialPortEvent.DSR:
                System.out.println("BlueSentry: Data set ready");
                break;
            case SerialPortEvent.RI:
                System.out.println("BlueSentry: Ring indicator");
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                System.out.println("BlueSentry: Output Buffer Empty");
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    try {
                        reconnectTimer.cancel();
                    } catch (IllegalStateException e) {
                    }
                    connected = true;
                    String line = bluesentryIn.readLine();
                    if (DEBUG) System.out.print(line + ":");
                    StringTokenizer st = new StringTokenizer(line);
                    int numTokens = st.countTokens();
                    numChannels = numTokens - 1;
                    if (st.hasMoreTokens()) {
                        if (st.nextToken().equals("?")) {
                            outputStream.write(INIT.getBytes());
                            return;
                        }
                        try {
                            for (int i = 0; i < numChannels; i++) {
                                channel[i] = Integer.parseInt(st.nextToken(), 16);
                            }
                            notifyClients();
                            if (DEBUG) System.out.println(getChannel(0));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    reconnectTimer = new Timer();
                    reconnectTimer.schedule(new ReconnectTask(), RECONNECT_TIMEOUT);
                } catch (IOException e) {
                }
                break;
        }
    }
