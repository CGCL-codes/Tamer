    public static int numberOfIntervalsOfTransformation(String filename) {
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
                return -1;
            }
            st.nextToken();
            int intervals = Integer.valueOf(st.nextToken()).intValue();
            fr.close();
            return intervals;
        } catch (FileNotFoundException e) {
            IJ.error("File not found exception" + e);
            return -1;
        } catch (IOException e) {
            IJ.error("IOException exception" + e);
            return -1;
        } catch (NumberFormatException e) {
            IJ.error("Number format exception" + e);
            return -1;
        }
    }
