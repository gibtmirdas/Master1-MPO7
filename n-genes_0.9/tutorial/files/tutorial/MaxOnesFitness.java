package tutorial;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;

public class MaxOnesFitness implements Fitness<Boolean> {

   public double compute( Evaluable<Boolean> individual ) {
      double falsePositions=0;
      for( Boolean b: individual )  {
         if ( ! b ) {
            falsePositions++;
         }
      }
      return falsePositions / individual.size();
   }
   
    public static void main( String[] args ) {
	System.out.println("Hello world !!!");
    }

}
