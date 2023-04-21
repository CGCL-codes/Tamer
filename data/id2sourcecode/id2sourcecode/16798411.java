    private CertificateData readCertificateData(CertificateDataCallback cdc) {
        CertificateData cd = new CertificateData();
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader systemIn = new BufferedReader(is);
        boolean valid = false;
        System.out.println("\nPlease input the following data for the certificate:");
        try {
            Loop1: while (!valid) {
                System.out.println("\nValidity (DDMMYYYY):");
                String validity = systemIn.readLine();
                if (validity.length() != 8) {
                    System.err.println("WARNING: Please input a date in the format DDMMYYYY.");
                    continue Loop1;
                }
                Calendar c = Calendar.getInstance();
                c.setLenient(false);
                try {
                    c.set(Calendar.YEAR, Integer.parseInt(validity.substring(4, 8)));
                    c.set(Calendar.MONTH, Integer.parseInt(validity.substring(2, 4)) - 1);
                    c.set(Calendar.DATE, Integer.parseInt(validity.substring(0, 2)));
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    Calendar temp = Calendar.getInstance();
                    temp.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), 0, 0, 0);
                    long todayMili = today.getTimeInMillis();
                    long dateMili = temp.getTimeInMillis();
                    if (todayMili > dateMili) {
                        System.err.println("WARNING: The selected date must be after to " + today.get(Calendar.DATE) + "/" + (today.get(Calendar.MONTH) + 1) + "/" + today.get(Calendar.YEAR) + ".");
                        continue Loop1;
                    }
                    int days = (int) ((dateMili - todayMili) / 1000 / 60 / 60 / 24);
                    today.add(Calendar.DATE, days);
                    if (today.compareTo(temp) > 0) {
                        days = days - 1;
                    } else if (today.compareTo(temp) < 0) {
                        days = days + 1;
                    }
                    cd.setValidity(days);
                    valid = true;
                } catch (NumberFormatException nfe) {
                    System.err.println("WARNING: The date can not contain letters, only numbers. Please type again.");
                } catch (IllegalArgumentException iae) {
                    System.err.println("WARNING: " + iae.getMessage() + " is not valid. Please type the validity date again.");
                }
            }
            valid = false;
            System.out.println("\nPlease answer the following questions." + "\n[Press Enter (Ret) to skip the question. At least one of the six questions must be answered.");
            while (!valid) {
                System.out.println("\nWhat is your complete name (CN)?");
                cd.setCommonName(systemIn.readLine());
                System.out.println("\nWhat is the name of your Organizational Unit (OU)?");
                cd.setOrganizationUnitName(systemIn.readLine());
                System.out.println("\nWhat is the name of your Organization (O)?");
                cd.setOrganizationName(systemIn.readLine());
                System.out.println("\nWhat is the name of your City or Locality (L)?");
                cd.setLocalityName(systemIn.readLine());
                System.out.println("\nWhat is the name of your Province or State (S)?");
                cd.setStateName(systemIn.readLine());
                Country: while (true) {
                    System.out.println("\nWhat is the two-letter Country code for this unit (C)?");
                    String country = systemIn.readLine();
                    if (country != null && !country.equals("") && country.length() != 2) {
                        System.err.println("WARNING: The country code, if specified, must contain two letters. Please try again.");
                        continue Country;
                    } else {
                        cd.setCountryName(country);
                        break Country;
                    }
                }
                String sn = cd.toSubjectName();
                if (sn != null && !sn.equals("")) {
                    System.out.println("\nIs " + sn + " correct? [y/n]");
                    System.out.print("[no]: ");
                    String option = systemIn.readLine();
                    if (option.equalsIgnoreCase("y") || option.equalsIgnoreCase("yes")) {
                        valid = true;
                    }
                } else {
                    System.err.println("WARNING: At least one of the questions must be answered. Please try again.");
                }
            }
            valid = false;
            String alg = null;
            if (cdc.getType() == CertificateDataCallback.ENCRYPTION) {
                cd.setAlgorithm("RSA");
            } else {
                System.out.println("\nPlease input the name of the algorithm for this certificate: [RSA or DSA]");
                while (!valid) {
                    alg = systemIn.readLine();
                    if (alg != null && (alg.equalsIgnoreCase("RSA") || alg.equalsIgnoreCase("DSA"))) {
                        cd.setAlgorithm(alg.toUpperCase());
                        valid = true;
                    } else {
                        System.out.println("\nWARNING: The supported algorithms are RSA and DSA. Please type one of these algorithms:");
                    }
                }
            }
            valid = false;
            int[] keySizes = null;
            alg = cd.getAlgorithm();
            if (alg.equalsIgnoreCase("RSA")) {
                keySizes = new int[] { 1024, 2048, 3072, 4096 };
            } else {
                keySizes = new int[] { 512, 640, 768, 896, 1024 };
            }
            System.out.println("\nPlease input one of the following options for the key size:\n");
            Loop2: while (!valid) {
                for (int j = 0; j < keySizes.length; j = j + 1) {
                    System.out.println("\t[" + (j + 1) + "] " + keySizes[j]);
                }
                int choice = -1;
                try {
                    choice = Integer.parseInt(systemIn.readLine());
                } catch (Exception ex) {
                    System.err.println("\nWARNING: Invalid option. Please input a valid number for the option:\n");
                    continue Loop2;
                }
                if ((choice <= 0) || (choice > keySizes.length)) {
                    System.err.println("\nWARNING: Invalid option. Please input a valid option:\n");
                } else {
                    cd.setKeySize(keySizes[choice - 1]);
                    valid = true;
                }
            }
        } catch (IOException ioe) {
            System.err.println("ERROR: Fail to read key size from the command prompt.");
            sawsDebugLog.write("ERROR: Fail to read key size from the command prompt.");
        }
        return cd;
    }
