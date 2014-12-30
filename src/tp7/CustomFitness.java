package tp7;

import ch.unige.ng.fitness.GPFitness;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.evaluations.IEvaluator;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomFitness implements GPFitness<Operation<Boolean>> {

    private String configFile;
	private IEvaluator evaluator;
	private ArrayList<Boolean[]> lookupTable;
	private CustomFileWriter writer;

    public CustomFitness() {
		try {
			writer = new CustomFileWriter("data.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public CustomFitness( String file, IEvaluator evaluator) {
        setFile(file);
		setEvaluator(evaluator);
	}

    @Forced
    public void setFile(String file) {
        this.configFile = file;
		// Import data from configFile
		lookupTable = ReadConfigFile.read(this.configFile);
    }

	@Forced public void setEvaluator(IEvaluator evaluator) {
		this.evaluator = evaluator;
	}

	@Override
	public int getInputSize() {
		return 10;
	}

	@Override
	public int getOutputSize() {
		return 1;
	}

	@Override
	public double compute(Evaluable<Operation<Boolean>> booleans) {
		writer.write(booleans.toString());
		int fit = 0;
		DataStack<Boolean> data;
		for(Boolean[] b: lookupTable){
			data = buildStack(Arrays.copyOf(b,10));
			evaluator.evaluate(data, booleans);
			if(data.size() != getOutputSize()) fit+= 1;
			else fit += data.pop() == b[10]? 0:1;
		}
		return fit;
	}

	public DataStack<Boolean> buildStack(Boolean[] booleans){
		DataStack<Boolean> stack = new DataStack<Boolean>();
		stack.clear();
		stack.setInputs(booleans);
		return stack;
	}
}
