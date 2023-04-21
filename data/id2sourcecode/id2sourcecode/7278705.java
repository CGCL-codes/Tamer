    public String gameState() {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            String boardStatus = getGameStateXML();
            byte[] hash = md5.digest(boardStatus.getBytes());
            String stateBoardMsg = "";
            for (int j = 0; j < hash.length; j++) {
                stateBoardMsg += Integer.toHexString(0xFF & hash[j]);
            }
            return stateBoardMsg;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }
