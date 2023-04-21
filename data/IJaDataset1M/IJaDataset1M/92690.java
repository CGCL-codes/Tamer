package demopackage;

import cz.cvut.felk.cig.jcop.algorithm.simulatedannealing.SimulatedAnnealing;
import cz.cvut.felk.cig.jcop.problem.sat.SAT;
import cz.cvut.felk.cig.jcop.solver.SimpleSolver;
import cz.cvut.felk.cig.jcop.solver.condition.TimeoutCondition;
import java.io.File;
import java.io.IOException;

/**
 * @author Ondrej Skalicka
 */
public class DemoSimpleSolver {

    public static void main(String[] args) throws IOException {
        SimpleSolver solver = new SimpleSolver(new SimulatedAnnealing(), new SAT(new File("data/sat/valid-standard.cnf")));
        solver.addStopCondition(new TimeoutCondition(500));
        solver.run();
        solver.render();
    }
}
