    @Override
    protected boolean readEphCoeff(double jultime) {
        boolean result = false;
        if ((jultime < this.startepoch) || (jultime >= this.finalepoch)) {
            return result;
        }
        if ((jultime < this.ephemerisdates[1]) || (jultime >= this.ephemerisdates[2])) {
            int i = 0;
            int records = 0;
            int j = 0;
            String filename = " ";
            char[] cline = new char[70];
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
                for (j = 1; j <= records; j++) {
                    file.read(cline, 0, 13);
                    for (i = 2; i <= 244; i++) {
                        file.read(cline, 0, 70);
                        cline[19] = 'e';
                        cline[42] = 'e';
                        cline[65] = 'e';
                        if (i > 2) {
                            ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) - 1)] = Double.parseDouble(String.valueOf(cline, 1, 22));
                        }
                        if (i > 2) {
                            ephemeriscoefficients[(j - 1) * numbersperinterval + 3 * (i - 2)] = Double.parseDouble(String.valueOf(cline, 24, 22));
                        }
                        if (i < 244) {
                            ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) + 1)] = Double.parseDouble(String.valueOf(cline, 47, 22));
                        }
                    }
                }
                file.close();
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
