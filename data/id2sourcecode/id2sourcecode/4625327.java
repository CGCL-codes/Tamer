    private double[] getCDs() throws FitsException, IOException {
        BasicHDU hdu = getHDU(0);
        Header hdr = hdu.getHeader();
        int[] val;
        int[] position = positionRaDec();
        double ra = hdr.getDoubleValue("PC1_" + position[0], 99999);
        double dec = hdr.getDoubleValue("PC2_" + position[1], 99999);
        if (ra == 99999 || dec == 99999) {
            double incA = hdr.getDoubleValue("CDELT1", 99999);
            double incD = hdr.getDoubleValue("CDELT2", 99999);
            if (incA == 99999 || incD == 99999) {
                logger_.log(Level.SEVERE, "KeyWord \"PC\" isn't present in this fits");
                System.exit(-1);
            }
            double rota1 = hdr.getDoubleValue("CROTA1", 0);
            double rota2 = hdr.getDoubleValue("CROTA2", 0);
            double rota = rota1;
            if (rota1 == 0) {
                rota = rota2;
            } else {
                if (rota2 != 0) {
                    rota = (rota1 + rota2) / 2;
                }
            }
            ra = incA * Math.cos((rota / 180.) * Math.PI);
            dec = incD * Math.cos((rota / 180.) * Math.PI);
        }
        return new double[] { ra, dec };
    }
