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
            char[] cline = new char[80];
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
                    for (i = 2; i <= 341; i++) {
                        file.read(cline, 0, 79);
                        cline[22] = 'e';
                        cline[48] = 'e';
                        cline[74] = 'e';
                        if (i > 2) {
                            ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) - 1)] = Double.parseDouble(String.valueOf(cline, 1, 25));
                        }
                        if ((i > 2) & (i < 341)) {
                            ephemeriscoefficients[(j - 1) * numbersperinterval + 3 * (i - 2)] = Double.parseDouble(String.valueOf(cline, 27, 25));
                        }
                        if (i < 341) {
                            ephemeriscoefficients[(j - 1) * numbersperinterval + (3 * (i - 2) + 1)] = Double.parseDouble(String.valueOf(cline, 53, 25));
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
