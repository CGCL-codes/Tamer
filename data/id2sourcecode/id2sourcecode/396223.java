    static void runTestOnArray(Object[] obj) throws java.lang.Exception {
        for (int i = 0; i < cs.length; i++) {
            System.out.print(obj[i].getClass().getName() + ": ");
            ByteArrayOutputStream bo1 = null, bo2 = null;
            try {
                SMPPPacket pak = (SMPPPacket) obj[i];
                if (pak == null) continue;
                bo1 = new ByteArrayOutputStream();
                bo2 = new ByteArrayOutputStream();
                pak.writeTo(bo1);
                SMPPPacket.readPacket(new ByteArrayInputStream(bo1.toByteArray())).writeTo(bo2);
                int ret = checkSize(pak);
                if (ret == 0) {
                    System.out.print("pass.");
                } else {
                    System.out.print("FAILED (" + ret + ")");
                    switch(ret) {
                        case 1:
                            System.out.print("\n    getCommandLen() = " + pak.getCommandLen());
                            System.out.print("\n    bytes: " + TestUtils.showBytes(bo1.toByteArray()));
                            break;
                        case 2:
                            System.out.print("\n    getCommandLen() = " + pak.getCommandLen());
                            System.out.print("\n    bytes 1: " + TestUtils.showBytes(bo1.toByteArray()) + "\n    bytes 2: " + TestUtils.showBytes(bo2.toByteArray()));
                            break;
                    }
                }
            } catch (Exception x) {
                System.out.print("FAILED..exception." + "\n    bytes1: " + TestUtils.showBytes(bo1.toByteArray()) + "\n    bytes2: " + TestUtils.showBytes(bo1.toByteArray()) + "\n");
                x.printStackTrace();
            }
            System.out.print("\n");
        }
    }
