    public void testJCsvWriter() {
        File f = null;
        try {
            f = File.createTempFile("fudaaTest", ".csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(f);
        double[][] tests = new double[15][20];
        for (int i = tests.length - 1; i >= 0; i--) {
            for (int j = tests[i].length - 1; j >= 0; j--) {
                tests[i][j] = Math.random();
            }
        }
        CsvWriter writer = null;
        try {
            writer = new CsvWriter(f);
            for (int i = 0; i < tests.length; i++) {
                for (int j = 0; j < tests[i].length; j++) {
                    writer.appendDouble(tests[i][j]);
                }
                writer.newLine();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }
        try {
            CsvDoubleReader reader = new CsvDoubleReader(new FileReader(f));
            if (writer != null) {
                reader.setSep(String.valueOf(writer.getSepChar()));
            }
            TDoubleArrayList l = new TDoubleArrayList();
            int idx = 0;
            while (reader.readLine(l)) {
                assertTrue(Arrays.equals(l.toNativeArray(), tests[idx++]));
            }
            reader.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        if (f != null) f.delete();
    }
