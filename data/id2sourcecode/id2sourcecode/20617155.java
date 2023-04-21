    @Override
    protected boolean readEphCoeff(double jultime) {
        boolean result = false;
        if ((jultime < this.startepoch) || (jultime >= this.finalepoch)) {
            return result;
        }
        if ((jultime < this.ephemerisdates[1]) || (jultime >= this.ephemerisdates[2])) {
            int mantissa1 = 0;
            int mantissa2 = 0;
            int exponent = 0;
            int i = 0;
            int records = 0;
            int j = 0;
            String filename = " ";
            String line = " ";
            try {
                for (i = 0; i < startfiledates.length - 1; i++) {
                    if ((jultime >= startfiledates[i]) && (jultime < startfiledates[i + 1])) {
                        ephemerisdates[1] = startfiledates[i];
                        ephemerisdates[2] = startfiledates[i + 1];
                        filename = filenames[i];
                        records = (int) (ephemerisdates[2] - ephemerisdates[1]) / intervalduration;
                    }
                }
                filename = this.patheph + filename;
                FileReader file = new FileReader(filename);
                BufferedReader buff = new BufferedReader(file);
                for (j = 1; j <= records; j++) {
                    line = buff.readLine();
                    for (i = 2; i <= 341; i++) {
                        line = buff.readLine();
                        if (i > 2) {
                            mantissa1 = Integer.parseInt(line.substring(4, 13));
                            mantissa2 = Integer.parseInt(line.substring(13, 22));
                            exponent = Integer.parseInt(line.substring(24, 26));
                            if (line.substring(23, 24).equals("+")) {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) - 1)] = mantissa1 * Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
                            } else {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) - 1)] = mantissa1 * Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
                            }
                            if (line.substring(1, 2).equals("-")) {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) - 1)] = -ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) - 1)];
                            }
                        }
                        if ((i > 2) & (i < 341)) {
                            mantissa1 = Integer.parseInt(line.substring(30, 39));
                            mantissa2 = Integer.parseInt(line.substring(39, 48));
                            exponent = Integer.parseInt(line.substring(50, 52));
                            if (line.substring(49, 50).equals("+")) {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + 3 * (i - 2)] = mantissa1 * Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
                            } else {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + 3 * (i - 2)] = mantissa1 * Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
                            }
                            if (line.substring(27, 28).equals("-")) {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + 3 * (i - 2)] = -ephemeriscoefficients[(j - 1) * numbersperinterval + 3 * (i - 2)];
                            }
                        }
                        if (i < 341) {
                            mantissa1 = Integer.parseInt(line.substring(56, 65));
                            mantissa2 = Integer.parseInt(line.substring(65, 74));
                            exponent = Integer.parseInt(line.substring(76, 78));
                            if (line.substring(75, 76).equals("+")) {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) + 1)] = mantissa1 * Math.pow(10, (exponent - 9)) + mantissa2 * Math.pow(10, (exponent - 18));
                            } else {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) + 1)] = mantissa1 * Math.pow(10, -(exponent + 9)) + mantissa2 * Math.pow(10, -(exponent + 18));
                            }
                            if (line.substring(53, 54).equals("-")) {
                                ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) + 1)] = -ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) + 1)];
                            }
                        }
                    }
                }
                buff.close();
                result = true;
            } catch (IOException e) {
                System.out.println("Error = " + e.toString());
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Error = " + e.toString());
            }
        } else {
            result = true;
        }
        return result;
    }
