    public Set<Map<String, TermModel>> query(String query, String program) {
        List<String> vars = getVars(query);
        engine = getEngine();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(TMPFILE));
            out.write(program + "\n" + "nonDeterministicGoal(InterestingVarsTerm,G,ListTM) :-" + "findall(InterestingVarsTerm,G,L), buildTermModel(L,ListTM).");
            out.close();
            System.out.println("LOADING PROGRAM:\n" + program);
            engine.consultAbsolute(new File(TMPFILE));
            StringBuffer goal = new StringBuffer();
            if (vars.size() > 0) {
                goal.append("nonDeterministicGoal(");
                for (String var : vars) {
                    goal.append(var + "+");
                }
                goal.deleteCharAt(goal.length() - 1);
                goal.append("," + query + ",ListModel)");
            } else {
                System.out.println("Executing ground Goal: " + query);
                result = new HashSet<Map<String, TermModel>>();
                if (engine.deterministicGoal(query)) {
                    result.add(new HashMap<String, TermModel>());
                    return result;
                } else {
                    return result;
                }
            }
            System.out.println("Executing Goal: " + goal);
            TermModel solutionVars = (TermModel) (engine.deterministicGoal(goal.toString(), "[ListModel]")[0]);
            System.out.println("Solution bindings list:" + solutionVars);
            result = new HashSet<Map<String, TermModel>>();
            TermModel tm = solutionVars;
            while (tm.children != null) {
                tmpVars = new ArrayList<String>(vars);
                tmpMap = new HashMap<String, TermModel>();
                addToResult(tm.children[0]);
                result.add(tmpMap);
                tm = tm.children[1];
            }
        } catch (IOException e) {
            throw new RuntimeException("could not write/read to tmpfile", e);
        } finally {
            engine.shutdown();
        }
        return null;
    }
