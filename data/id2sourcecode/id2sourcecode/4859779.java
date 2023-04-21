    public void close() throws IOException {
        super.close();
        byte[] dig = d.digest();
        if (dig.length != hash.length) throw new Error("Hash did not match (different lengths)");
        for (int i = 0; i < dig.length; i++) if (dig[i] != hash[i]) throw new Error("Wrong hash!\n" + "Id: " + HexUtil.byteArrToHex(hash) + "\n" + "Generated: " + HexUtil.byteArrToHex(dig) + "\n");
    }
