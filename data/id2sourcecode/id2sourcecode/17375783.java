    public String[] getResponse() {
        boolean continueReading;
        boolean readAgain = true;
        int myError = ERR_NOERROR;
        final boolean skipError = true;
        continueReading = gpsPort.isConnected();
        if (gpsPort.debugActive()) {
            gpsPort.writeDebug("\r\n:" + Convert.toString(Vm.getTimeStamp()) + ":");
        }
        if (current_state == C_FOUND_STATE) {
            current_state = C_START_STATE;
        }
        while (continueReading) {
            while (continueReading && (read_buf_p < bytesRead)) {
                char c;
                c = (char) read_buf[read_buf_p++];
                switch(current_state) {
                    case C_EOL_STATE:
                        if (((c == 10) || (c == 13))) {
                            current_state = C_FOUND_STATE;
                            continueReading = false;
                            if (ignoreNMEA) {
                                continueReading = ((String) vCmd.items[0]).startsWith("GP");
                            }
                        } else {
                            current_state = C_ERROR_STATE;
                        }
                        break;
                    case C_FOUND_STATE:
                    case C_INITIAL_STATE:
                    case C_START_STATE:
                        vCmd.removeAllElements();
                        if (c == '$') {
                            current_state = C_FIELD_STATE;
                            cmd_buf_p = 0;
                            checksum = 0;
                        } else if (!((c == 10) || (c == 13))) {
                            if (current_state == C_START_STATE) {
                                myError = ERR_INCOMPLETE;
                                current_state = C_ERROR_STATE;
                            }
                        }
                        break;
                    case C_FIELD_STATE:
                        if ((c == 10) || (c == 13)) {
                            current_state = C_EOL_STATE;
                        } else if (c == '*') {
                            current_state = C_STAR_STATE;
                            vCmd.add(new String(cmd_buf, 0, cmd_buf_p));
                        } else if (c == ',') {
                            checksum ^= c;
                            vCmd.add(new String(cmd_buf, 0, cmd_buf_p));
                            cmd_buf_p = 0;
                        } else {
                            cmd_buf[cmd_buf_p++] = c;
                            checksum ^= c;
                        }
                        break;
                    case C_STAR_STATE:
                        if ((c == 10) || (c == 13)) {
                            current_state = C_ERROR_STATE;
                        } else if (((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f'))) {
                            if (c >= '0' && c <= '9') {
                                read_checksum = (c - '0') << 4;
                            } else if (c >= 'A' && c <= 'F') {
                                read_checksum = (c - 'A' + 10) << 4;
                            } else {
                                read_checksum = (c - 'a' + 10) << 4;
                            }
                            current_state = C_CHECKSUM_CHAR1_STATE;
                        } else {
                            myError = ERR_INCOMPLETE;
                            current_state = C_ERROR_STATE;
                        }
                        break;
                    case C_CHECKSUM_CHAR1_STATE:
                        if ((c == 10) || (c == 13)) {
                            myError = ERR_INCOMPLETE;
                            current_state = C_ERROR_STATE;
                        } else if (((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F'))) {
                            if (c >= '0' && c <= '9') {
                                read_checksum += c - '0';
                            } else if (c >= 'A' && c <= 'F') {
                                read_checksum += c - 'A' + 10;
                            } else {
                                read_checksum += c - 'a' + 10;
                            }
                            if (read_checksum != checksum) {
                                myError = ERR_CHECKSUM;
                                current_state = C_ERROR_STATE;
                            }
                            current_state = C_EOL_STATE;
                        } else {
                            myError = ERR_INCOMPLETE;
                            current_state = C_ERROR_STATE;
                        }
                        break;
                    case C_ERROR_STATE:
                        if (((c == 10) || (c == 13))) {
                            current_state = C_START_STATE;
                        }
                }
                if (cmd_buf_p > (C_BUF_SIZE - 1)) {
                    myError = ERR_TOO_LONG;
                    current_state = C_ERROR_STATE;
                }
                if (current_state == C_ERROR_STATE) {
                    current_state = C_INITIAL_STATE;
                    vCmd.removeAllElements();
                    if (!skipError) {
                        continueReading = false;
                    }
                }
            }
            if (continueReading) {
                read_buf_p = 0;
                bytesRead = 0;
                if (isConnected()) {
                    if (readAgain) {
                        readAgain = false;
                        try {
                            int max = gpsPort.readCheck();
                            if (max > C_BUF_SIZE) {
                                max = C_BUF_SIZE;
                            }
                            if (max > 0) {
                                bytesRead = gpsPort.readBytes(read_buf, 0, max);
                            }
                        } catch (Exception e) {
                            bytesRead = 0;
                        }
                    }
                    if (bytesRead == 0) {
                        continueReading = false;
                    } else {
                        if (gpsPort.debugActive()) {
                            String q = "(" + Convert.toString(Vm.getTimeStamp()) + ")";
                            gpsPort.writeDebug(q.getBytes(), 0, q.length());
                            gpsPort.writeDebug(read_buf, 0, bytesRead);
                        }
                    }
                }
            }
        }
        if (myError == C_ERROR_STATE) {
            vCmd.removeAllElements();
        }
        if (current_state == C_FOUND_STATE) {
            return (String[]) vCmd.toObjectArray();
        } else {
            return null;
        }
    }
