    protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen) throws IllegalBlockSizeException, BadPaddingException {
        if (SigDebug.isActive()) SigDebug.write("MSRSACipherFactoryImpl: engineDoFinal entered");
        byte[] outputData = null;
        buffer.write(input, inputOffset, inputLen);
        byte[] inputData = buffer.toByteArray();
        if (ciphermode == Cipher.DECRYPT_MODE) {
            if (KeySize != inputData.length) throw new IllegalBlockSizeException("MSRSA length of data to be decrypted must equal keysize " + KeySize + "  " + inputData.length);
            outputData = MSF.MSrsaDecrypt(PaddingAlgorithm, inputData);
        }
        if (ciphermode == Cipher.ENCRYPT_MODE) {
            if (KeySize < inputData.length) throw new IllegalBlockSizeException("MSRSA length of data to be decrypted must be <= keysize " + KeySize + "  " + inputData.length);
            outputData = MSF.MSrsaEncrypt(PaddingAlgorithm, inputData);
        }
        buffer.reset();
        return outputData;
    }
