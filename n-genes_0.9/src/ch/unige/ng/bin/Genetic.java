/*************************************************************************
*
* Copyright (C) 2005 University of Geneva, Switzerland.
*
* This file is part of n-genes
*
* n-genes is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
* 
* n-genes is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with n-genes; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301  USA
*
* http://cui.unige.ch/spc/tools/n-genes
* n-genes@cui.unige.ch
*
******************************************************************************/


package ch.unige.ng.bin;

import ch.unige.ng.gen.Context;
import ch.unige.ng.gen.Generation;
import ch.unige.ng.gen.GeneticAlgorithm;
import ch.unige.ng.halt.HaltFunction;
import ch.unige.ng.ops.Operator;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;
import ch.unige.ng.timer.EventTimer;
import ch.unige.ng.timer.GeneticTimer;
import ch.unige.ng.tools.Forced;

import java.util.LinkedList;
import java.util.List;

/**
 * This class defines a genetic algorithm. This should be seen as the main class, this class contains differeent
 * classes that are called, depending to the algorithm.
 *
 * @author Jacques Fontignie
 * @version 6 janv. 2005
 */
     public class Genetic<T extends GlobalIndividual> {


    /**
     * Boolean informing we are in mode sfashutting, if this is the case, the algorithm will wait for
     * the current generation to close, it is used to avoid to lose all the data.
     */
    private boolean safeShutting;

    /**
     * The shutting function used to catch terminations
     */
    private ShuttingFunction shuttingFunction;

    /**
     * The context in which is held the problem and the random generator
     */
    private Context context;

    /**
     * The list of operators that are done on the population
     */
    private List<Operator<T>> operatorList;

    /**
     * The timers that are fired at certain times.
     */
    private List<GeneticTimer<T>> timerList;

    /**
     * The timer that are fired to generate events
     */
    private List<EventTimer<T>> eventList;

    /**
     * The function that are called when the algorithm is ternminated
     */
    private List<HaltFunction> haltFunctions;

    /**
     * The size of the population
     */
    private int popSize;

    /**
     * The selector that is used in the algorithm
     */
    private Selector<T> selector;

    /**
     * If the time ha to be written at the end.
     */
    private boolean time;

    /**
     * A prototype of the individual
     */
    private T individual;

    /**
     * The population used.
     */
    private Population<T> population;

    /**
     * This constructor creates a new genetic program
     *
     * @param population   The population used for the algorithm
     * @param individual   The prototype individual
     * @param time         do we have to display the time at the end of the algorithm
     * @param context      The context used
     * @param popSize      The size of the population
     * @param selector     The selector used for the algorithm
     * @param safeShutting Do the algorithm terminate regularly if there is a SiGTERM
     * @param operators    the genetic operators used
     */
    public Genetic(Population<T> population, T individual, boolean time,
                   Context context, int popSize, Selector<T> selector, boolean safeShutting,
                   Operator<T>... operators) {
        this();
        setPopulation(population);
        setIndividual(individual);
        setTime(time);
        setContext(context);
        setPopSize(popSize);
        setSelection(selector);
        setSafeShutting(safeShutting);
        for (Operator<T> operator : operators)
            addOperator(operator);
    }

    /**
     * The constrcutor. It is compatible with the java beans, because this class is called by the <code>XMLParser</code>
     *
     * @see ch.unige.ng.tools.parser.XMLParser
     */
    public Genetic() {
        operatorList = new LinkedList<Operator<T>>();
        timerList = new LinkedList<GeneticTimer<T>>();
        haltFunctions = new LinkedList<HaltFunction>();
        eventList = new LinkedList<EventTimer<T>>();
    }

    /**
     * Determines if you want to shut the program safely, that is that the current generation terminates and
     * after that, the end logs and timer are fired. This can be useful if the algorithm run for a long time and you want
     * to call some functions at the end.
     * <p/>
     * Use this function unless you are sure that the program will not loop indefinitly, because, there is no other
     * way to kill the program that kill -9.
     *
     * @param safeShutting
     */
    @Forced public void setSafeShutting(boolean safeShutting) {
        this.safeShutting = safeShutting;
    }

    /**
     * Sets the population used
     *
     * @param population
     */
    @Forced public void setPopulation(Population<T> population) {
        this.population = population;
    }

    /**
     * This method sets the prototype of the individual.
     * <p/>
     * This method is forced-annotated becaus we want it to be called by the parser, if this is not done,
     * an exception will be fired.
     *
     * @param individual the prototype.
     */
    @Forced public void setIndividual(T individual) {
        this.individual = individual;
    }


    /**
     * This method defines if we want the execution time to be written at the end of the algorithm
     *
     * @param time
     */
    @Forced public void setTime(boolean time) {
        this.time = time;
    }

    /**
     * This method sets the context to the algorithm
     *
     * @param context
     */
    @Forced public void setContext(Context context) {
        this.context = context;
    }


    /**
     * This method adds a new operator to the algorithm. The operators will be selected with the same probability.
     * <p/>
     * If you do not want it, you should use the class <code> Breeder </code>
     *
     * @param op
     * @see ch.unige.ng.ops.utils.Breeder
     */
    @Forced public void addOperator(Operator<T> op) {
        operatorList.add(op);
    }

    /**
     * Adds a function that is checked at the end of each generation to know if the algorithm is terminated or not.
     * If there is now such function, the algorithm will work until Windows crashes.
     *
     * @param function
     */
    public void addHaltFunction(HaltFunction function) {
        haltFunctions.add(function);
    }

    /**
     * Sets the number of individuals that are in the population.
     *
     * @param popSize
     */
    @Forced public void setPopSize(int popSize) {
        this.popSize = popSize;
    }

    /**
     * Sets the selection algorithm
     *
     * @param selector
     */
    @Forced public void setSelection(Selector<T> selector) {
        this.selector = selector;
    }

    /**
     * Adds a new timer. A timer is fired at certain time and can do some actions.
     *
     * @param timer
     */
    public void addTimer(GeneticTimer<T> timer) {
        timerList.add(timer);
    }

    /**
     * Adds a new Event timer. A timer is fired at certain time and can do some actions.
     *
     * @param timer
     */
    public void addEventTimer(EventTimer<T> timer) {
        eventList.add(timer);
    }


    public Context getContext() {
        return context;
    }

    public List<Operator<T>> getOperatorList() {
        return operatorList;
    }

    public List<GeneticTimer<T>> getTimerList() {
        return timerList;
    }

    public List<HaltFunction> getHaltFunctions() {
        return haltFunctions;
    }

    public int getPopSize() {
        return popSize;
    }

    public Selector getSelector() {
        return selector;
    }

    public boolean isTime() {
        return time;
    }

    public GlobalIndividual getIndividual() {
        return individual;
    }

    /**
     * This is the main method of the algorithm.
     */
    public void run() throws Throwable {
        //Create the safe shutting mode
        if (safeShutting) {
            shuttingFunction = new ShuttingFunction();
            haltFunctions.add(shuttingFunction);

        }
        try {
            //Run the genetic algorithm
            runGenetic();
            //Catch the exceptions
        } catch (Throwable t) {
            //If we are in safe shutting mode, we have to terminate the release the shutdownhook
            if (safeShutting) {
                shuttingFunction.terminate();
            }
            //Send the exception
            throw t;
        }
    }

    /**
     * Run the genetic algorithm
     */
    private void runGenetic() {        
        System.out.println("Run Genetic algorithm");
        System.out.println(".............................");
        long begin = System.currentTimeMillis();
        //we run for numMeasure time the algorithm

        Generation<T> basicGeneration = createGeneration();

        //Running the genetic algorithm
        final GeneticAlgorithm<T> ga = new GeneticAlgorithm<T>(population, context, haltFunctions);


        //Initializing the population
        ga.initializePopulation(popSize, individual);
        ga.iterate(basicGeneration);

        //If we are in safe shutting mode, we just have to inform that the hook has to be released
        if (safeShutting) {
            shuttingFunction.terminate();
        }

        //The context is no more evolving.
        context.setEvolving(false);
        for (EventTimer<T> timer : eventList) {
            timer.step(ga.getPopulation(), selector, context);
        }
        for (GeneticTimer<T> timer : timerList)
            timer.step(ga.getPopulation(), selector, context);

        if (time)
            System.out.println("elapsed time:\t" + ((System.currentTimeMillis() - begin) / (1000.)));

        //Release the semaphore to let the program end, else it will never stop.
        if (safeShutting) {
            shuttingFunction.informTerminated();
        }
    }

    /**
     * @return the generation
     */
    private Generation<T> createGeneration() {
        //What is going in a generation?
        return new Generation<T>() {
            double probs [] = new double[operatorList.size()];

            public void calculate(Population<T> pop, Context context) {
                for (EventTimer<T> timer : eventList)
                    timer.step(pop, selector, context);

                //By default each operator has the same probability. To change it, you have to use
                //the breeder class

                //If there is only one operator, we will operate without calculation
                if (operatorList.size() == 1) {
                    operatorList.get(0).operate(pop, selector, context, pop.size());
                    return;
                }

                //First, we have to estimate the probability of each operator
                double sum = 0;
                for (int i = 0; i < operatorList.size(); i++) {
                    double value = context.getRng().nextDouble();
                    probs[i] = value;
                    sum += value;
                }

                int i = 0;
                int numTreated = 0;
                int popSize = population.size();
                for (Operator<T> operator : operatorList) {
                    if (i < operatorList.size() - 1) {
                        int treated = (int) (probs[i] / sum * popSize);
                        if (treated > 0)
                            operator.operate(pop, selector, context, treated);
                        numTreated += treated;
                    } else if (popSize - numTreated > 0)
                        operator.operate(pop, selector, context, popSize - numTreated);
                    i++;
                }
            }

            public void refresh(Population<T> pop, Context context) {
                //Sends data to the geneticTimer
                for (GeneticTimer<T> timer : timerList)
                    timer.step(pop, selector, context);
            }
        };
    }


}
