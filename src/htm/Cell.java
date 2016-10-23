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
public class Cell {
       
    private ArrayList<CellSynapse> dendrite= new ArrayList<>(); // all CellSynapse poiting on this cell
    private boolean predictateState;
    private boolean activeState;
    private int dendriteThreshold;
    
    public Cell(int DENDRITE_THRESHOLD){
        this.predictateState = false;
        this.activeState=false;
        this.dendriteThreshold=DENDRITE_THRESHOLD;
    }
    
    public void addCellSynapse(Cell from, Cell to, double PERMANENCE_THRESHOLD)
    {
        this.dendrite.add(new CellSynapse(from, to, PERMANENCE_THRESHOLD));
    }
    public boolean predictate(){
        int compteur=0;
        int predicateCompteur=0;
        for(CellSynapse cs : dendrite)
        {
            if(cs.isConnected() && cs.getFrom().isActiveState())
            {
                compteur++;
            }
            if((!cs.isConnected()) && cs.getFrom().isActiveState())
            {
                predicateCompteur++;
            }
        }
        if(predicateCompteur+compteur >= dendriteThreshold)
        {
           for(CellSynapse cs : dendrite)
            {
                cs.setActivePotentialPredictate(cs.getFrom().isActiveState());
                cs.setDesactivePotentialPredictate(!cs.getFrom().isActiveState());
            } 
        }
        if(compteur >= dendriteThreshold)
        {
           predictateState = true;
           for(CellSynapse cs : dendrite)
            {
                cs.setActivePredictate(cs.getFrom().isActiveState());
                cs.setDesactivePredictate(!cs.getFrom().isActiveState());
            } 
        }
        return predictateState;
    }

    public ArrayList<CellSynapse> getDendrite() {
        return dendrite;
    }

    public void setDendrite(ArrayList<CellSynapse> dendrite) {
        this.dendrite = dendrite;
    }

    public boolean isPredictateState() {
        return predictateState;
    }

    public void setPredictateState(boolean predictateState) {
        this.predictateState = predictateState;
    }

    public boolean isActiveState() {
        return activeState;
    }

    public void setActiveState(boolean activeState) {
        this.activeState = activeState;
    }

    public int getDendriteThreshold() {
        return dendriteThreshold;
    }

    public void setDendriteThreshold(int dendriteThreshold) {
        this.dendriteThreshold = dendriteThreshold;
    }
}
