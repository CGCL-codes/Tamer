    public boolean saveProject(File file) {
        boolean saved = false;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            exportProject();
            writer.close();
            saved = true;
            exportedProjectName = file;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Trying to write to the following file failed.\n" + file + "\n" + "The file might be read-only or it might be open in another program.\n" + "The project was not saved.\n" + "You should save the project to a different file (select \"Save as...\" in the \"Project\" menu.", "Couldn't write to file!", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            System.err.println("IO " + e);
            e.printStackTrace();
        }
        return (saved);
    }
