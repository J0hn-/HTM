/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

import java.util.Random;

/**
 *
 * @author HP
 */
public class CellSynapse {
    private Cell from;
    private Cell to;
    private double permanence;
    private double permanenceThreshold;
    private boolean activePredictate;
    private boolean desactivePredictate;
    private boolean activePotentialPredictate;
    private boolean desactivePotentialPredictate;
    
    public CellSynapse(Cell from, Cell to, double PERMANENCE_THRESHOLD){
        Random r = new Random();
        this.from = from;
        this.to = to;
        this.permanenceThreshold = PERMANENCE_THRESHOLD;
        this.permanence = (r.nextInt(11))/10;
    }
    
    public boolean isConnected()
    {
        return permanence >= permanenceThreshold;
    }

    public Cell getFrom() {
        return from;
    }

    public Cell getTo() {
        return to;
    }

    public double getPermanence() {
        return permanence;
    }

    public void setPermanence(double permanence) {
        this.permanence = permanence;
        if(permanence > 1)
        {
            permanence = 1;
        }
         if(permanence < 0)
        {
            permanence = 0;
        }
    }

    public double getPermanenceThreshold() {
        return permanenceThreshold;
    }

    public boolean isActivePredictate() {
        return activePredictate;
    }

    public void setActivePredictate(boolean activePredictate) {
        this.activePredictate = activePredictate;
    }

    public boolean isDesactivePredictate() {
        return desactivePredictate;
    }

    public void setDesactivePredictate(boolean desactivePredictate) {
        this.desactivePredictate = desactivePredictate;
    }

    public boolean isActivePotentialPredictate() {
        return activePotentialPredictate;
    }

    public void setActivePotentialPredictate(boolean activePotentialPredictate) {
        this.activePotentialPredictate = activePotentialPredictate;
    }

    public boolean isDesactivePotentialPredictate() {
        return desactivePotentialPredictate;
    }

    public void setDesactivePotentialPredictate(boolean desactivePotentialPredictate) {
        this.desactivePotentialPredictate = desactivePotentialPredictate;
    }
    
    
}
