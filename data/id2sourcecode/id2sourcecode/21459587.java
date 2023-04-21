    public static void main(String[] args) {
        if (args.length == 3) {
            try {
                System.out.println("################################################################################");
                System.out.println("load and initialize module: " + args[0]);
                Module pkcs11Module1 = Module.getInstance(args[0]);
                pkcs11Module1.initialize(null);
                System.out.println("load and initialize module: " + args[1]);
                Module pkcs11Module2 = Module.getInstance(args[1]);
                pkcs11Module2.initialize(null);
                System.out.println("################################################################################");
                System.out.println("################################################################################");
                System.out.println("getting tokens");
                Slot[] slotsWithToken1 = pkcs11Module1.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);
                if (slotsWithToken1.length < 1) {
                    System.err.println("No token present for module: " + pkcs11Module1.getInfo());
                    System.exit(1);
                }
                Token token1 = slotsWithToken1[0].getToken();
                System.out.println("________________________________________________________________________________");
                System.out.println("token #1:");
                System.out.println(token1.getTokenInfo());
                System.out.println("________________________________________________________________________________");
                Slot[] slotsWithToken2 = pkcs11Module2.getSlotList(Module.SlotRequirement.TOKEN_PRESENT);
                if (slotsWithToken2.length < 1) {
                    System.err.println("No token present for module: " + pkcs11Module2.getInfo());
                    System.exit(1);
                }
                Token token2 = slotsWithToken2[0].getToken();
                System.out.println("________________________________________________________________________________");
                System.out.println("token #2:");
                System.out.println(token2.getTokenInfo());
                System.out.println("________________________________________________________________________________");
                System.out.println("################################################################################");
                System.out.println("################################################################################");
                System.out.println("opening sessions");
                Session session1 = token1.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RO_SESSION, null, null);
                Session session2 = token2.openSession(Token.SessionType.SERIAL_SESSION, Token.SessionReadWriteBehavior.RO_SESSION, null, null);
                System.out.println("opening data file: " + args[2]);
                InputStream dataInputStream = new FileInputStream(args[2]);
                Mechanism digestMechanism = Mechanism.SHA_1;
                System.out.println("initializing sessions for hashing");
                session1.digestInit(digestMechanism);
                session2.digestInit(digestMechanism);
                byte[] dataBuffer = new byte[1024];
                byte[] helpBuffer;
                int bytesRead;
                while ((bytesRead = dataInputStream.read(dataBuffer)) >= 0) {
                    helpBuffer = new byte[bytesRead];
                    System.arraycopy(dataBuffer, 0, helpBuffer, 0, bytesRead);
                    session1.digestUpdate(helpBuffer);
                    session2.digestUpdate(helpBuffer);
                    Arrays.fill(helpBuffer, (byte) 0);
                }
                Arrays.fill(dataBuffer, (byte) 0);
                byte[] digestValue1 = session1.digestFinal();
                byte[] digestValue2 = session2.digestFinal();
                System.out.println("The SHA-1 hash value #1 is: " + new BigInteger(1, digestValue1).toString(16));
                System.out.println("The SHA-1 hash value #2 is: " + new BigInteger(1, digestValue2).toString(16));
                if (Arrays.equals(digestValue1, digestValue2)) {
                    System.out.println("The hash values are equal.");
                } else {
                    System.out.println("The hash values are different. Test failed");
                }
                System.out.println("closing sessions");
                session1.closeSession();
                session2.closeSession();
                System.out.println("################################################################################");
                System.out.println("################################################################################");
                System.out.println("verifying hash with software digest");
                MessageDigest softwareDigestEngine = MessageDigest.getInstance("SHA-1");
                dataInputStream = new FileInputStream(args[2]);
                while ((bytesRead = dataInputStream.read(dataBuffer)) >= 0) {
                    softwareDigestEngine.update(dataBuffer, 0, bytesRead);
                }
                dataInputStream.close();
                byte[] softwareDigestValue = softwareDigestEngine.digest();
                Arrays.fill(dataBuffer, (byte) 0);
                System.out.println("The software digest value is: " + new BigInteger(1, softwareDigestValue).toString(16));
                if (Arrays.equals(digestValue1, softwareDigestValue) && Arrays.equals(digestValue2, softwareDigestValue)) {
                    System.out.println("All SHA-1 hash values are equal. Test passed successfully.");
                } else {
                    System.out.println("Verification of hash value FAILED!");
                }
                System.out.println("################################################################################");
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        } else {
            printUsage();
        }
        System.gc();
    }
