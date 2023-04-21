    private synchronized void getPseudoIEEE802Address(byte[] ieee802Addr) {
        byte[] currentTime = String.valueOf(getUniqueTimeStamp()).getBytes();
        byte[] localHostAddress = getLocalHostAddress();
        byte[] inMemObj = new Object().toString().getBytes();
        byte[] freeMemory = String.valueOf(Runtime.getRuntime().freeMemory()).getBytes();
        byte[] totalMemory = String.valueOf(Runtime.getRuntime().totalMemory()).getBytes();
        byte[] hashcode = null;
        byte[] bytes = new byte[freeMemory.length + totalMemory.length + currentTime.length + localHostAddress.length + inMemObj.length];
        int bytesPos = 0;
        System.arraycopy(currentTime, 0, bytes, bytesPos, currentTime.length);
        bytesPos += currentTime.length;
        System.arraycopy(localHostAddress, 0, bytes, bytesPos, localHostAddress.length);
        bytesPos += localHostAddress.length;
        System.arraycopy(inMemObj, 0, bytes, bytesPos, inMemObj.length);
        bytesPos += inMemObj.length;
        System.arraycopy(freeMemory, 0, bytes, bytesPos, freeMemory.length);
        bytesPos += freeMemory.length;
        System.arraycopy(totalMemory, 0, bytes, bytesPos, totalMemory.length);
        bytesPos += totalMemory.length;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            hashcode = md5.digest(bytes);
        } catch (Exception e) {
        }
        System.arraycopy(hashcode, 0, ieee802Addr, 0, 6);
        ieee802Addr[0] |= 0x80;
    }
