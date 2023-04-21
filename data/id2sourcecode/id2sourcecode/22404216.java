    private static void writeSpectra2Mgf(ArrayList<MSScan> spectra, String batchTag, ConverterXMLReader reader, MSDispatcher dispatcher, boolean append) throws IOException {
        String name;
        if (dispatcher.getCombineFilesFlag()) {
            name = reader.getOutputFilePrefix() + batchTag + ".mgf";
        } else {
            String filename = dispatcher.getProcessedFile().getName();
            filename = Pattern.compile("mzXML$", Pattern.CASE_INSENSITIVE).matcher(filename).replaceAll("mgf");
            name = reader.getOutputFilePrefix() + batchTag + filename;
        }
        FileWriter mgf;
        if (!append) {
            try {
                mgf = new FileWriter(reader.getMGFDir() + name, false);
                mgf.write("");
                mgf.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        try {
            mgf = new FileWriter(reader.getMGFDir() + name, true);
            boolean begin = true;
            for (MSScan spectrum : spectra) {
                if (!begin) mgf.write("\n\n");
                if (spectrum.getPeakList().getPrecursor() == null) continue;
                mgf.write("BEGIN IONS" + "\n");
                mgf.write("TITLE=" + spectrum.getTitle() + "\n");
                double mz = spectrum.getPeakList().getPrecursor().getMz();
                mz = CALIB_OFFSET + CALIB_SLOPE * mz;
                mgf.write("PEPMASS=" + mz + "\n");
                mgf.write("CHARGE=" + spectrum.getPeakList().getPrecursor().getCharge() + "\n");
                int n = spectrum.getPeakList().size();
                for (int q = 0; q < n; q++) {
                    mgf.write("" + String.format("%.3f", spectrum.getPeakList().getMzAt(q)) + "\t" + String.format("%.3f", spectrum.getPeakList().getIntensityAt(q)) + "\n");
                }
                mgf.write("END IONS" + "\n");
                mgf.flush();
                begin = false;
            }
            mgf.flush();
            mgf.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
