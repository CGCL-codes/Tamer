    public static boolean save(File file, String str) {
        try {
            if (file != null) {
                BufferedReader reader = new BufferedReader(new StringReader(str));
                PrintWriter writer = new PrintWriter(new FileWriter(file));
                String line;
                while ((line = reader.readLine()) != null) writer.println(line);
                reader.close();
                writer.close();
                return true;
            }
        } catch (Exception e) {
            Util.error("SaveAs Failed!\n" + file, e, null);
        }
        return false;
    }
