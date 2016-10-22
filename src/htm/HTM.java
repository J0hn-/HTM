/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author HP
 */
public class HTM {
    private final static int NUMBER_OF_COLUMN=20;
    private final static int NUMBER_OF_INPUT=20;
    private final static int CONNECTIVITY=50; //% de chance qu'un input soit relié à une column
    private final static double SEUIL_SYNAPTIQUE=0.5; // minimum value for a synaps value to be activated
    private final static double MIN_OVERLAP=1.5; //minimum value for a column value(= sum of activated synaps value) to be activated
    private final static int ITERATION=500;
    private final static int DESIRED_LOCAL_ACTIVITY=3;
    private final static int INHIBITION_RADIUS = 3;
    
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
        for(int i = 0; i < NUMBER_OF_INPUT; i++)
        {
            inputs.add(new Input(random.nextBoolean()));
        }
        
        for(int i = 0; i < NUMBER_OF_COLUMN; i++)
        {
            columns.add(new Column(MIN_OVERLAP));
            for(Input input : inputs)
            {
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
        reinitialisationColumns();
        reinitialisationSynaps();
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
        }
    }
    
    private static void reinitialisationSynaps(){
        for(Column c : columns)
        {
            for(Synapse s : c.getsDendrite())
            {
                s.activate();
            }
        }
    }       
}
