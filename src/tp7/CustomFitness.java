package tp7;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.tools.Forced;

import java.util.ArrayList;


public class CustomFitness implements Fitness<Boolean> {

    private String configFile;

    public CustomFitness() {
    }

    public CustomFitness( String file) {
        setFile(file);
    }

    @Forced
    public void setFile(String file) {
        this.configFile = file;
    }

    @Override
    public double compute(Evaluable<Boolean> i){

        //foreach line
        // compute m bools => result
        // result = m+1

        return 0;
    }
}
