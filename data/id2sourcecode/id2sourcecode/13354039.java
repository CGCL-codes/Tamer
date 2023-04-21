    protected void uploadMultipartFile(File sourceFile, String directory, String fileName, Encryption encryption) throws InterruptedException, InternalException, CloudException {
        File toDelete = null;
        File toUpload;
        if (encryption == null) {
            toUpload = sourceFile;
        } else {
            try {
                FileInputStream input = new FileInputStream(sourceFile);
                FileOutputStream output;
                toDelete = File.createTempFile(fileName, ".enc");
                output = new FileOutputStream(toDelete);
                encryption.encrypt(input, output);
                output.flush();
                output.close();
                input.close();
                toUpload = toDelete;
            } catch (EncryptionException e) {
                e.printStackTrace();
                throw new InternalException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new InternalException(e);
            }
        }
        try {
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(toUpload));
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                BufferedOutputStream output;
                byte[] buffer = new byte[32768];
                long count = 0;
                int b, partNumber = 1;
                File part;
                part = File.createTempFile(fileName, ".part." + partNumber);
                output = new BufferedOutputStream(new FileOutputStream(part));
                while ((b = input.read(buffer, 0, buffer.length)) > 0) {
                    count += b;
                    output.write(buffer, 0, b);
                    messageDigest.update(buffer, 0, b);
                    if (count >= 2000000000L) {
                        int tries = 5;
                        output.flush();
                        output.close();
                        while (true) {
                            tries--;
                            try {
                                put(directory, null, part);
                                break;
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                                if (tries < 1) {
                                    throw new InternalException("Unable to complete upload for part " + partNumber + " of " + part.getAbsolutePath());
                                }
                            }
                        }
                        part.delete();
                        partNumber++;
                        part = File.createTempFile(fileName, ".part." + partNumber);
                        output = new BufferedOutputStream(new FileOutputStream(part));
                        count = 0L;
                    }
                }
                if (count > 0L) {
                    int tries = 5;
                    output.flush();
                    output.close();
                    while (true) {
                        tries--;
                        try {
                            put(directory, fileName + ".part." + partNumber, part);
                            break;
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            if (tries < 1) {
                                throw new InternalException("Unable to complete upload for part " + partNumber + " of " + part.getAbsolutePath());
                            }
                        }
                    }
                    part.delete();
                }
                String content = "parts=" + partNumber + "\nchecksum=" + toBase64(messageDigest.digest()) + "\n";
                if (encryption != null) {
                    content = content + "encrypted=true\n";
                    content = content + "encryptionVersion=" + encryption.getClass().getName() + "\n";
                } else {
                    content = content + "encrypted=false\n";
                }
                content = content + "\nlength=" + sourceFile.length();
                int tries = 5;
                while (true) {
                    tries--;
                    try {
                        put(directory, fileName + ".properties", content);
                        break;
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                        if (tries < 1) {
                            throw new InternalException("Unable to complete upload for properties of " + part.getAbsolutePath());
                        }
                    }
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new InternalException(e);
            } finally {
                if (toDelete != null) {
                    toDelete.delete();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalException(e);
        }
    }
