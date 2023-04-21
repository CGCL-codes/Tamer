    @Override
    public boolean execute(ArrayList<String> input, XOperationCollector xCol, PrintStream out) {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "Blowfish");
        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            DataOutputStream outFile = new DataOutputStream(new FileOutputStream(new File(input.get(2))));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encryptedLogin = cipher.doFinal(input.get(0).getBytes());
            byte[] encryptedPassword = cipher.doFinal(input.get(1).getBytes());
            outFile.write(encryptedLogin.length);
            outFile.write(encryptedPassword.length);
            outFile.write(encryptedLogin);
            outFile.write(encryptedPassword);
            outFile.flush();
            outFile.close();
            out.println("The new access file was created");
        } catch (NoSuchAlgorithmException e) {
            out.println(e);
        } catch (NoSuchPaddingException e) {
            out.println(e);
        } catch (BadPaddingException e) {
            out.println(e);
        } catch (IllegalBlockSizeException e) {
            out.println(e);
        } catch (FileNotFoundException e) {
            out.println(e);
        } catch (InvalidKeyException e) {
            out.println(e);
        } catch (IOException e) {
            out.println(e);
        }
        return true;
    }
