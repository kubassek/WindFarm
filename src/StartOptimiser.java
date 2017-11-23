import java.io.FileWriter;
import java.util.ArrayList;

public class StartOptimiser {
	public static int nSc;

	public static void main(String argv[]) throws Exception {

		FileWriter writer = new FileWriter("./Scenarios/output.txt");
			for (int i = 0; i < 10; i++) {
				KusiakLayoutEvaluator eval = new KusiakLayoutEvaluator();
				WindScenario sc = new WindScenario("./Scenarios/practice_" + 0 + ".xml");
				eval.initialize(sc);
				Solver algorithm = new Solver(eval, i, 0, writer);
				algorithm.run_cw();
			}

		writer.close();

	}
}
