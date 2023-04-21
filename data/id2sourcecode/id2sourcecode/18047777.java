    private void writeConfigurationFile() {
        String nextLine = "ERROR: FILE INPUT";
        File firstElement = fileArray[0][0];
        String filename = firstElement.getParentFile().getName() + firstElement.getName().substring(0, firstElement.getName().lastIndexOf('.'));
        configFile = configFileFolder + File.separator + filename + ".txt";
        FileReader fileReader = null;
        PrintWriter configWriter = null;
        try {
            fileReader = new FileReader(masterConfigFile);
            configWriter = new PrintWriter(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufReader = new BufferedReader(fileReader);
        while (true) {
            try {
                nextLine = bufReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nextLine == null) {
                break;
            }
            if (nextLine.contains("sourceDirectory ./")) {
                nextLine = "sourceDirectory " + sourceDirectory.toString().replace('\\', '/') + "/";
            } else if (nextLine.contains("destinationDirectory ./")) {
                nextLine = "destinationDirectory " + destinationDirectory.toString().replace('\\', '/') + "/";
            } else if (nextLine.contains("TileArrays")) {
                nextLine = "TileArrays " + rows * cols;
            } else if (nextLine.contains("TileXDim")) {
                nextLine = "TileXDim " + cols;
            } else if (nextLine.contains("TileYDim")) {
                nextLine = "TileYDim " + rows;
            }
            configWriter.println(nextLine);
        }
        try {
            bufReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < rows; ++i) {
            configWriter.print("TileList ");
            for (int j = 0; j < cols; ++j) {
                configWriter.print(fileArray[i][j].getParentFile().getName() + File.separator + fileArray[i][j].getName() + " ");
            }
            configWriter.println();
        }
        configWriter.close();
    }
