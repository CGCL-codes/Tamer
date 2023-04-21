    public void extract() {
        try {
            System.out.println("importing data from directory " + params.getDir());
            loadConfig();
            if (params.getMode().equals("extract")) {
                loadFolder(params.dir);
                writeSpreadsheet();
            }
        } catch (Exception e) {
            System.err.println(e);
            error(e.toString());
            e.printStackTrace();
        }
    }
