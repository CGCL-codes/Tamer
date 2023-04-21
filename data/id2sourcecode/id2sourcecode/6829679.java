    public SolutionSet execute() throws JMException {
        int bisections, archiveSize, maxEvaluations, evaluations;
        CrowdingArchive archive;
        Operator mutationOperator;
        Comparator dominance;
        QualityIndicator indicators;
        bisections = ((Integer) this.getInputParameter("bisections")).intValue();
        archiveSize = ((Integer) this.getInputParameter("archiveSize")).intValue();
        maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();
        indicators = (QualityIndicator) getInputParameter("indicators");
        mutationOperator = this.operators_.get("mutation");
        evaluations = 0;
        archive = new CrowdingArchive(archiveSize, problem_.getNumberOfObjectives());
        dominance = new DominanceComparator();
        Solution solution = new Solution(problem_);
        problem_.evaluate(solution);
        problem_.evaluateConstraints(solution);
        evaluations++;
        if (!archive.add(new Solution(solution))) {
            ;
        }
        try {
            FileOutputStream fos = new FileOutputStream("metrics" + ".paesCrowding");
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            do {
                Solution mutateIndividual = new Solution(solution);
                mutationOperator.execute(mutateIndividual);
                problem_.evaluate(mutateIndividual);
                problem_.evaluateConstraints(mutateIndividual);
                evaluations++;
                int flag = dominance.compare(solution, mutateIndividual);
                if (flag == 1) {
                    solution = new Solution(mutateIndividual);
                    if (!archive.add(mutateIndividual)) ;
                } else if (flag == 0) {
                    if (archive.add(mutateIndividual)) {
                        solution = test(solution, mutateIndividual);
                    } else {
                        ;
                    }
                } else ;
                if ((indicators != null) && (evaluations % 100 == 0)) {
                    SolutionSet nonDominatedTmp = archive;
                    bw.write(evaluations + "");
                    bw.write(" ");
                    bw.write(indicators.getParetoOptimalSolutions(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getGD(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getIGD(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getEpsilon(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getSpread(nonDominatedTmp) + "");
                    bw.write(" ");
                    bw.write(indicators.getHypervolume(nonDominatedTmp) + "");
                    bw.newLine();
                }
            } while (evaluations < maxEvaluations);
            bw.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
        return archive;
    }
