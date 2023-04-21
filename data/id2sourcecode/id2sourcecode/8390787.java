    private void verifyDigests() throws InvalidContentException {
        for (int i = 0; i < digests.length; i++) {
            byte rc[] = digests[i].digest();
            if (!MessageDigest.isEqual(result[i], rc)) throw new InvalidContentException(NLS.bind(SignedContentMessages.File_In_Jar_Is_Tampered, entry.getName(), bundleFile.getBaseFile()), null);
        }
    }
