    public boolean saveProject(File file, boolean convertingData) {
        boolean saved = false;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
            CsvWriter csvw = new CsvWriter(osw, ',');
            writeToCSVFile(csvw);
            saved = true;
            exportedProjectName = file;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Trying to write to the following file failed.\n" + file + "\n" + "The file might be read-only or it might be open in another program.\n" + "The project was not saved.\n" + "You should save the project to a different file", "Couldn't write to file!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            System.err.println("IO " + e);
            e.printStackTrace();
        }
        return (saved);
    }
