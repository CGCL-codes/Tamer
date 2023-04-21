    public static String generate(File source) {
        byte[] SHA = new byte[20];
        String SHADigest = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            FileInputStream inputStream = new FileInputStream(source);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            digest.update(data);
            SHA = digest.digest();
            for (int i = 0; i < SHA.length; i++) SHADigest += SHA[i];
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NO SUCH ALGORITHM EXCEPTION: " + e.getMessage());
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND EXCEPTION: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO EXCEPTION: " + e.getMessage());
        }
        return SHADigest.trim();
    }
