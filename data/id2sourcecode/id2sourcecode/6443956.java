        public void serialEvent(SerialPortEvent event) {
            switch(event.getEventType()) {
                case SerialPortEvent.BI:
                    SerialCardioTerminalImpl.writeToLog("BREAK INTERRUPT!");
                    if (isCompleted()) firePagerStateChanged(PagerStates.OFFLINE);
                case SerialPortEvent.OE:
                case SerialPortEvent.FE:
                case SerialPortEvent.PE:
                case SerialPortEvent.CD:
                case SerialPortEvent.CTS:
                case SerialPortEvent.DSR:
                case SerialPortEvent.RI:
                case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                    break;
                case SerialPortEvent.DATA_AVAILABLE:
                    SerialCardioTerminalImpl.writeToLog("received DATA_AVAILABLE event");
                    if (isCompleted()) return;
                    try {
                        SerialCardioTerminalImpl.writeToLog("Available " + inputStream.available() + " bytes, need to read:" + buffer.limit());
                        int av = inputStream.available();
                        if (buffer.limit() - receivedBytes < av) {
                            av = buffer.limit() - receivedBytes;
                        }
                        byte ar[] = new byte[av];
                        receivedBytes += inputStream.read(ar);
                        buffer.put(ar);
                        SerialCardioTerminalImpl.writeToLog("Read " + receivedBytes + " bytes, Remaining " + (buffer.limit() - receivedBytes));
                    } catch (IOException e) {
                        throwable = e;
                        return;
                    } catch (NullPointerException e) {
                        throwable = e;
                        return;
                    } finally {
                        if (terminatorIsNullAndExpectMoreToReceive()) {
                            SerialCardioPagerConduit.this.setLastSync(System.currentTimeMillis());
                            return;
                        } else if (checkTerminatorVsMoreToReceive()) {
                            if (canCompareTerminatorWithBuffer()) {
                                if (!isTerminatorFound(terminator)) {
                                    SerialCardioPagerConduit.this.setLastSync(System.currentTimeMillis());
                                    return;
                                } else {
                                    terminatorFound = true;
                                }
                            } else {
                                SerialCardioPagerConduit.this.setLastSync(System.currentTimeMillis());
                                return;
                            }
                        }
                        if (isTerminatorFound(terminator)) {
                            terminatorFound = true;
                        }
                        completed = true;
                        if (buffer != null && buffer.array() != null) receivedBytes = buffer.array().length; else receivedBytes = 0;
                        synchronized (SerialCardioPagerConduit.this) {
                            SerialCardioTerminalImpl.writeToLog("Notifying waiting SerialCardioPagerConduit thread about data received...");
                            SerialCardioPagerConduit.this.notify();
                        }
                    }
                    break;
            }
        }
