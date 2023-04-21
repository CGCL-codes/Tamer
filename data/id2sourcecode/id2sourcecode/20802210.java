    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("PopulateFSDB START");
        _temp_dir = getServletContext().getRealPath(File.separatorChar + "WEB-INF" + File.separatorChar + "temp_datasets");
        upload(req);
        try {
            boolean is_new_dataset = false;
            boolean is_overwrite_dataset = false;
            boolean is_append_dataset = false;
            String selected_layer = req.getParameter("selected_layer");
            String dimension_size = req.getParameter("selected_dataset_table");
            String selected_dataset = req.getParameter("selected_dataset_name");
            String mode = req.getParameter("mode");
            String dataset_label = req.getParameter("dataset_label");
            String selected_project = req.getParameter("selected_project");
            System.out.println("selected_project = " + selected_project);
            if (mode.equals("new")) {
                is_new_dataset = true;
            }
            if (mode.equals("append")) {
                is_append_dataset = true;
            }
            if (mode.equals("overwrite")) {
                is_overwrite_dataset = true;
            }
            if (is_new_dataset) {
            }
            System.out.println("dimension_size " + dimension_size);
            FileResource file_resource1 = new FileResource(_filename);
            Debug.println("File " + _filename);
            CSVTableReader table_reader1 = new CSVTableReader(file_resource1, true);
            String error_message = validateAreaCodes(table_reader1, selected_layer);
            if (!error_message.equals("")) {
                res.getWriter().println("<font color='red'>Please check your CSV file: There is an error in the Area codes, as specified below: </font> ");
                res.getWriter().println("<br/>" + error_message);
                Debug.println("Please check your file: Error in Area codes!!! ");
                return;
            }
            FileResource file_resource = new FileResource(_filename);
            Debug.println("File " + _filename);
            CSVTableReader table_reader2 = new CSVTableReader(file_resource, true);
            error_message = validateValues(table_reader2);
            if (!error_message.equals("")) {
                res.getWriter().println("<font color='red'>Please check your CSV file: There is an error in the values column, as specified below: </font>");
                res.getWriter().println("<br/>" + error_message);
                Debug.println("Please check your file: Error in values!!! ");
                return;
            }
            FileResource file_resource3 = new FileResource(_filename);
            Debug.println("File " + _filename);
            CSVTableReader table_reader3 = new CSVTableReader(file_resource3, true);
            error_message = validatePrecision(table_reader3);
            if (!error_message.equals("")) {
                res.getWriter().println("<font color='red'>Please check your CSV file: There is an error in the precision column, as specified below: </font>");
                res.getWriter().println("<br/>" + error_message);
                Debug.println("Please check your file: Error in precision!!! " + error_message);
                return;
            }
            if (is_overwrite_dataset) {
                deleteDataset(selected_dataset);
            }
            FileResource file_resource4 = new FileResource(_filename);
            CSVTableReader table_reader4 = new CSVTableReader(file_resource4, true);
            String output_filename = _filename + ".fs.csv";
            File output_file = new File(output_filename);
            if (!output_file.exists()) {
                output_file.createNewFile();
            }
            FileWriter filewriter = new FileWriter(output_file);
            PrintWriter printwriter = new PrintWriter(filewriter);
            CSVTableWriter writer = new CSVTableWriter(table_reader4, printwriter, 0);
            filewriter.close();
            FileResource file_resource7 = new FileResource(_filename);
            Debug.println("File " + _filename);
            CSVTableReader table_reader7 = new CSVTableReader(file_resource7, true);
            error_message = validateDimensions(table_reader7);
            if (!error_message.equals("")) {
                res.getWriter().println("<font color='red'>Please check your CSV file: There is an error in the dimensions column(s), as specified below: </font>");
                res.getWriter().println("<br/>" + error_message);
                Debug.println("Please check your file: Error in dimensions column(s)!!! " + error_message);
                return;
            }
            loadDataIntoDatasetTable(selected_dataset, output_filename, writer.getColumnNames());
            insertProjectLayer(selected_project, selected_layer);
            if (is_new_dataset) {
                insertLayerDataset(selected_layer, dataset_label);
            }
            if (!is_new_dataset) res.getWriter().println("Please launch the GIEWS Workstation site to view the updated dataset (see Table/Chart modules)."); else res.getWriter().println("The dataset has been added to the FS database.  Please launch the GIEWS Workstation site to view the updated dataset (see Table/Chart modules).");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
