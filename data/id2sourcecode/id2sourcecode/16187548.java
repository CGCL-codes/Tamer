    public static void loadTransformation(String filename, final double[][] cx, final double[][] cy) {
        try {
            final FileReader fr = new FileReader(filename);
            final BufferedReader br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            int lineN = 1;
            StringTokenizer st = new StringTokenizer(line, "=");
            if (st.countTokens() != 2) {
                fr.close();
                IJ.write("Line " + lineN + "+: Cannot read number of intervals");
                return;
            }
            st.nextToken();
            int intervals = Integer.valueOf(st.nextToken()).intValue();
            line = br.readLine();
            line = br.readLine();
            lineN += 2;
            for (int i = 0; i < intervals + 3; i++) {
                line = br.readLine();
                lineN++;
                st = new StringTokenizer(line);
                if (st.countTokens() != intervals + 3) {
                    fr.close();
                    IJ.write("Line " + lineN + ": Cannot read enough coefficients");
                    return;
                }
                for (int j = 0; j < intervals + 3; j++) cx[i][j] = Double.valueOf(st.nextToken()).doubleValue();
            }
            line = br.readLine();
            line = br.readLine();
            lineN += 2;
            for (int i = 0; i < intervals + 3; i++) {
                line = br.readLine();
                lineN++;
                st = new StringTokenizer(line);
                if (st.countTokens() != intervals + 3) {
                    fr.close();
                    IJ.write("Line " + lineN + ": Cannot read enough coefficients");
                    return;
                }
                for (int j = 0; j < intervals + 3; j++) cy[i][j] = Double.valueOf(st.nextToken()).doubleValue();
            }
            fr.close();
        } catch (FileNotFoundException e) {
            IJ.error("File not found exception" + e);
            return;
        } catch (IOException e) {
            IJ.error("IOException exception" + e);
            return;
        } catch (NumberFormatException e) {
            IJ.error("Number format exception" + e);
            return;
        }
    }
