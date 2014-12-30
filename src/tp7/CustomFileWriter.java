package tp7;

import ch.unige.ng.statistics.Statistic;
import ch.unige.ng.tools.io.FilePrinter;

import java.io.IOException;

/**
 * Created by thomas on 30.12.14.
 */
public class CustomFileWriter extends FilePrinter {

	public CustomFileWriter(String s, boolean b, boolean b2) throws IOException {
		super(s, b, b2);
	}

	public CustomFileWriter(String filename) throws IOException {
		super(filename, true, false);

	}

	public void write(String data){

		try {
			super.write(data.getBytes());
			super.write("\n".getBytes());
			super.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
