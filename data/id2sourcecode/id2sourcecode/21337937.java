    static void doPredict(BufferedReader reader, Writer writer, Model model) throws IOException {
        int correct = 0;
        int total = 0;
        int nr_class = model.getNrClass();
        double[] prob_estimates = null;
        int n;
        int nr_feature = model.getNrFeature();
        if (model.bias >= 0) n = nr_feature + 1; else n = nr_feature;
        Formatter out = new Formatter(writer);
        if (flag_predict_probability) {
            if (!model.isProbabilityModel()) {
                throw new IllegalArgumentException("probability output is only supported for logistic regression");
            }
            int[] labels = model.getLabels();
            prob_estimates = new double[nr_class];
            printf(out, "labels");
            for (int j = 0; j < nr_class; j++) printf(out, " %d", labels[j]);
            printf(out, "\n");
        }
        String line = null;
        while ((line = reader.readLine()) != null) {
            List<FeatureNode> x = new ArrayList<FeatureNode>();
            StringTokenizer st = new StringTokenizer(line, " \t");
            String label = st.nextToken();
            int target_label = atoi(label);
            while (st.hasMoreTokens()) {
                String[] split = COLON.split(st.nextToken(), 2);
                if (split == null || split.length < 2) exit_input_error(total + 1);
                try {
                    int idx = atoi(split[0]);
                    double val = atof(split[1]);
                    if (idx <= nr_feature) {
                        FeatureNode node = new FeatureNode(idx, val);
                        x.add(node);
                    }
                } catch (NumberFormatException e) {
                    exit_input_error(total + 1, e);
                }
            }
            if (model.bias >= 0) {
                FeatureNode node = new FeatureNode(n, model.bias);
                x.add(node);
            }
            FeatureNode[] nodes = new FeatureNode[x.size()];
            nodes = x.toArray(nodes);
            int predict_label;
            if (flag_predict_probability) {
                predict_label = Linear.predictProbability(model, nodes, prob_estimates);
                printf(out, "%d", predict_label);
                for (int j = 0; j < model.nr_class; j++) printf(out, " %g", prob_estimates[j]);
                printf(out, "\n");
            } else {
                predict_label = Linear.predict(model, nodes);
                printf(out, "%d\n", predict_label);
            }
            if (predict_label == target_label) {
                ++correct;
            }
            ++total;
        }
        System.out.printf("Accuracy = %g%% (%d/%d)%n", (double) correct / total * 100, correct, total);
    }
