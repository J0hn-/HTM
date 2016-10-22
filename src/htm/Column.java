/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.min;
import static jdk.nashorn.internal.objects.NativeMath.max;

/**
 *
 * @author HP
 */
public class Column {
    private final static boolean LEARNING = true;

    private boolean activated;
    private ArrayList<Synapse> dendrite = new ArrayList<>();
    private double currentValue; //sum of all synaptiques values of all activated synapses of it's dendrite

    private ArrayList<Column> neighbors = new ArrayList<>();
    private double minOverlap; //minimum value of its current value to be concidered as significant activated

    private double boost; //boost value for learning
    private ArrayList<Boolean> activations = new ArrayList<>(); // list of its 1000 last states (activated or not)
    private ArrayList<Boolean> significantOverlaps = new ArrayList<>(); // list of ots 1000 last significant overlaps states (sgnificant activated or not)

    private ArrayList<Cell> cells = new ArrayList<>();

    public Column(double MIN_OVERLAP) {
        this.minOverlap = MIN_OVERLAP;
        this.activated = false;
        this.boost = 1;
        this.currentValue = 0;
        activations.add(false);
        significantOverlaps.add(false);
    }

    // calculation of current value
    public void valCol() {
        int value = 0;
        for (Synapse s : dendrite) {
            if (s.passSyn()) {
                value += s.getValeurSynaptique();
            }
        }
        if (value < minOverlap) {
            value = 0;
        }
        currentValue = (LEARNING ? value * boost : value);
    }

    public void updateActivations() {
        activations.add(activated);
        if (activations.size() > 1000) {
            activations.remove(0);
        }
    }

    //return % of activations over the last 1000 state
    public double activeDutyCycle() {
        int compteur = 0;
        for (boolean b : activations) {
            if (b) {
                compteur++;
            }
        }
        return (double) compteur / activations.size();
    }

    public void updateSignificantOverlaps() {
        significantOverlaps.add((getCurrentValue() > minOverlap));
        if (significantOverlaps.size() > 1000) {
            significantOverlaps.remove(0);
        }
    }

    //return % of significant activations over the last 1000 states
    public double overlapDutyCycle() {
        int compteur = 0;
        for (boolean b : significantOverlaps) {
            if (b) {
                compteur++;
            }
        }
        return (double) compteur / significantOverlaps.size();
    }

    // learning over the dendrite
    public void updateSynapse() {
        for (Synapse s : dendrite) {
            s.setValeurSynaptique(s.getValeurSynaptique() + (s.passSyn() ? 0.1 : -0.1));
        }
    }

    // activation in function of its neighbors
    public void inhibition(int DESIRED_LOCAL_ACTIVITY) {
        ArrayList<Column> sortedNeighbors = getsNeighbors();
        sortedNeighbors.sort((c1, c2) -> Double.compare(c2.getCurrentValue(), c1.getCurrentValue()));
        double minLocalActivity = sortedNeighbors.get(DESIRED_LOCAL_ACTIVITY - 1).getCurrentValue();

        if (getCurrentValue() > 0 && getCurrentValue() >= minLocalActivity) {
            setActivated(true);
        }
        if(getCurrentValue() > 0 && getCurrentValue() == minLocalActivity)
        {
            setCurrentValue(getCurrentValue() + 1);
        }
        updateActivations();
    }

    // in its neighbors, return the maximum % of activations of the last 1000 states
    public double maxDutyCycle() {
        double max = 0;
        for (Column c : neighbors) {
            if (max < c.activeDutyCycle()) {
                max = c.activeDutyCycle();
            }
        }
        return max;
    }


    // boost for learning if necessary
    public void boost() {
        double activeDC = activeDutyCycle();
        double minDC = 0.01 * maxDutyCycle();
        boost = (activeDC > minDC ? 1 : 1 + (boost * (1 + (-activeDC + minDC))));

        if (overlapDutyCycle() < minDC) {
            for (Synapse s : dendrite) {
                s.setValeurSynaptique(min(s.getValeurSynaptique() + (0.1 * s.getSeuilSynaptique()), 1));
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

    public void setCurrentValue(double currentValue) {
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
