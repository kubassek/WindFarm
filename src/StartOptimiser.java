public class StartOptimiser {
	public static int nSc;

	public static void main(String argv[]) throws Exception {
		KusiakLayoutEvaluator eval = new KusiakLayoutEvaluator();
		//mac
		//WindScenario sc = new WindScenario("./Scenarios/practice_"+"4"+".xml");
		//pc
		WindScenario sc = new WindScenario("./Scenarios/practice_"+"0"+".xml");
		eval.initialize(sc);
		Solver algorithm = new Solver(eval);
		algorithm.run_cw();
	}
}
