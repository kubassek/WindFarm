import java.util.ArrayList;
import java.util.Random;

public class Solver {

    WindFarmLayoutEvaluator wfle;
    boolean[][] population;
    double[] fits;
    Random rand;
    int populationSize;
    int mutationChance = 20;

    ArrayList<double[]> grid;

    public Solver(WindFarmLayoutEvaluator evaluator) {
        wfle = evaluator;
        rand = new Random();
        grid = new ArrayList<double[]>();

        // set up any parameter here, e.g pop size, cross_rate etc.
        populationSize = 200;  // change this to anything you want
    }


    public void run_cw() {


        /************set up grid for scenario chosen  ***************************/
        // do not change or remove this section of code
        // centers must be > 8*R apart and avoid obstacles

        double interval = 8.001 * wfle.getTurbineRadius();

        for (double x = 0.0; x < wfle.getFarmWidth(); x += interval) {
            for (double y = 0.0; y < wfle.getFarmHeight(); y += interval) {
                boolean valid = true;
                for (int o = 0; o < wfle.getObstacles().length; o++) {
                    double[] obs = wfle.getObstacles()[o];
                    if (x > obs[0] && y > obs[1] && x < obs[2] && y < obs[3]) {
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

        population = new boolean[populationSize][grid.size()];
        fits = new double[populationSize];

        for (int p = 0; p < populationSize; p++) {
            for (int i = 0; i < grid.size(); i++) {
                population[p][i] = rand.nextBoolean();
            }
        }

        /****** evaluate initial population  *************************/

        // this populates the fit[] array with the fitness values for each solution
        evaluate();

        /**** PUT YOUR OPTIMISER CODE HERE ***********/
        int i = 0;
        while(i<10000) {
            // add some code to evolve a solution

            //selection
            boolean[][] parents = new boolean[2][grid.size()];
            int[] parent = this.randomSelection();
            for (int j = 0; j < grid.size(); j++) {
                parents[0][j] = population[parent[0]][j];
                parents[1][j] = population[parent[1]][j];
            }

            //crossover
            boolean[][] children = new boolean[2][grid.size()];
            children = this.randomUniform(parents);

            //replacement
            boolean[] child1 = new boolean[grid.size()];
            boolean[] child2 = new boolean[grid.size()];
            for (int j = 0; j < grid.size(); j++) {
                child1[j] = children[0][j];
                child2[j] = children[1][j];
            }

            boolean[] child = new boolean[grid.size()];
            double[] childFits = new double[2];
            double childFit = 0;

            child1 = this.mutationGeneFlip(child1);
            child2 = this.mutationGeneFlip(child2);

            childFits[0] = this.evaluate_individual(child1);
            childFits[1] = this.evaluate_individual(child2);

            if (childFits[0] < childFits[1]) {
                child = child1;
                childFit = childFits[0];
            } else if (childFits[1] < childFits[0]) {
                child = child2;
                childFit = childFits[1];
            }

            if(this.falseCheck(child)) {
                /*
                if (fits[parent[0]] < fits[parent[1]]) {
                    if(fits[parent[1]]>childFit) {
                        for (int j = 0; j < grid.size(); j++) {
                            population[parent[1]][j] = child[j];
                            fits[parent[1]] = childFit;
                        }
                        System.out.println(i + " " + childFit);
                    }
                } else if (fits[parent[1]] < fits[parent[0]]) {
                    if(fits[parent[0]]>childFit) {
                        for (int j = 0; j < grid.size(); j++) {
                            population[parent[0]][j] = child[j];
                            fits[parent[0]] = childFit;
                        }
                        System.out.println(i + " " + childFit);
                    }
                }
                */

                if(this.falseCheck(child)) {

                    int worst = 0;

                    for (int j = 0; j < grid.size(); j++) {
                        if (fits[j] > fits[worst]) {
                            worst = j;
                        }
                    }

                    if (childFit < fits[worst]) {
                        for (int k = 0; k < grid.size(); k++) {
                            population[worst][k] = child[k];
                        }

                        fits[worst] = childFit;

                    }
                }

            }
            i = wfle.getNumberOfEvaluation();
        }
        evaluate();
    }

    private boolean[] mutationGeneFlip(boolean[] child){
        boolean[] mutatedChild = new boolean[grid.size()];

        int randomChance = rand.nextInt(mutationChance);
        int randomGene = rand.nextInt(grid.size());

        mutatedChild = child;

        if(randomChance == 1){
            if(child[randomGene]==true){
                mutatedChild[randomGene] = false;
            }else{
                mutatedChild[randomGene] = true;
            }
        }

        return mutatedChild;
    }

    private int[] randomSelection() {
        int[] parents = new int[2];
        parents[0] = rand.nextInt(populationSize);
        parents[1] = rand.nextInt(populationSize);
        return parents;
    }

    private boolean falseCheck(boolean[] child) {
        boolean check = false;
        for (int j = 0; j < grid.size(); j++) {
            if (child[j] == true) {
                check = true;
            }
        }
        return check;
    }

    private boolean[][] randomUniform(boolean[][] parents) {
        boolean[][] children = new boolean[2][grid.size()];
        for (int i = 0; i < grid.size(); i++) {
            if (rand.nextBoolean() == true) {
                children[0][i] = parents[0][i];
                children[1][i] = parents[1][i];
            } else {
                children[0][i] = parents[1][i];
                children[1][i] = parents[0][i];
            }
        }
        return children;
    }

    private boolean[][] onePoint(boolean[][] parents) {
        boolean[][] children = new boolean[2][grid.size()];

        int randomSplit = 2 + rand.nextInt((grid.size()-1) - 2 + 1);

        for (int i = 0; i < grid.size(); i++) {
            if(i<randomSplit){
                children[0][i] = parents[1][i];
                children[1][i] = parents[0][i];
            }else if(i>randomSplit){
                children[0][i] = parents[0][i];
                children[1][i] = parents[1][i];
            }
        }

        return children;
    }

    // evaluate a single chromosome
    private double evaluate_individual(boolean[] child) {


        int nturbines = 0;
        for (int i = 0; i < grid.size(); i++) {
            if (child[i]) {
                nturbines++;
            }
        }


        double[][] layout = new double[nturbines][2];
        int l_i = 0;
        for (int i = 0; i < grid.size(); i++) {
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
            //System.out.println("layout valid");
        } else {
            coe = Double.MAX_VALUE;
        }

        return coe;
    }

    // evaluates the whole population
    private void evaluate() {
        System.out.println("starting");
        double minfit = Double.MAX_VALUE;
        for (int p = 0; p < populationSize; p++) {
            int nturbines = 0;
            for (int i = 0; i < grid.size(); i++) {
                if (population[p][i]) {
                    nturbines++;
                }
            }

            double[][] layout = new double[nturbines][2];
            int l_i = 0;
            for (int i = 0; i < grid.size(); i++) {
                if (population[p][i]) {
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
        System.out.println("min " + minfit);
    }


}
