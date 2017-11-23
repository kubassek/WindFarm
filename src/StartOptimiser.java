import java.io.FileWriter;
import java.util.ArrayList;

public class StartOptimiser {
	public static int nSc;

	public static void main(String argv[]) throws Exception {
		//mac
		//WindScenario sc = new WindScenario("./Scenarios/practice_"+"4"+".xml");
		//pc
		ArrayList<ArrayList<String>> fileOutput = new ArrayList<ArrayList<String>>();
		for(int j = 0; j<1; j++) {
			for (int i = 0; i < 1; i++) {
				KusiakLayoutEvaluator eval = new KusiakLayoutEvaluator();
				WindScenario sc = new WindScenario("./Scenarios/practice_" + j + ".xml");
				eval.initialize(sc);
				Solver algorithm = new Solver(eval, i, j);
				algorithm.run_cw();
				fileOutput.add(algorithm.getOutput());
			}
		}

		FileWriter writer = new FileWriter("./Scenarios/output.txt");
		for(int i=0; i<fileOutput.size();i++) {
			for(String line: fileOutput.get(i)){
				writer.write(line);
			}
		}
		writer.close();

	}
}
