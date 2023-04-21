    public static void main(String[] args) {
        try {
            File iFile = new File("D:/Project/MeAndBeningWorkspace/SmsApp/temp/coba.xml");
            FileInputStream fis = new FileInputStream(iFile);
            FileChannel channel = fis.getChannel();
            MappedByteBuffer mbb = channel.map(MapMode.READ_ONLY, 0, iFile.length());
            System.out.println("tes");
        } catch (FileNotFoundException f) {
        } catch (IOException i) {
        }
    }
