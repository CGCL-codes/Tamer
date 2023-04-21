    public static void main(String[] args) {
        String results_files[] = new String[DATAFILE_PREFIX.length];
        String output_files[] = new String[DATAFILE_PREFIX.length];
        for (int i = 0; i < results_files.length; i++) {
            results_files[i] = REAL_DATAPATH + DATAFILE_PREFIX[i] + DATAFILE_NAME;
            output_files[i] = REAL_DATAPATH + DATAFILE_PREFIX[i] + OUTPUTFILE_NAME;
        }
        try {
            for (int i = 0; i < results_files.length; i++) {
                System.out.println("starting to convert " + results_files[i]);
                (new File(output_files[i])).delete();
                Scanner data = new Scanner(new BufferedReader(new FileReader(results_files[i])));
                BufferedWriter out = new BufferedWriter(new FileWriter(output_files[i]));
                out.write(data.nextLine() + "\n");
                while (data.hasNext()) {
                    Scanner line = new Scanner(data.nextLine());
                    String datatype = line.next();
                    String dataset = line.next();
                    String crossover_operator = line.next();
                    String mutation_operator = line.next();
                    String selection_method = line.next();
                    String fitness_transform = line.next();
                    String mutation_rate = line.next();
                    String child_density = line.next();
                    String pop_size = line.next();
                    String max_time = line.next();
                    String target_fitness = line.next();
                    String max_stagnancy = line.next();
                    String metric = line.next();
                    String rep = line.next();
                    String order = line.next();
                    String fitness = line.next();
                    String runtime = line.next();
                    String generations = line.next();
                    String init_order = line.next();
                    String init_fitness = line.next();
                    String init_reverse_order = line.next();
                    String init_reverse_fitness = line.next();
                    String random_search_order = line.next();
                    String random_search_fitness = line.next();
                    String random_search_runtime = line.next();
                    if (!selection_method.equals("ROU") && !fitness_transform.equals("UNT")) {
                    } else {
                        if (selection_method.equals("ROU") && fitness_transform.equals("UNT")) {
                            selection_method = "ROU";
                        } else if (selection_method.equals("ROU") && fitness_transform.equals("LIN")) {
                            selection_method = "ROUW";
                            fitness_transform = "UNT";
                        } else if (selection_method.equals("ROU") && fitness_transform.equals("EXP")) {
                            selection_method = "ROUE";
                            fitness_transform = "UNT";
                        }
                        String toOutput = datatype + "\t" + dataset + "\t" + crossover_operator + "\t" + mutation_operator + "\t" + selection_method + "\t" + fitness_transform + "\t" + mutation_rate + "\t" + child_density + "\t" + pop_size + "\t" + max_time + "\t" + target_fitness + "\t" + max_stagnancy + "\t" + metric + "\t" + rep + "\t" + order + "\t" + fitness + "\t" + runtime + "\t" + generations + "\t" + init_order + "\t" + init_fitness + "\t" + init_reverse_order + "\t" + init_reverse_fitness + "\t" + random_search_order + "\t" + random_search_fitness + "\t" + random_search_runtime + "\n";
                        out.write(toOutput);
                    }
                }
                System.out.println("finished writing " + output_files[i]);
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
