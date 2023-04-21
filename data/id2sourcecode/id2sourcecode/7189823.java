    public static void main(String[] args) throws IOException {
        InputStream input = new ByteArrayInputStream(args[1].getBytes());
        StringBuilder toPrint = new StringBuilder();
        if (args[0].startsWith("o")) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            OutputStream out = obfuscate(bytes);
            int read = input.read();
            while (read >= 0) {
                out.write(read);
                read = input.read();
            }
            byte[] receiptBytes = bytes.toByteArray();
            for (int b = 0; b < receiptBytes.length; b++) {
                int chr = (receiptBytes[b] + 256) % 256;
                toPrint.append(HEX_CHARS[chr >>> 4]);
                toPrint.append(HEX_CHARS[chr & 0xf]);
            }
        } else if (args[0].startsWith("u")) {
            input = unobfuscate(input);
            InputStreamReader reader = new InputStreamReader(input);
            int read = reader.read();
            while (read >= 0) {
                toPrint.append((char) read);
                read = reader.read();
            }
        } else throw new IllegalArgumentException("First argument must start with o or u");
        System.out.println(toPrint.toString());
    }
