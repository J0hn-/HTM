/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

/**
 *
 * @author HP
 */
public class HTM {
    private static final int NUMBER_OF_COLUMN = 20;
    private static final int NUMBER_OF_INPUT = 20;
    private static final int CONNECTIVITY = 100; //% chance to create a synapse (link between input and column)
    private static final double SEUIL_SYNAPTIQUE = 0.5; // minimum value for a synapse value to be activated
    private static final double MIN_OVERLAP = 1.0; //minimum value for a column value(= sum of all activated synapses's value) to be activated
    private static final int ITERATION  = 500;
    private static final int DESIRED_LOCAL_ACTIVITY = 1; //a column is activiated only if its value is more than the value of its DESIRED_LOCAL_ACTIVITY neighbors
    private static final int INHIBITION_RADIUS = 3;//the number of neighbors left (or right) for a column, so total neighbors for a column is INHIBITION_RADIUS*2
    private static final int POURCENTAGE_ACTIVATED_INPUTS = 20; //% of inputs activated at the same time
    private static final double PERMANENCE_THRESHOLD=0.5; // minimum value for a CellSynapse permanence to be connected
    private static final int CELL_DENDRITE_THRESHOLD=5; //minium number of cellDendrite semgment active connected synapse to be activated 
    private static final int NUMBER_OF_CELL=3;

    private ArrayList<Boolean> input = new ArrayList<>();
    private int inputCursor;
    private static Random random;
    
    private ArrayList<Input> inputs = new ArrayList<>();
    private ArrayList<Column> columns = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        random = new Random();

        // Initialisation
        HTM htm = new HTM();
        
