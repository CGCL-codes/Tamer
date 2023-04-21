    public static void main(String[] args) {
        final String plansFile = "../mmoyo/output/taste/inputPlans/500.plans_fromCalibM44.xml.gz";
        final String netFile = "../../berlin-bvg09/pt/nullfall_berlin_brandenburg/input/network_multimodal.xml.gz";
        final String TAB = "\t";
        DataLoader dLoader = new DataLoader();
        Population pop = dLoader.readPopulation(plansFile);
        DistribSelector randomTest = new DistribSelector();
        for (Person person : pop.getPersons().values()) {
            Plan selectedPlan = randomTest.selectPlan(person);
            int i = person.getPlans().indexOf(selectedPlan);
            System.out.print(i + TAB);
            ((PersonImpl) person).setSelectedPlan(selectedPlan);
        }
        System.out.println("writing output plan file...");
        PopulationWriter popwriter = new PopulationWriter(pop, dLoader.readNetwork(netFile));
        File file = new File(plansFile);
        String changedPopFile = file.getParent() + File.separatorChar + file.getName() + "probDistribSelectPlan6.xml.gz";
        popwriter.write(changedPopFile);
        System.out.println("done");
    }
