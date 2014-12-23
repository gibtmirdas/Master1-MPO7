package tutorial;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;

public class MaxOnesFitness implements Fitness<Boolean>{

	@Override
	public double compute(Evaluable<Boolean> individual) {
		return 1.0;
	}
}
