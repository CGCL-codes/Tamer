    public static String generatePboxVerilog(int inLength, int[] indexes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        int[] frequences = new int[inLength];
        for (int i = 0; i < indexes.length; i++) frequences[indexes[i]]++;
        int minF = Integer.MAX_VALUE;
        int maxF = Integer.MIN_VALUE;
        for (int i = 0; i < inLength; i++) {
            if ((frequences[i] < minF) && (frequences[i] != 0)) minF = frequences[i];
            if (frequences[i] > maxF) maxF = frequences[i];
        }
        writer.print("module Pbox_");
        writer.print(inLength);
        writer.append("_");
        writer.print(indexes.length);
        writer.append("_");
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        md.update(AArrayUtilities.ints2Bytes(indexes), 0, indexes.length * 4);
        byte[] hash = md.digest();
        byte[] smallHash = AArrayUtilities.xor(hash, 0, hash, 10, 10);
        writer.append(AStringUtilities.bytesToHex(smallHash));
        writer.print("(input wire [" + (inLength - 1) + ":0] in,");
        writer.println("output wire [" + (indexes.length - 1) + ":0] out);");
        writer.print("//unused bits: ");
        for (int i = 0; i < inLength; i++) {
            if (frequences[i] == 0) {
                writer.print(i);
                writer.print(" ");
            }
        }
        writer.println();
        writer.print("//bits used the least (");
        writer.print(minF);
        writer.print(" times): ");
        for (int i = 0; i < inLength; i++) {
            if (frequences[i] == minF) {
                writer.print(i);
                writer.print(" ");
            }
        }
        writer.println();
        writer.print("//bits used the most (");
        writer.print(maxF);
        writer.print(" times): ");
        for (int i = 0; i < inLength; i++) {
            if (frequences[i] == maxF) {
                writer.print(i);
                writer.print(" ");
            }
        }
        writer.println();
        writer.append("   assign out = {");
        for (int i = indexes.length - 1; i > 0; i--) {
            writer.append("in[");
            writer.print(indexes[i]);
            writer.print("],");
        }
        writer.append("in[");
        writer.print(indexes[0]);
        writer.println("]};");
        writer.println("endmodule");
        writer.println();
        writer.close();
        return out.toString();
    }
