/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author HP
 */
public class HTM {
    private final static int NUMBER_OF_COLUMN=20;
    private final static int NUMBER_OF_INPUT=20;
    private final static int CONNECTIVITY=50; //% chance to create a synapse (link between input and column) 
    private final static double SEUIL_SYNAPTIQUE=0.5; // minimum value for a synapse value to be activated
    private final static double MIN_OVERLAP=1.5; //minimum value for a column value(= sum of all activated synapses's value) to be activated
    private final static int ITERATION=500;
    private final static int DESIRED_LOCAL_ACTIVITY=3; //a column is activiated only if its value is more than the value of its DESIRED_LOCAL_ACTIVITY neighbors
    private final static int INHIBITION_RADIUS = 3;//the number of neighbors left (or right) for a column, so total neighbors for a column is INHIBITION_RADIUS*2
    
    private static Random random;
    
    private static ArrayList<Input> inputs = new ArrayList<>();
    private static ArrayList<Column> columns = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        random = new Random();
        
        initialisation();
        
        int i=0;
        while(i<ITERATION)
        {
            iteration();
            i++;
        }
    }
    
    private static void initialisation(){
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
    
    private static void setupNeighborsOfColumn(){
        for(int i = 0; i < NUMBER_OF_COLUMN; i++) {
            ArrayList<Column> n = new ArrayList<>();
            for (int j = i - INHIBITION_RADIUS; j <= i + INHIBITION_RADIUS; j++)
            {
                if(j > 0 && j < NUMBER_OF_COLUMN && j != i)
                {
                    n.add(columns.get(j));
                    System.out.println(j);
                }

            }
            columns.get(i).setsNeighbors(n);
            System.out.println("- " + i);
        }
    }
    
    private static void iteration(){
        // newInputs();
        reinitialisationSynapse();
        reinitialisationColumns();        
        inhibitionProcess();
        learning();
        //affichage TODO
        
    }
    
    private static void inhibitionProcess(){
        for(Column c : columns)
        {
            c.inhibition(DESIRED_LOCAL_ACTIVITY);
        }
    }
    
    private static void learning(){
        for(Column c : columns)
        {
            if(c.isActivated())
            {
                c.updateSynaps();
            }
            c.boost();
        }
    }
    
    private static void newInputs(){
        for(Input input : inputs)
        {
            input.setValue(random.nextBoolean());
        }
    }
    
    private static void reinitialisationColumns(){
        for(Column column : columns)
        {
            column.setActivated(false);
            column.valCol();;
        }
    }
    
    private static void reinitialisationSynapse(){
        for(Column c : columns)
        {
            for(Synapse s : c.getsDendrite())
            {
                s.activate();
            }
        }
    }       
}
