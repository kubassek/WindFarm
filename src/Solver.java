import java.util.ArrayList;
import java.util.Random;

public class Solver {

    WindFarmLayoutEvaluator wfle;
    boolean[][] individuals;
    double[] fits;
    Random rand;
    int populationSize;
    
    ArrayList<double[]> grid;

    public Solver(WindFarmLayoutEvaluator evaluator) {
        wfle = evaluator;
        rand = new Random();
        grid = new ArrayList<double[]>();
        
        // set up any parameter here, e.g pop size, cross_rate etc.
        populationSize = 50;  // change this to anything you want
    }
    
    
   
    public void run_cw() {
    

    	
    /************set up grid for scenario chosen  ***************************/	
    // do not change or remove this section of code
    // centers must be > 8*R apart and avoid obstacles
    	
  	double interval = 8.001 * wfle.getTurbineRadius();

  	for (double x=0.0; x<wfle.getFarmWidth(); x+=interval) {
  	    for (double y=0.0; y<wfle.getFarmHeight(); y+=interval) {
                boolean valid = true;
                for (int o=0; o<wfle.getObstacles().length; o++) {
                    double[] obs = wfle.getObstacles()[o];
                    if (x>obs[0] && y>obs[1] && x<obs[2] && y<obs[3]) {
                        valid = false;
                    }
                }

                if (valid) {
                    double[] point = {x, y};
                    grid.add(point);
                }
            }
        }
  	
  	
  	
  	/************initialize a population:*****************************/
  	
        //  the variable grid.size() denotes the 
  		// maximum number of turbines for the given scenario
  	
        individuals = new boolean[populationSize][grid.size()];
        fits = new double[populationSize];

        for (int p=0; p<populationSize; p++) {
            for (int i=0; i<grid.size(); i++) {
                individuals[p][i] = rand.nextBoolean();
            }
        }

       /****** evaluate initial population  *************************/
     
        // this populates the fit[] array with the fitness values for each solution
        evaluate();

        /**** PUT YOUR OPTIMISER CODE HERE ***********/
        
        for (int i=0; i<(1); i++) {
        	// add some code to evolve a solution

            //selection
            boolean[][] parents = null;
            int[] parent = this.randomSelection();
            for (int j=0; j<grid.size(); j++) {
                parents[0][j] = individuals[parent[0]][j];
                parents[1][j] = individuals[parent[1]][j];
            }
            System.out.println("made it");
            for (int j=0; j<grid.size(); j++) {
                System.out.print(parents[0][j]);
            }
            //crossover
            //replacement
        }
      }
    
    private int[] randomSelection(){
        int[] parents = null;
        parents[0] = rand.nextInt(populationSize);
        parents[1] = rand.nextInt(populationSize);
        return parents;
    }

    // evaluate a single chromosome
    private double evaluate_individual(boolean[] child) {
 
       
         int nturbines=0;
         for (int i=0; i<grid.size(); i++) {
                if (child[i]) {
                    nturbines++;
                }
         }
            

          double[][] layout = new double[nturbines][2];
            int l_i = 0;
            for (int i=0; i<grid.size(); i++) {
                if (child[i]) {
                    layout[l_i][0] = grid.get(i)[0];
                    layout[l_i][1] = grid.get(i)[1];
                    l_i++;
                }
            }
	    
	    double coe;
	    if (wfle.checkConstraint(layout)) {
		wfle.evaluate(layout);
		coe = wfle.getEnergyCost();
		System.out.println("layout valid");
	    } else {
		coe = Double.MAX_VALUE;
	    }

     return coe;
	  
        
    }

    // evaluates the whole population
    private void evaluate() {
        double minfit = Double.MAX_VALUE;
        for (int p=0; p<populationSize; p++) {
            int nturbines=0;
            for (int i=0; i<grid.size(); i++) {
                if (individuals[p][i]) {
                    nturbines++;
                }
            }

            double[][] layout = new double[nturbines][2];
            int l_i = 0;
            for (int i=0; i<grid.size(); i++) {
                if (individuals[p][i]) {
                    layout[l_i][0] = grid.get(i)[0];
                    layout[l_i][1] = grid.get(i)[1];
                    l_i++;
                }
            }
	    
	    double coe;
	    if (wfle.checkConstraint(layout)) {
		wfle.evaluate(layout);
		coe = wfle.getEnergyCost();
	    } else {
		coe = Double.MAX_VALUE;
	    }

            fits[p] = coe;
            if (fits[p] < minfit) {
                minfit = fits[p];
            }
	   
        }
       // System.out.println("min " + minfit);
    }

    
    
}
