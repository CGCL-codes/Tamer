    private void doHash() {
        try {
            keygen = KeyGenerator.getInstance(cipheralgorithm);
            myKey = keygen.generateKey();
            myDigest = MessageDigest.getInstance(mdalgorithm);
            digestbytes = myDigest.digest(txtCleartext.getText().getBytes());
            txtHash.setText(new String(digestbytes));
            cipher = Cipher.getInstance(cipheralgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, myKey);
            cipherbytes = myDigest.digest();
            txtEncryptedHash.setText(new String(cipherbytes));
            lblStatus.setText("Status: Hash computed and encrypted");
        } catch (Exception e) {
            lblStatus.setText("Status: Error - Exception: " + e.getMessage());
        }
    }
