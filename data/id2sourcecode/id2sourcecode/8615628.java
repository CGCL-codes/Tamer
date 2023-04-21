    public void referenceSetUpdate(boolean build) throws JMException {
        if (build) {
            Solution individual;
            (new Spea2Fitness(solutionSet_)).fitnessAssign();
            solutionSet_.sort(fitness_);
            for (int i = 0; i < refSet1Size_; i++) {
                individual = solutionSet_.get(0);
                solutionSet_.remove(0);
                individual.unMarked();
                refSet1_.add(individual);
            }
            for (int i = 0; i < solutionSet_.size(); i++) {
                individual = solutionSet_.get(i);
                individual.setDistanceToSolutionSet(distance_.distanceToSolutionSet(individual, refSet1_));
            }
            int size = refSet2Size_;
            if (solutionSet_.size() < refSet2Size_) {
                size = solutionSet_.size();
            }
            for (int i = 0; i < size; i++) {
                double maxMinimum = 0.0;
                int index = 0;
                for (int j = 0; j < solutionSet_.size(); j++) {
                    if (solutionSet_.get(j).getDistanceToSolutionSet() > maxMinimum) {
                        maxMinimum = solutionSet_.get(j).getDistanceToSolutionSet();
                        index = j;
                    }
                }
                individual = solutionSet_.get(index);
                solutionSet_.remove(index);
                for (int j = 0; j < solutionSet_.size(); j++) {
                    double aux = distance_.distanceBetweenSolutions(solutionSet_.get(j), individual);
                    if (aux < individual.getDistanceToSolutionSet()) {
                        solutionSet_.get(j).setDistanceToSolutionSet(aux);
                    }
                }
                refSet2_.add(individual);
                for (int j = 0; j < refSet2_.size(); j++) {
                    for (int k = 0; k < refSet2_.size(); k++) {
                        if (i != j) {
                            double aux = distance_.distanceBetweenSolutions(refSet2_.get(j), refSet2_.get(k));
                            if (aux < refSet2_.get(j).getDistanceToSolutionSet()) {
                                refSet2_.get(j).setDistanceToSolutionSet(aux);
                            }
                        }
                    }
                }
            }
        } else {
            Solution individual;
            for (int i = 0; i < subSet_.size(); i++) {
                individual = (Solution) improvementOperator_.execute(subSet_.get(i));
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
                if (refSet1Test(individual)) {
                    for (int indSet2 = 0; indSet2 < refSet2_.size(); indSet2++) {
                        double aux = distance_.distanceBetweenSolutions(individual, refSet2_.get(indSet2));
                        if (aux < refSet2_.get(indSet2).getDistanceToSolutionSet()) {
                            refSet2_.get(indSet2).setDistanceToSolutionSet(aux);
                        }
                    }
                } else {
                    refSet2Test(individual);
                }
            }
            subSet_.clear();
        }
    }
