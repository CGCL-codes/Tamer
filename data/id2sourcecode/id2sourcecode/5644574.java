    public static Map parseC2SRequest(byte[] aReq) {
        String lHost = null;
        String lOrigin = null;
        String lLocation = null;
        String lPath = null;
        String lSubProt = null;
        String lDraft = null;
        String lSecKey1 = null;
        String lSecKey2 = null;
        byte[] lSecKey3 = new byte[8];
        Boolean lIsSecure = false;
        Long lSecNum1 = null;
        Long lSecNum2 = null;
        byte[] lSecKeyResp = new byte[8];
        Map lRes = new FastMap();
        int lReqLen = aReq.length;
        String lRequest = "";
        try {
            lRequest = new String(aReq, "US-ASCII");
        } catch (Exception lEx) {
        }
        if (lRequest.indexOf("policy-file-request") >= 0) {
            lRes.put("policy-file-request", lRequest);
            return lRes;
        }
        lIsSecure = (lRequest.indexOf("Sec-WebSocket") > 0);
        if (lIsSecure) {
            lReqLen -= 8;
            for (int lIdx = 0; lIdx < 8; lIdx++) {
                lSecKey3[lIdx] = aReq[lReqLen + lIdx];
            }
        }
        int lPos = lRequest.indexOf("Host:");
        lPos += 6;
        lHost = lRequest.substring(lPos);
        lPos = lHost.indexOf("\r\n");
        lHost = lHost.substring(0, lPos);
        lPos = lRequest.indexOf("Origin:");
        lPos += 8;
        lOrigin = lRequest.substring(lPos);
        lPos = lOrigin.indexOf("\r\n");
        lOrigin = lOrigin.substring(0, lPos);
        lPos = lRequest.indexOf("GET");
        lPos += 4;
        lPath = lRequest.substring(lPos);
        lPos = lPath.indexOf("HTTP");
        lPath = lPath.substring(0, lPos - 1);
        lLocation = "ws://" + lHost + lPath;
        lPos = lRequest.indexOf("WebSocket-Protocol:");
        if (lPos > 0) {
            lPos += 20;
            lSubProt = lRequest.substring(lPos);
            lPos = lSubProt.indexOf("\r\n");
            lSubProt = lSubProt.substring(0, lPos);
        }
        lPos = lRequest.indexOf("Sec-WebSocket-Draft:");
        if (lPos > 0) {
            lPos += 21;
            lDraft = lRequest.substring(lPos);
            lPos = lDraft.indexOf("\r\n");
            lDraft = lDraft.substring(0, lPos);
        }
        lPos = lRequest.indexOf("Sec-WebSocket-Key1:");
        if (lPos > 0) {
            lPos += 20;
            lSecKey1 = lRequest.substring(lPos);
            lPos = lSecKey1.indexOf("\r\n");
            lSecKey1 = lSecKey1.substring(0, lPos);
            lSecNum1 = calcSecKeyNum(lSecKey1);
        }
        lPos = lRequest.indexOf("Sec-WebSocket-Key2:");
        if (lPos > 0) {
            lPos += 20;
            lSecKey2 = lRequest.substring(lPos);
            lPos = lSecKey2.indexOf("\r\n");
            lSecKey2 = lSecKey2.substring(0, lPos);
            lSecNum2 = calcSecKeyNum(lSecKey2);
        }
        if (lSecNum1 != null && lSecNum2 != null) {
            BigInteger lSec1 = new BigInteger(lSecNum1.toString());
            BigInteger lSec2 = new BigInteger(lSecNum2.toString());
            byte[] l128Bit = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            byte[] lTmp;
            int lOfs;
            lTmp = lSec1.toByteArray();
            int lIdx = lTmp.length;
            int lCnt = 0;
            while (lIdx > 0 && lCnt < 4) {
                lIdx--;
                lCnt++;
                l128Bit[4 - lCnt] = lTmp[lIdx];
            }
            lTmp = lSec2.toByteArray();
            lIdx = lTmp.length;
            lCnt = 0;
            while (lIdx > 0 && lCnt < 4) {
                lIdx--;
                lCnt++;
                l128Bit[8 - lCnt] = lTmp[lIdx];
            }
            lTmp = lSecKey3;
            System.arraycopy(lSecKey3, 0, l128Bit, 8, 8);
            try {
                MessageDigest lMD = MessageDigest.getInstance("MD5");
                lSecKeyResp = lMD.digest(l128Bit);
            } catch (Exception lEx) {
            }
        }
        lRes.put(RequestHeader.WS_PATH, lPath);
        lRes.put(RequestHeader.WS_HOST, lHost);
        lRes.put(RequestHeader.WS_ORIGIN, lOrigin);
        lRes.put(RequestHeader.WS_LOCATION, lLocation);
        lRes.put(RequestHeader.WS_PROTOCOL, lSubProt);
        lRes.put(RequestHeader.WS_SECKEY1, lSecKey1);
        lRes.put(RequestHeader.WS_SECKEY2, lSecKey2);
        lRes.put(RequestHeader.WS_DRAFT, lDraft);
        lRes.put("isSecure", lIsSecure);
        lRes.put("secKeyResponse", lSecKeyResp);
        return lRes;
    }
