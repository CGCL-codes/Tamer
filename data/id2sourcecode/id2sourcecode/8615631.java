    public int subSetGeneration() throws JMException {
        Solution[] parents = new Solution[2];
        Solution[] offSpring;
        subSet_.clear();
        for (int i = 0; i < refSet1_.size(); i++) {
            parents[0] = refSet1_.get(i);
            for (int j = i + 1; j < refSet1_.size(); j++) {
                parents[1] = refSet1_.get(j);
                if (!parents[0].isMarked() || !parents[1].isMarked()) {
                    Solution[] DEparent1;
                    Solution[] DEparent2;
                    if (refSet1_.size() > 4) {
                        DEparent1 = (Solution[]) selectionOperator_.execute(new Object[] { refSet1_, i });
                        DEparent2 = (Solution[]) selectionOperator_.execute(new Object[] { refSet1_, j });
                    } else if (archive_.size() > 4) {
                        DEparent1 = (Solution[]) selectionOperator_.execute(new Object[] { archive_, i });
                        DEparent2 = (Solution[]) selectionOperator_.execute(new Object[] { archive_, j });
                    } else {
                        DEparent1 = new Solution[4];
                        DEparent1[0] = refSet1_.get(i);
                        DEparent1[1] = new Solution(problem_);
                        DEparent1[2] = new Solution(problem_);
                        DEparent1[3] = new Solution(problem_);
                        DEparent2 = new Solution[4];
                        DEparent2[0] = refSet1_.get(j);
                        DEparent2[1] = new Solution(problem_);
                        DEparent2[2] = new Solution(problem_);
                        DEparent2[3] = new Solution(problem_);
                    }
                    offSpring = new Solution[2];
                    offSpring[0] = (Solution) crossoverOperator_.execute(new Object[] { refSet1_.get(i), DEparent1 });
                    offSpring[1] = (Solution) crossoverOperator_.execute(new Object[] { refSet1_.get(i), DEparent2 });
                    problem_.evaluate(offSpring[0]);
                    problem_.evaluate(offSpring[1]);
                    problem_.evaluateConstraints(offSpring[0]);
                    problem_.evaluateConstraints(offSpring[1]);
                    evaluations_++;
                    if ((evaluations_ % 100) == 0) {
                        if ((indicators != null) && (requiredEvaluations == 0)) {
                            SolutionSet nonDominatedTmp = (new Ranking(archive_)).getSubfront(0);
                            try {
                                bw_.write(evaluations_ + "");
                                bw_.write(" ");
                                bw_.write(indicators.getParetoOptimalSolutions(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getIGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getEpsilon(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getSpread(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getHypervolume(nonDominatedTmp) + "");
                                bw_.newLine();
                            } catch (Exception e) {
                                Configuration.logger_.severe("Error acceding to the file");
                                e.printStackTrace();
                            }
                        }
                    }
                    evaluations_++;
                    if ((evaluations_ % 100) == 0) {
                        if ((indicators != null) && (requiredEvaluations == 0)) {
                            SolutionSet nonDominatedTmp = (new Ranking(archive_)).getSubfront(0);
                            try {
                                bw_.write(evaluations_ + "");
                                bw_.write(" ");
                                bw_.write(indicators.getParetoOptimalSolutions(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getIGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getEpsilon(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getSpread(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getHypervolume(nonDominatedTmp) + "");
                                bw_.newLine();
                            } catch (Exception e) {
                                Configuration.logger_.severe("Error acceding to the file");
                                e.printStackTrace();
                            }
                        }
                    }
                    if (evaluations_ < maxEvaluations) {
                        subSet_.add(offSpring[0]);
                        subSet_.add(offSpring[1]);
                    }
                    parents[0].marked();
                    parents[1].marked();
                }
            }
        }
        for (int i = 0; i < refSet2_.size(); i++) {
            parents[0] = refSet2_.get(i);
            for (int j = i + 1; j < refSet2_.size(); j++) {
                parents[1] = refSet2_.get(j);
                if (!parents[0].isMarked() || !parents[1].isMarked()) {
                    Solution[] DEparent1;
                    Solution[] DEparent2;
                    if (refSet2_.size() > 4) {
                        DEparent1 = (Solution[]) selectionOperator_.execute(new Object[] { refSet2_, i });
                        DEparent2 = (Solution[]) selectionOperator_.execute(new Object[] { refSet2_, j });
                    } else if (archive_.size() > 4) {
                        DEparent1 = (Solution[]) selectionOperator_.execute(new Object[] { archive_, i });
                        DEparent2 = (Solution[]) selectionOperator_.execute(new Object[] { archive_, j });
                    } else {
                        DEparent1 = new Solution[4];
                        DEparent1[0] = refSet2_.get(i);
                        DEparent1[1] = new Solution(problem_);
                        DEparent1[2] = new Solution(problem_);
                        DEparent1[3] = new Solution(problem_);
                        DEparent2 = new Solution[4];
                        DEparent2[0] = refSet2_.get(j);
                        DEparent2[1] = new Solution(problem_);
                        DEparent2[2] = new Solution(problem_);
                        DEparent2[3] = new Solution(problem_);
                    }
                    offSpring = new Solution[2];
                    offSpring[0] = (Solution) crossoverOperator_.execute(new Object[] { refSet2_.get(i), DEparent1 });
                    offSpring[1] = (Solution) crossoverOperator_.execute(new Object[] { refSet2_.get(i), DEparent2 });
                    problem_.evaluateConstraints(offSpring[0]);
                    problem_.evaluateConstraints(offSpring[1]);
                    problem_.evaluate(offSpring[0]);
                    problem_.evaluate(offSpring[1]);
                    evaluations_++;
                    if ((evaluations_ % 100) == 0) {
                        if ((indicators != null) && (requiredEvaluations == 0)) {
                            SolutionSet nonDominatedTmp = (new Ranking(archive_)).getSubfront(0);
                            try {
                                bw_.write(evaluations_ + "");
                                bw_.write(" ");
                                bw_.write(indicators.getParetoOptimalSolutions(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getIGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getEpsilon(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getSpread(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getHypervolume(nonDominatedTmp) + "");
                                bw_.newLine();
                            } catch (Exception e) {
                                Configuration.logger_.severe("Error acceding to the file");
                                e.printStackTrace();
                            }
                        }
                    }
                    evaluations_++;
                    if ((evaluations_ % 100) == 0) {
                        if ((indicators != null) && (requiredEvaluations == 0)) {
                            SolutionSet nonDominatedTmp = (new Ranking(archive_)).getSubfront(0);
                            try {
                                bw_.write(evaluations_ + "");
                                bw_.write(" ");
                                bw_.write(indicators.getParetoOptimalSolutions(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getIGD(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getEpsilon(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getSpread(nonDominatedTmp) + "");
                                bw_.write(" ");
                                bw_.write(indicators.getHypervolume(nonDominatedTmp) + "");
                                bw_.newLine();
                            } catch (Exception e) {
                                Configuration.logger_.severe("Error acceding to the file");
                                e.printStackTrace();
                            }
                        }
                    }
                    if (evaluations_ < maxEvaluations) {
                        subSet_.add(offSpring[0]);
                        subSet_.add(offSpring[1]);
                    }
                    parents[0].marked();
                    parents[1].marked();
                }
            }
        }
        return subSet_.size();
    }
