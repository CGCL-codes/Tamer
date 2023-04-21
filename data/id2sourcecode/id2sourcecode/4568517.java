    public static void encrypt(String uri) throws Exception {
        if (fileExists(uri)) {
            Document document = parseFile(uri);
            Key symmetricKey = GenerateSymmetricKey();
            Key keyEncryptKey = GenerateKeyEncryptionKey();
            storeKeyFile(keyEncryptKey);
            XMLCipher keyCipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES_KeyWrap);
            keyCipher.init(XMLCipher.WRAP_MODE, keyEncryptKey);
            EncryptedKey encryptedKey = keyCipher.encryptKey(document, symmetricKey);
            Element rootElement = document.getDocumentElement();
            Element elementToEncrypt = rootElement;
            XMLCipher xmlCipher = XMLCipher.getInstance(XMLCipher.AES_128);
            xmlCipher.init(XMLCipher.ENCRYPT_MODE, symmetricKey);
            EncryptedData encryptedDataElement = xmlCipher.getEncryptedData();
            KeyInfo keyInfo = new KeyInfo(document);
            keyInfo.add(encryptedKey);
            encryptedDataElement.setKeyInfo(keyInfo);
            boolean encryptContentsOnly = true;
            xmlCipher.doFinal(document, elementToEncrypt, encryptContentsOnly);
            writeEncryptedDocToFile(document, uri);
        }
    }
