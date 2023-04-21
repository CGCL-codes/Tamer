    public void flush() {
        try {
            System.out.println("Saving contents to DISK file...");
            FileOutputStream os = new FileOutputStream("DISK");
            os.write(data);
            os.close();
            System.out.println(readCount + " read operations and " + writeCount + " write operations performed");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