        int i = 0;
        while(i < ITERATION)
        {
            System.out.println(i);
            htm.iteration();
            i++;
            try {
                sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private HTM(){
        // generation of input
        for (int i = 0; i < NUMBER_OF_INPUT*20; i++)
        {
            //input.add(i >= i/NUMBER_OF_INPUT*NUMBER_OF_INPUT+i/NUMBER_OF_INPUT &&
            //        i < i/NUMBER_OF_INPUT*NUMBER_OF_INPUT+i/NUMBER_OF_INPUT+NUMBER_OF_INPUT*POURCENTAGE_ACTIVATED_INPUTS/100);
            input.add(i%2 == 0);
            //System.out.print((input.get(i) ? '●' : '○'));
        }

        // creation of inputs
        for(int i = 0; i < NUMBER_OF_INPUT; i++)
        {
            inputs.add(new Input(input.get(i)));
        }
        this.inputCursor = NUMBER_OF_INPUT;

        // creation of columns...
        for(int i = 0; i < NUMBER_OF_COLUMN; i++)
        {
            columns.add(new Column(MIN_OVERLAP, NUMBER_OF_CELL, CELL_DENDRITE_THRESHOLD));
            for(Input input : inputs)
            {
                //... and synapses between coluns and inputs with CONNECTIVITY % chance
                if((random.nextInt(101))<=CONNECTIVITY)
                {
                    columns.get(i).addSynaps(new Synapse(columns.get(i), input, SEUIL_SYNAPTIQUE));
                }
            }
        }        
        setupNeighborsOfColumn();
        // creation of cellSynapse
        for(int i = 0; i < NUMBER_OF_COLUMN; i++)
        {
            columns.get(i).setupCells(columns, PERMANENCE_THRESHOLD);
        }   
    }
    
    private void setupNeighborsOfColumn(){
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            ArrayList<Column> n = new ArrayList<>();
            for (int j = i - INHIBITION_RADIUS; j <= i + INHIBITION_RADIUS; j++)
            {
                if(j >= 0 && j < NUMBER_OF_COLUMN && j != i)
                {
                    n.add(columns.get(j));
                    //System.out.println(j);
                }

            }
            columns.get(i).setsNeighbors(n);
            //System.out.println("- " + i);
        }
    }
    
    private void iteration(){
        newInputs();
        reinitialisationSynapse();
        reinitialisationColumns();        
        inhibitionProcess();
        learning();
        temporalProcess();
        System.out.println(this);
        
    }
    
    private void temporalProcess(){
        for(Column c : columns)
        {
            c.cellsActivation();
        }
        for(Column c : columns)
        {
            c.cellsPrediction();
        }
    }
    
    private void inhibitionProcess(){
        for(Column c : columns)
        {
            c.inhibition(DESIRED_LOCAL_ACTIVITY);
        }
    }
    
    private void learning(){
        for(Column c : columns)
        {
            // Spatial learning
            if(c.isActivated())
            {
                c.updateSynapse();
            }
            c.boost();
            // temporal learning
            boolean correctPredicate = false;
            // check if previous predictate was correct
            if(c.isPredictateState() && c.isActivated())
            {
                correctPredicate = true;
            }
            // learning depend on correct predicate or not
            for(Cell cell : c.getsCells())
            {
                for(CellSynapse cs : cell.getDendrite())
                {
                    if(cs.isActivePredictate())
                    {
                        cs.setPermanence(cs.getPermanence() + (correctPredicate ? 0.1 : -0.1));
                    }
                    if(cs.isDesactivePredictate())
                    {
                        cs.setPermanence(cs.getPermanence() + (correctPredicate ? -0.1 : 0));
                    }
                    if(cs.isActivePotentialPredictate())
                    {
                        cs.setPermanence(cs.getPermanence() + (c.isActivated() ? 0.1 : -0.1));
                    }
                    if(cs.isDesactivePotentialPredictate())
                    {
                        cs.setPermanence(cs.getPermanence() + (c.isActivated() ? -0.1 : 0));
                    }
                    // reset cellSynapse predictate state
                    cs.setActivePredictate(false);
                    cs.setDesactivePredictate(false);
                    cs.setActivePotentialPredictate(false);
                    cs.setDesactivePotentialPredictate(false);
                }
            }
            // reset column predictate state
            c.setPredictateState(false);
        }        
    }
    
    private void newInputs(){
        int i = 0;
        for(Input input : inputs)
        {
            input.setValue(!input.isValue());
            //input.setValue(this.input.get(i + inputCursor));
            i++;
        }
        inputCursor = (inputCursor + NUMBER_OF_INPUT) % (NUMBER_OF_INPUT*20);
    }
    
    private void reinitialisationColumns(){
        for(Column column : columns)
        {
            column.setActivated(false);
            column.valCol();
        }
    }
    
    private void reinitialisationSynapse(){
        for(Column c : columns)
        {
            for(Synapse s : c.getsDendrite())
            {
                s.activate();
            }
        }
    }

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    @Override
    public String toString() {
        String s = "HTM" + System.lineSeparator();
        s += "Colmns:";
        String t = "States:";
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            s += "\t" + i;

            if (columns.get(i).isActivated() && columns.get(i).isPredictateState())
                t += ANSI_PURPLE + "\t■" + ANSI_RESET;
            else if (columns.get(i).isActivated())
                t += ANSI_GREEN + "\t■" + ANSI_RESET;
            else if(columns.get(i).isPredictateState())
                t += ANSI_CYAN +"\t□" + ANSI_RESET;
            else
                t += ANSI_RED +"\t□" + ANSI_RESET;
        }
        s += System.lineSeparator() + t + System.lineSeparator();
        ArrayList<String> cells = new ArrayList<>();
        cells.add("Cells:");
        for (int i = 1; i < NUMBER_OF_CELL; i++) {
            cells.add("\t");
        }
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            for(int j = 0; j < NUMBER_OF_CELL; j++) {
                Cell c = columns.get(i).getsCells().get(j);
                String z = cells.get(j);
                if (c.isActiveState() && c.isPredictateState())
                    z += ANSI_PURPLE + "\t●" + ANSI_RESET;
                else if (c.isActiveState())
                    z += ANSI_GREEN + "\t●" + ANSI_RESET;
                else if (c.isPredictateState())
                    z += ANSI_CYAN +"\t○" + ANSI_RESET;
                else
                    z += ANSI_RED +"\t○" + ANSI_RESET;
                cells.set(j,z);
            }
        }
        for (String w: cells) {
            s += w + System.lineSeparator();
        }
        s += "Inputs:";
        t = "States:";
        for(int i = 0; i < NUMBER_OF_INPUT; i++) {
            s += "\t" + i;
            if (inputs.get(i).isValue())
                t += ANSI_YELLOW + "\t●" + ANSI_RESET;
            else
                t += ANSI_BLUE + "\t○" + ANSI_RESET;
        }
        s += System.lineSeparator() + t + System.lineSeparator();
        s += "Values:";
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            if (columns.get(i).isActivated())
                s += ANSI_GREEN + "\t" + columns.get(i).getCurrentValue() + ANSI_RESET;
            else
                s += ANSI_RED +"\t" + columns.get(i).getCurrentValue() + ANSI_RESET;
        }
         s += System.lineSeparator();
        s += "Boost:";
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            if (columns.get(i).isActivated())
                s += ANSI_GREEN + "\t" + columns.get(i).getBoost() + ANSI_RESET;
            else
                s += ANSI_RED +"\t" + columns.get(i).getBoost() + ANSI_RESET;
        }
        s += System.lineSeparator();
        return s;
    }
}
