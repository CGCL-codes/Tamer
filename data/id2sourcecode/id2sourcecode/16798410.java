    public void handle(Callback[] callbacks) throws UnsupportedCallbackException {
        if (callbacks == null) {
            throw new UnsupportedCallbackException(null, "ERROR: Callbacks can not be null.");
        }
        for (int i = 0; i < callbacks.length; i++) {
            if (callbacks[i] instanceof SAWSTextOutputCallback) {
                switch(((SAWSTextOutputCallback) callbacks[i]).getMessageType()) {
                    case SAWSTextOutputCallback.ERROR:
                        System.err.println("\nERROR: " + ((SAWSTextOutputCallback) callbacks[i]).getMessage());
                        break;
                    case SAWSTextOutputCallback.INFORMATION:
                        System.err.println("\nINFO: " + ((SAWSTextOutputCallback) callbacks[i]).getMessage());
                        break;
                    case SAWSTextOutputCallback.WARNING:
                        System.err.println("\nWARNING: " + ((SAWSTextOutputCallback) callbacks[i]).getMessage());
                        break;
                    case SAWSTextOutputCallback.LONG_MESSAGE:
                        System.err.println("\n" + ((SAWSTextOutputCallback) callbacks[i]).getMessage());
                        break;
                }
            } else if (callbacks[i] instanceof SAWSPasswordCallback) {
                SAWSPasswordCallback pc = (SAWSPasswordCallback) callbacks[i];
                System.out.println("\n" + pc.getPrompt());
                System.out.flush();
                try {
                    pc.setPassword(readPassword(System.in));
                } catch (IOException e) {
                    System.err.println("\nWARNING: Fail to read the password from the command prompt.");
                    sawsDebugLog.write("\nWARNING: Fail to read the password from the command prompt.");
                }
            } else if (callbacks[i] instanceof CertificateDataCallback) {
                CertificateDataCallback cdc = (CertificateDataCallback) callbacks[i];
                CertificateData cd = this.readCertificateData(cdc);
                cdc.setCertData(cd);
            } else if (callbacks[i] instanceof SAWSChoiceCallback) {
                SAWSChoiceCallback cc = (SAWSChoiceCallback) callbacks[i];
                System.out.println("\n" + cc.getPrompt() + "\n");
                System.out.flush();
                InputStreamReader is = new InputStreamReader(System.in);
                BufferedReader systemIn = new BufferedReader(is);
                String[] options = cc.getOptions();
                int n = options.length;
                for (int j = 0; j < n; j = j + 1) {
                    System.out.println("\t[" + (j + 1) + "]  " + options[j]);
                }
                System.out.println("\nPlease type the number that corresponds to your choice:");
                int choice = -1;
                boolean finished = false;
                while (!finished) {
                    try {
                        choice = Integer.parseInt(systemIn.readLine());
                    } catch (IOException e) {
                        System.err.println("ERROR: Fail when reading the option from the command prompt.");
                        sawsDebugLog.write("ERROR: Fail when reading the option from the command prompt.");
                    } catch (Exception ex) {
                        System.err.println("\nWARNING: Invalid option. Please input a valid number for the option:");
                    }
                    if ((choice <= 0) || (choice > n)) {
                        System.err.println("\nWARNING: Invalid option. Please input a valid option:");
                    } else {
                        finished = true;
                    }
                }
                cc.setSelectedIndex(choice - 1);
            } else if (callbacks[i] instanceof SAWSTextInputCallback) {
                SAWSTextInputCallback cc = (SAWSTextInputCallback) callbacks[i];
                System.out.println("\n" + cc.getPrompt());
                System.out.flush();
                InputStreamReader is = new InputStreamReader(System.in);
                BufferedReader systemIn = new BufferedReader(is);
                String value = null;
                try {
                    value = systemIn.readLine();
                } catch (IOException ioe) {
                    System.err.println("ERROR: Fail when reading a value from the command prompt.");
                    sawsDebugLog.write("ERROR: Fail when reading a value from the command prompt.");
                }
                cc.setText(value);
            } else {
                throw new UnsupportedCallbackException(callbacks[i], "ERROR: Unrecognized Callback.");
            }
        }
    }
