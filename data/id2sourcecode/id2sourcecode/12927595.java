    public static void get_FDT_data() {
        try {
            FileWriter file_writer = new FileWriter("xml_data/FDT_data_dump.xml");
            BufferedWriter file_buffered_writer = new BufferedWriter(file_writer);
            URL fdt = new URL("http://opendata.5t.torino.it/get_fdt");
            URLConnection url_connection = fdt.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(url_connection.getInputStream()));
            String input_line;
            int num_lines = 0;
            while ((input_line = in.readLine()) != null) {
                file_buffered_writer.write(input_line + "\n");
                num_lines++;
            }
            System.out.println("FDT :: Writed " + num_lines + " lines.");
            in.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
