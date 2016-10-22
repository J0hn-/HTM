/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class Column {
    private static boolean LEARNING=true;
    
    private double minOverlap;
    
    private boolean activated;
    private ArrayList<Cell> cells;
    private ArrayList<Column> neighbors;
    private ArrayList<Synaps> dendrite;
    private double currentValue;
    
    private double boost;
    private boolean[] activations;
    private boolean[] significantOverlaps;
    
    public Column(double mo){
        this.minOverlap = mo;
        this.activated = false;
        this.boost = 1;
        this.currentValue = 0;
    }
    
    public double valCol(){
        int value = 0;
        for(Synaps s : dendrite)
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
    
    public void updateSynaps(){
        for(Synaps s : dendrite)
        {
            s.setValeurSynaptique(s.getValeurSynaptique() + (s.passSyn() ? 0.1 : -0.1));
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public void setIsActivated(boolean isActivated) {
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

    public ArrayList<Synaps> getsDendrite() {
        return dendrite;
    }

    public void setsDendrite(ArrayList<Synaps> dentrite) {
        this.dendrite = dentrite;
    }
    
    public void addSynaps(Synaps s){
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

    public boolean[] getsActivations() {
        return activations;
    }

    public void setsActivations(boolean[] activations) {
        this.activations = activations;
    }

    public boolean[] getsSignificantOverlaps() {
        return significantOverlaps;
    }

    public void setsSignificantOverlaps(boolean[] significantOverlaps) {
        this.significantOverlaps = significantOverlaps;
    }
    
    
}
