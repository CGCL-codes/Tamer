    public static void main(String[] args) {
        int tmpDenom = 0;
        int tmpNumCoupons = 0;
        long tmpID = 0;
        BufferedReader reader = null;
        Coupon tmpCoupon;
        String orgName;
        String expDate;
        boolean areTransferable;
        long tmpSerial = 1000000;
        reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            FileWriter outFile = new FileWriter("coupons.db");
            PrintWriter output = new PrintWriter(outFile);
            System.out.println("");
            System.out.print("Enter organization name: ");
            orgName = reader.readLine();
            System.out.print("Enter coupon expiration date: ");
            expDate = reader.readLine();
            System.out.print("Should these coupons be transferable? (Y/N): ");
            if (reader.readLine() == "Y") areTransferable = true; else areTransferable = false;
            System.out.println();
            System.out.print("Enter denomination to generate (-1 to quit) : ");
            tmpDenom = new Integer(reader.readLine()).intValue();
            while (tmpDenom >= 0) {
                System.out.println("");
                System.out.print("Enter number of coupons to generate: ");
                tmpNumCoupons = new Integer(reader.readLine()).intValue();
                System.out.println("Generating " + tmpNumCoupons + " coupons of denimination " + tmpDenom + "... ");
                SecureRandom ran = new SecureRandom();
                byte[] bb = new byte[20];
                ran.nextBytes(bb);
                String w = cryptix.util.core.Hex.dumpString(bb);
                System.out.println("Generating key for " + tmpDenom + " denominated coupons...");
                KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
                keyGen.initialize(1024, ran);
                KeyPair pair = keyGen.generateKeyPair();
                System.out.println("key generated...");
                FileWriter outPublicKeyFile = new FileWriter(tmpDenom + "-public.key");
                PrintWriter pubOutput = new PrintWriter(outPublicKeyFile);
                FileWriter outPrivateKeyFile = new FileWriter(tmpDenom + "-private.key");
                PrintWriter privOutput = new PrintWriter(outPrivateKeyFile);
                System.out.println("Private key: " + pair.getPrivate().toString());
                System.out.println("Public key: " + pair.getPublic().toString());
                System.out.println("Saving keys...");
                CryptixRSAPublicKey pub = (CryptixRSAPublicKey) pair.getPublic();
                pubOutput.println(pub.getModulus());
                pubOutput.println(pub.getExponent());
                pubOutput.close();
                outPublicKeyFile.close();
                CryptixRSAPrivateKey priv = (CryptixRSAPrivateKey) pair.getPrivate();
                RSAFactors factors = (RSAFactors) priv;
                privOutput.println(priv.getExponent());
                privOutput.println(factors.getP());
                privOutput.println(factors.getQ());
                privOutput.println(factors.getInverseOfQModP());
                privOutput.close();
                outPrivateKeyFile.close();
                System.out.println("Generating coupons...");
                for (int i = 1; i <= tmpNumCoupons; i++) {
                    SecureRandom tmpRdn = new SecureRandom();
                    tmpID = (long) (tmpRdn.nextLong());
                    if (tmpID < 0) {
                        tmpID = tmpID * -1;
                    }
                    tmpCoupon = new Coupon(tmpSerial++, tmpID, tmpDenom, expDate, areTransferable, orgName);
                    System.out.println(tmpCoupon.toString());
                    tmpCoupon.setBankSecretKey((PrivateKey) (pair.getPrivate()));
                    tmpCoupon.createBankSignature();
                    tmpCoupon.save(output);
                }
                System.out.println(" ");
                System.out.print("Enter denomination to generate (-1 to quit) : ");
                tmpDenom = new Integer(reader.readLine()).intValue();
            }
            output.close();
            reader.close();
        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
    }
