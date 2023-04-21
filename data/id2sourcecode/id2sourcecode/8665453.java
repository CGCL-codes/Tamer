    public static void main(String args[]) {
        SshWrapper ssh = new SshWrapper();
        try {
            byte[] buffer = new byte[256];
            ssh.connect(args[0], 22);
            ssh.login("marcus", "xxxxx");
            ssh.setPrompt("marcus");
            System.out.println("after login");
            ssh.send("ls -l");
            ssh.read(buffer);
            System.out.println(new String(buffer));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
