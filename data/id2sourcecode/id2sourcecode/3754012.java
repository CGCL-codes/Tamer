    public static void main(final String[] args) {
        String revision = null;
        String date = null;
        URL url = ReleaseInfo.class.getResource("/revision.txt");
        if (url != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                revision = reader.readLine();
                date = reader.readLine();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        System.out.println("MATSim");
        System.out.println("    Multi-Agent Transport Simulation Toolkit");
        if (revision == null) {
            System.out.println("    Build: unknown");
        } else {
            System.out.println("    Build: " + revision + " (" + date + ")");
        }
        System.out.println();
        System.out.println("Copyright (C) 2012 by");
        System.out.println("    Kay W. Axhausen, Michael Balmer, Christoph Dobler, Thibaut Dubernet,");
        System.out.println("    Dominik Grether, Andreas Horni, Gregor Laemmel, Nicolas Lefebvre,");
        System.out.println("    Fabrice Marchal, Konrad Meister, Kai Nagel, Andreas Neumann,");
        System.out.println("    Marcel Rieser, David Strippgen, Rashid Waraich, Michael Zilske,");
        System.out.println("    Technische Universitaet Berlin (TU-Berlin) and");
        System.out.println("    Swiss Federal Institute of Technology Zurich (ETHZ)");
        System.out.println();
        System.out.println("This program is distributed under the Gnu Public License (GPL) 2 and");
        System.out.println("comes WITHOUT ANY WARRANTY.");
        System.out.println("Please see the files WARRANTY, LICENSE and COPYING in the distribution.");
        System.out.println();
    }
