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
    private static final double MIN_OVERLAP = 1.5; //minimum value for a column value(= sum of all activated synapses's value) to be activated
    private static final int ITERATION  = 500;
    private static final int DESIRED_LOCAL_ACTIVITY = 3; //a column is activiated only if its value is more than the value of its DESIRED_LOCAL_ACTIVITY neighbors
    private static final int INHIBITION_RADIUS = 3;//the number of neighbors left (or right) for a column, so total neighbors for a column is INHIBITION_RADIUS*2
    
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
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private HTM(){
        // creation of inputs
        for(int i = 0; i < NUMBER_OF_INPUT; i++)
        {
            inputs.add(new Input(random.nextBoolean()));
        }
        // creation of columns...
        for(int i = 0; i < NUMBER_OF_COLUMN; i++)
        {
            columns.add(new Column(MIN_OVERLAP));
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
    }
    
    private void setupNeighborsOfColumn(){
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            ArrayList<Column> n = new ArrayList<>();
            for (int j = i - INHIBITION_RADIUS; j <= i + INHIBITION_RADIUS; j++)
            {
                if(j > 0 && j < NUMBER_OF_COLUMN && j != i)
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
        // newInputs();
        reinitialisationSynapse();
        reinitialisationColumns();        
        inhibitionProcess();
        learning();
        System.out.println(this);
        
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
            if(c.isActivated())
            {
                c.updateSynapse();
            }
            c.boost();
        }
    }
    
    private void newInputs(){
        for(Input input : inputs)
        {
            input.setValue(random.nextBoolean());
        }
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

    @Override
    public String toString() {
        String s = "HTM" + System.lineSeparator();
        s += "Columns:";
        String t = "States:  ";
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            s += " " + i;

            if (columns.get(i).isActivated())
                t += "■";
            else
                t += "□";

            for(int j = 0; j <= i/10; j++)
                t += ' ';
        }
        s += System.lineSeparator() + t + System.lineSeparator();
        s += "Inputs: ";
        t = "States:  ";
        for(int i = 0; i < NUMBER_OF_INPUT; i++) {
            s += " " + i;
            if (inputs.get(i).isValue())
                t += "●";
            else
                t += "○";
            for(int j = 0; j <= i/10; j++)
                t += ' ';
        }
        s += System.lineSeparator() + t + System.lineSeparator();
        s += "Values:";
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            s += " " + columns.get(i).getCurrentValue();
        }
        s += System.lineSeparator();
        return s;
    }
}
