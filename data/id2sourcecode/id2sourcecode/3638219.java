    public List<byte[]> getHashes(File mailbox) throws IOException, GeneralSecurityException {
        FileInputStream fis = new FileInputStream(mailbox);
        MessageDigest md = MessageDigest.getInstance("MD5");
        List<byte[]> hashes = new ArrayList();
        try {
            int nextByte;
            boolean include = false, firstMessage = true;
            byte[] line = new byte[initialField.length], temp;
            int current = 0;
            while ((nextByte = fis.read()) != -1) {
                if (nextByte == 0x0a || nextByte == 0x0d) {
                    if (current == from.length && areEqualArrays(from, line)) {
                        if (!firstMessage) hashes.add(md.digest());
                        include = false;
                    } else if (current == initialField.length && areEqualArrays(initialField, line)) {
                        md.update(initialField);
                        include = true;
                    } else if (include) {
                        md.update(line, 0, current);
                    }
                    current = 0;
                    line = new byte[initialField.length];
                    firstMessage = false;
                    continue;
                }
                line[current] = (byte) (nextByte & 0xff);
                current++;
                if (current == line.length) {
                    temp = new byte[line.length * 2];
                    System.arraycopy(line, 0, temp, 0, current);
                    line = temp;
                }
            }
            hashes.add(md.digest());
            return hashes;
        } finally {
            try {
                fis.close();
            } catch (IOException ioe) {
            }
        }
    }
