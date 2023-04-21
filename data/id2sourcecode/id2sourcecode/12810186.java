    @Amf
    public Navigation checkHaiku(HaikuData haikuData) throws NoSuchAlgorithmException {
        HaikuComment comment = new HaikuComment();
        if (StringUtil.isEmpty(haikuData.getGo1()) || StringUtil.isEmpty(haikuData.getShichi()) || StringUtil.isEmpty(haikuData.getGo2())) {
            comment.setPoint(0);
            comment.setComment("とりあえず　五七五には　しとこうか");
            return AmfResponse.to(comment);
        }
        String haikuText = haikuData.getGo1() + haikuData.getShichi() + haikuData.getGo2();
        if (this.isNGWord(haikuText)) {
            comment.setPoint(0);
            comment.setComment("下ネタを　言ったあなたは　負け組よ！");
            return AmfResponse.to(comment);
        }
        MessageDigest md5 = MessageDigest.getInstance("SHA-1");
        byte[] digestByte = md5.digest(haikuText.getBytes());
        int point = 0;
        for (int i = 0; i < digestByte.length; i++) {
            point += digestByte[i];
        }
        point = Math.abs(point) % 100;
        comment.setComment(commentMap.get(point % commentMap.size()).replace("{0}", haikuData.getGo2()));
        comment.setPoint(point);
        return AmfResponse.to(comment);
    }
