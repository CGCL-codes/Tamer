package es.ulpgc.dis.heuriskein.model.solver.representation;

import java.util.ArrayList;
import java.util.HashMap;
import es.ulpgc.dis.heuriskein.model.solver.Individual;

public class CombinatoricIndividual extends Chromosome {

    private ArrayList<Integer> gen;

    public CombinatoricIndividual() {
        this.gen = new ArrayList<Integer>();
    }

    private CombinatoricIndividual(Individual original) {
        super(original);
        this.gen = (ArrayList<Integer>) ((CombinatoricIndividual) original).gen.clone();
        setFitnessValue(original.getFitnessValue());
    }

    public Object clone() {
        return new CombinatoricIndividual(this);
    }

    @Override
    public ArrayList getGen() {
        return gen;
    }

    @Override
    public int setGen(ArrayList<String> list) {
        gen.clear();
        for (String a : list) {
            gen.add(new Integer(a));
        }
        setChanged();
        return 0;
    }

    public void setGen(int[] gen) {
        this.gen.clear();
        for (int element : gen) {
            this.gen.add(new Integer(element));
        }
        setChanged();
    }

    public int[] getGenArray() {
        int copy[] = new int[gen.size()];
        int i = 0;
        for (Integer value : gen) {
            copy[i++] = value.intValue();
        }
        return copy;
    }

    public String toString() {
        String str = gen.toString();
        return str;
    }

    public boolean equals(Object o) {
        CombinatoricIndividual ind;
        try {
            ind = (CombinatoricIndividual) o;
        } catch (Exception e) {
            return false;
        }
        for (int i = 0; i < gen.size(); i++) {
            if (!gen.get(i).equals(ind.gen.get(i))) {
                return false;
            }
        }
        return true;
    }

    public String getShortName() {
        return "Integer Representation";
    }

    public int getRepresentationLength() {
        return gen.size();
    }
}
