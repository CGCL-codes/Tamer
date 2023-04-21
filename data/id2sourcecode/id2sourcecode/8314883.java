    void exec() {
        System.out.print("Command to Execute: ");
        FileOutputStream foutp = null;
        try {
            foutp = new FileOutputStream("keyboardhero/Tmp.java");
            foutp.write(("package keyboardhero;\n" + "\n" + "import java.io.*;\n" + "\n" + "final class Tmp {\n" + "	public static void main(String[] args) {\n" + "		" + BUFFERED_RDR.readLine() + "\n" + "	}\n" + "}\n").getBytes());
            foutp.close();
            String ln;
            Process process = Runtime.getRuntime().exec("javac -classpath .;lib/jogl.jar;lib/gluegen-rt.jar keyboardhero/Tmp.java");
            BufferedReader buff = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((ln = buff.readLine()) != null) {
                System.out.println(ln);
            }
            boolean isOk = true;
            buff = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((ln = buff.readLine()) != null) {
                isOk = false;
                System.out.println(ln);
            }
            process.waitFor();
            if (isOk) {
                process = Runtime.getRuntime().exec("java keyboardhero.Tmp");
                buff = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((ln = buff.readLine()) != null) {
                    System.out.println(ln);
                }
                buff = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((ln = buff.readLine()) != null) {
                    System.out.println(ln);
                }
                process.waitFor();
            }
        } catch (Exception e) {
            if (Util.getDebugLevel() > 80) e.printStackTrace();
        } finally {
            if (foutp != null) {
                try {
                    foutp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
