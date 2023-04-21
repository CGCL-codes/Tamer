    public Map<Id, List<Id>> run() {
        int cliqueSize = minCliqueSize + randomGen.nextInt(maxCliqueSize - minCliqueSize);
        List<Person> clique = new ArrayList<Person>(maxCliqueSize);
        int count = 0;
        this.cliques.clear();
        for (Person person : this.population.getPersons().values()) {
            if (count < cliqueSize) {
                count++;
                clique.add(person);
            } else {
                processClique(clique);
                cliqueSize = minCliqueSize + randomGen.nextInt(maxCliqueSize - minCliqueSize);
                clique.clear();
                clique.add(person);
                count = 1;
            }
        }
        return this.cliques;
    }
