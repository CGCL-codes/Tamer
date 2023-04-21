    public void onSimulationStep(SimStepPerception ssp) {
        System.out.println(this.getName() + " simulation step!");
        Graph p = ssp.getGraph(this.graph.nVertices, this.graph.nEdges);
        graph.update(p, true);
        graph.toProgram().saveTo(this.getLogPath() + this.getName() + "_graph.lp.txt");
        Program pers = ssp.toProgram();
        pers.add(assembleProgram(this.role));
        pers.add(this.beliefs.toProgram());
        pers.add(this.graph.toProgram());
        pers.saveTo(logPath + "step_rc" + cycleCounter + ".lp.txt");
        System.out.println("computing next action");
        List<AnswerSet> asl = dlv.computeModels(pers, 1);
        System.out.println("done, answer sets: " + asl.size());
        int i = 1;
        Collection<Literal> actions = null;
        AnswerSet goodAS = null;
        SolveTime st = this.getDLV().getTimings();
        if (st != null) {
            System.out.println("time (w|c|r): " + st.write / 1000000 + " | " + st.calculate / 1000000 + " | " + st.read / 1000000);
        }
        for (AnswerSet a : asl) {
            if (i == 1) goodAS = a;
            System.out.println("solution " + i + ": " + a.size() + " facts");
            a.toProgram().saveTo(logPath + "step_bels_rc" + cycleCounter + "_as" + i + ".lp.txt");
            ++i;
        }
        if (goodAS != null) {
            System.out.print("actions: ");
            for (String pred : goodAS.literals.keySet()) {
                if (pred.startsWith("do_")) {
                    System.out.print(goodAS.literals.get(pred) + ", ");
                    actions = goodAS.literals.get(pred);
                }
            }
            System.out.println("");
        }
        if (actions != null) {
            Literal action = actions.iterator().next();
            Action l = Literal2Action(action);
            this.act(l);
        } else {
            System.err.println(this.getName() + " warning: i have no action to commit!");
        }
        if (goodAS != null) {
            this.updateBeliefs(goodAS);
        }
    }
