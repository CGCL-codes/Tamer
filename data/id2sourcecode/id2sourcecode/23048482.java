    public static void main(String[] args) {
        try {
            Stub stub = (Stub) (new org.tempuri.ILOGIN_SERVserviceLocator().getILOGIN_SERVPort());
            org.tempuri.ILOGIN_SERVbindingStub s = (org.tempuri.ILOGIN_SERVbindingStub) stub;
            TLoginData loginData = new TLoginData();
            loginData.setLogin("s.startsev");
            String text = "1";
            String md5 = null;
            try {
                StringBuffer code = new StringBuffer();
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte bytes[] = text.getBytes();
                byte digest[] = messageDigest.digest(bytes);
                for (int i = 0; i < digest.length; ++i) {
                    code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF)).substring(1));
                }
                md5 = code.toString();
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println(md5);
            loginData.setPwdHash(md5);
            loginData.setLocalIP("93.85.154.8");
            loginData.setMACAddress("00-53-45-00-00-00");
            StringHolder str = new StringHolder();
            IntHolder res = new IntHolder();
            s.serv_LogIn(loginData, new IntHolder(), str, res);
            System.out.println(str.value);
        } catch (Exception ex) {
            System.out.println(ex);
            if (ex instanceof AxisFault) {
                ((AxisFault) ex).printStackTrace();
            }
        }
    }
