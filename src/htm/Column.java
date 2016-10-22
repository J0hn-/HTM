/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author HP
 */
public class Column {
    private final static boolean LEARNING=true;
    
    private boolean activated;
    private ArrayList<Synapse> dendrite = new ArrayList<>();
    private double currentValue; 
    
    private ArrayList<Column> neighbors = new ArrayList<>();
    private double minOverlap;
      
    private double boost;
    private ArrayList<Boolean> activations = new ArrayList<>();
    private ArrayList<Boolean> significantOverlaps = new ArrayList<>();
    
    private ArrayList<Cell> cells = new ArrayList<>();
    private double tempValue;
    
    public Column(double MIN_OVERLAP){
        this.minOverlap = MIN_OVERLAP;
        this.activated = false;
        this.boost = 1;
        this.currentValue = 0;
    }
    
    public double valCol(){
        int value = 0;
        for(Synapse s : dendrite)
        {
            if(s.passSyn())
            {
                value += s.getValeurSynaptique();
            }
        }
        if(value<minOverlap)
        {
            value=0;
        }
        return (LEARNING ? value*boost : value);         
    }
    
    public void updateActivations()
    {
        activations.add((activated ? true : false));
        if(activations.size() > 1000)
        {
            activations.remove(0);
        }
    }
    
    public double activeDutyCycle()
    {
        int compteur=0;
        for(boolean b : activations)
        {
            if(b)
            {
                compteur++;
            }
        }
        return compteur/activations.size();
    }
    
    public void updateSignificantOverlaps()
    {
        significantOverlaps.add((valCol() > minOverlap ? true : false));
        if(significantOverlaps.size() > 1000)
        {
            significantOverlaps.remove(0);
        }
    }
    
    public double overlapDutyCycle()
    {
        int compteur=0;
        for(boolean b : significantOverlaps)
        {
            if(b)
            {
                compteur++;
            }
        }
        return compteur/significantOverlaps.size();
    }
    
    public void updateSynaps(){
        for(Synapse s : dendrite)
        {
            s.setValeurSynaptique(s.getValeurSynaptique() + (s.passSyn() ? 0.1 : -0.1));
        }
    }
    
    public void inhibition(int DESIRED_LOCAL_ACTIVITY){
        ArrayList<Column> sortedNeighbors = getsNeighbors();
            sortedNeighbors.sort( new Comparator<Column>() {
                @Override
                public int compare(Column c1, Column c2) {
                    return Double.compare(c1.valCol(), c2.valCol());
                }
            });
            double minLocalActivity = getsNeighbors().get(DESIRED_LOCAL_ACTIVITY -1).valCol();
            
            if(valCol() > 0 && valCol() > minLocalActivity)
            {
                setActivated(true);
            }
            updateActivations();
    }
    
    public double maxDutyCycle(){
        double max = 0;
        for(Column c : neighbors)
        {
            if(max < c.activeDutyCycle())
            {
                max = c.activeDutyCycle();
            }
        }
        return max;
    }
    public void boost(){
        double activeDC = activeDutyCycle(); 
        double minDC = 0.01*maxDutyCycle();
        boost = ( activeDC > minDC ? 1 : 1+(boost*(1+(- activeDC + minDC))) );
        
        if(overlapDutyCycle() < minDC)
        {
            for(Synapse s : dendrite)
            {
                s.setValeurSynaptique(s.getValeurSynaptique() + (0.1*s.getSeuilSynaptique()));
            }
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean isActivated) {
        this.activated = isActivated;
    }

    public ArrayList<Cell> getsCells() {
        return cells;
    }

    public void setsCells(ArrayList<Cell> cells) {
        this.cells = cells;
    }

    public ArrayList<Column> getsNeighbors() {
        return neighbors;
    }

    public void setsNeighbors(ArrayList<Column> neighbors) {
        this.neighbors = neighbors;
    }

    public ArrayList<Synapse> getsDendrite() {
        return dendrite;
    }

    public void setsDendrite(ArrayList<Synapse> dentrite) {
        this.dendrite = dentrite;
    }
    
    public void addSynaps(Synapse s){
        this.dendrite.add(s);
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(float currentValue) {
        this.currentValue = currentValue;
    }

    public double getBoost() {
        return boost;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }

    public ArrayList<Boolean> getsActivations() {
        return activations;
    }

    public void setsActivations(ArrayList<Boolean> activations) {
        this.activations = activations;
    }

    public ArrayList<Boolean> getsSignificantOverlaps() {
        return significantOverlaps;
    }

    public void setsSignificantOverlaps(ArrayList<Boolean> significantOverlaps) {
        this.significantOverlaps = significantOverlaps;
    }
    
    
}
