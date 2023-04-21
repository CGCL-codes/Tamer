    public SolutionSet execute() throws JMException {
        initParam();
        try {
            fos_ = new FileOutputStream("metrics" + ".AbYSS");
            osw_ = new OutputStreamWriter(fos_);
            bw_ = new BufferedWriter(osw_);
            Solution solution;
            for (int i = 0; i < solutionSetSize_; i++) {
                solution = diversificationGeneration();
                problem_.evaluateConstraints(solution);
                problem_.evaluate(solution);
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
                solution = (Solution) improvementOperator_.execute(solution);
                evaluations_ += improvementOperator_.getEvaluations();
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
                solutionSet_.add(solution);
            }
            int newSolutions = 0;
            while (evaluations_ < maxEvaluations) {
                referenceSetUpdate(true);
                newSolutions = subSetGeneration();
                while (newSolutions > 0) {
                    referenceSetUpdate(false);
                    if (evaluations_ >= maxEvaluations) {
                        bw_.close();
                        return archive_;
                    }
                    newSolutions = subSetGeneration();
                }
                if (evaluations_ < maxEvaluations) {
                    solutionSet_.clear();
                    for (int i = 0; i < refSet1_.size(); i++) {
                        solution = refSet1_.get(i);
                        solution.unMarked();
                        solution = (Solution) improvementOperator_.execute(solution);
                        evaluations_ += improvementOperator_.getEvaluations();
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
                        solutionSet_.add(solution);
                    }
                    refSet1_.clear();
                    refSet2_.clear();
                    distance_.crowdingDistanceAssignment(archive_, problem_.getNumberOfObjectives());
                    archive_.sort(crowdingDistance_);
                    int insert = solutionSetSize_ / 2;
                    if (insert > archive_.size()) insert = archive_.size();
                    if (insert > (solutionSetSize_ - solutionSet_.size())) insert = solutionSetSize_ - solutionSet_.size();
                    for (int i = 0; i < insert; i++) {
                        solution = new Solution(archive_.get(i));
                        solution.unMarked();
                        solutionSet_.add(solution);
                    }
                    while (solutionSet_.size() < solutionSetSize_) {
                        solution = diversificationGeneration();
                        problem_.evaluateConstraints(solution);
                        problem_.evaluate(solution);
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
                        solution = (Solution) improvementOperator_.execute(solution);
                        evaluations_ += improvementOperator_.getEvaluations();
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
                        solution.unMarked();
                        solutionSet_.add(solution);
                    }
                }
            }
            bw_.close();
        } catch (IOException e) {
            Configuration.logger_.severe("Error acceding to the file");
            e.printStackTrace();
        }
        return archive_;
    }
