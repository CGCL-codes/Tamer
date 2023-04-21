    private void checkAndCreateDTD() {
        File dtdFile = new File(FULL_SETUP_DTD_NAME);
        if (!dtdFile.exists()) {
            try {
                dtdFile.createNewFile();
                FileWriter fileWriter = new FileWriter(dtdFile);
                fileWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + CR);
                fileWriter.write("  <!--Setup root element-->" + CR);
                fileWriter.write("  <!ELEMENT " + ROOT_ELEMENT + " (" + VERSION_ELEMENT + ", " + OPEN_FILES_ELEMENT + ", " + ICON_FILES_ELEMENT + ", " + DETECTOR_DATA_ELEMENT + "," + PASSWORD_ELEMENT + ")>" + CR + CR);
                fileWriter.write("  <!-- defines the version of the setup file -->" + CR);
                fileWriter.write("  <!ELEMENT " + VERSION_ELEMENT + " EMPTY>" + CR);
                fileWriter.write("  <!-- the only attribute is the main version number -->" + CR);
                fileWriter.write("  <!ATTLIST " + VERSION_ELEMENT + " " + MAIN_VERSION_ATTRIBUTE + " CDATA #REQUIRED>" + CR);
                fileWriter.write("  <!-- defines a list of already opened files -->" + CR);
                fileWriter.write("  <!ELEMENT " + OPEN_FILES_ELEMENT + " (" + OPEN_FILE_ELEMENT + ")*>" + CR);
                fileWriter.write("  <!-- defines a file -->" + CR);
                fileWriter.write("  <!-- the data is a name given to the file -->" + CR);
                fileWriter.write("  <!ELEMENT " + OPEN_FILE_ELEMENT + " EMPTY>" + CR);
                fileWriter.write("  <!-- the first attribute is the real place where to find the file -->" + CR);
                fileWriter.write("  <!-- the second attribute is the date of the last access to this file -->" + CR);
                fileWriter.write("  <!ATTLIST " + OPEN_FILE_ELEMENT + " " + REFERENCE_ATTRIBUTE + " CDATA #REQUIRED " + LAST_OPEN_ATTRIBUTE + " CDATA #REQUIRED>" + CR + CR);
                fileWriter.write("  <!-- defines a list of icon files -->" + CR);
                fileWriter.write("  <!ELEMENT " + ICON_FILES_ELEMENT + " (" + ICON_FILE_ELEMENT + "*)>" + CR);
                fileWriter.write("  <!-- defines an icon file and the related element -->" + CR);
                fileWriter.write("  <!-- the data is the name of the element associated to the file -->" + CR);
                fileWriter.write("  <!ELEMENT " + ICON_FILE_ELEMENT + " EMPTY>" + CR);
                fileWriter.write("  <!-- the only attribute is the real place where to find the icon file -->" + CR);
                fileWriter.write("  <!ATTLIST " + ICON_FILE_ELEMENT + " " + ELEMENT_ATTRIBUTE + " CDATA #REQUIRED " + FILE_ATTRIBUTE + " CDATA #REQUIRED>" + CR + CR);
                fileWriter.write("  <!-- knowledge concerning the data used with this editor -->" + CR);
                fileWriter.write("  <!ELEMENT " + DETECTOR_DATA_ELEMENT + " (" + DATA_LOCATION_ELEMENT + ", " + DATA_TYPE_ELEMENT + "*)>" + CR);
                fileWriter.write("  <!-- the path to the root of the data -->" + CR);
                fileWriter.write("  <!ELEMENT " + DATA_LOCATION_ELEMENT + " EMPTY>" + CR);
                fileWriter.write("  <!-- the only attribute is the place where to find the data -->" + CR);
                fileWriter.write("  <!ATTLIST " + DATA_LOCATION_ELEMENT + " " + PATH_ATTRIBUTE + " CDATA #REQUIRED>" + CR);
                fileWriter.write("  <!-- a given type of data -->" + CR);
                fileWriter.write("  <!ELEMENT " + DATA_TYPE_ELEMENT + " EMPTY>" + CR);
                fileWriter.write("  <!-- the first attribute is the dtd file for this type, relative to the DATA_LOCATION -->" + CR);
                fileWriter.write("  <!-- the second attribute is the root element to use for this data type -->" + CR);
                fileWriter.write("  <!ATTLIST " + DATA_TYPE_ELEMENT + " " + TYPE_ATTRIBUTE + " CDATA #REQUIRED " + DTD_ATTRIBUTE + " CDATA #REQUIRED " + ROOT_NODE_ATTRIBUTE + " CDATA #REQUIRED>" + CR + CR);
                fileWriter.write("  <!ELEMENT " + PASSWORD_ELEMENT + " EMPTY>" + CR);
                fileWriter.write("  <!ATTLIST " + PASSWORD_ELEMENT + " " + HASH_VALUE_ATTRIBUTE + " CDATA #REQUIRED>" + CR + CR);
                fileWriter.flush();
                fileWriter.close();
            } catch (java.io.IOException e) {
                dtdFile.delete();
            }
        }
    }
