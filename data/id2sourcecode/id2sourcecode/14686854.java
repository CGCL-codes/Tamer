    private void doEncrypt() throws IOException {
        String cipherBaseFilename;
        if (borkFilenames) cipherBaseFilename = hexify(sha1(infile.getName())); else cipherBaseFilename = basename(infile);
        outfile = new File(outputDir, cipherBaseFilename + ".bork");
        if (outfile.exists()) {
            if (sameEncryptedFile(infile, outfile)) {
                skipped = true;
                return;
            } else {
                String outfileName = outfile.getName();
                outfile = null;
                throw new IOException("Encrypted output clash: plaintext file " + infile + " maps to encrypted file " + outfileName + " which is from another source");
            }
        }
        FileInputStream input = null;
        FileOutputStream cleartextOutput = null;
        try {
            input = new FileInputStream(infile);
            cleartextOutput = new FileOutputStream(outfile);
            cleartextOutput.write(MAGIC);
            writeShort(cleartextOutput, VERSION);
            writeString(cleartextOutput, CIPHER_NAME);
            Cipher cipher = createCipher(CIPHER_NAME, createSessionKey(password, cipherBaseFilename));
            int crcPos = (int) cleartextOutput.getChannel().position();
            cleartextOutput.getChannel().position(crcPos + 4);
            cipher.skip(4);
            CipherOutputStream encryptedOutput = new CipherOutputStream(cleartextOutput, cipher);
            writeString(encryptedOutput, infile.getName());
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) encryptedOutput.write(buffer, 0, bytesRead);
            cleartextOutput.getChannel().position(crcPos);
            cipher.reset();
            writeInt(encryptedOutput, (int) encryptedOutput.getCRC());
            close(cleartextOutput);
            outfile.setLastModified(infile.lastModified());
        } finally {
            close(input);
            close(cleartextOutput);
        }
    }
