    public static void loadAffineMatrix(String filename, double[][] affineMatrix) {
        try {
            final FileReader fr = new FileReader(filename);
            final BufferedReader br = new BufferedReader(fr);
            String line;
            line = br.readLine();
            StringTokenizer st = new StringTokenizer(line, " ");
            if (st.countTokens() != 6) {
                fr.close();
                IJ.write("Cannot read affine transformation matrix");
                return;
            }
            affineMatrix[0][0] = Double.valueOf(st.nextToken()).doubleValue();
            affineMatrix[0][1] = Double.valueOf(st.nextToken()).doubleValue();
            affineMatrix[1][0] = Double.valueOf(st.nextToken()).doubleValue();
            affineMatrix[1][1] = Double.valueOf(st.nextToken()).doubleValue();
            affineMatrix[0][2] = Double.valueOf(st.nextToken()).doubleValue();
            affineMatrix[1][2] = Double.valueOf(st.nextToken()).doubleValue();
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
