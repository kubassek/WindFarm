public class StartOptimiser {
	public static int nSc;

	public static void main(String argv[]) throws Exception {
		//nSc = Integer.parseInt(argv[0]);
		KusiakLayoutEvaluator eval = new KusiakLayoutEvaluator();
		WindScenario sc = new WindScenario("./Scenarios/practice_"+"0"+".xml");
		eval.initialize(sc);
		Solver algorithm = new Solver(eval);
		algorithm.run_cw();
	}
}
