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
public class Synaps {
    
    private boolean activated;
    private double seuilSynaptique;
    private double valeurSynaptique;
    private Input input;
    private Column column;
    
    public Synaps(Column c, Input i, double s){
        Random r = new Random();
        this.input = i;
        this.column = c;
        this.activated = false;
        this.valeurSynaptique = (r.nextInt(11))/10;
        this.seuilSynaptique = s;
    }
    
    public boolean passSyn(){
        return this.valeurSynaptique > this.seuilSynaptique && input.isValue();
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    
    public void activate(){
        this.activated = passSyn();
    }

    public double getSeuilSynaptique() {
        return seuilSynaptique;
    }

    public void setSeuilSynaptique(double seuilSynaptique) {
        this.seuilSynaptique = seuilSynaptique;
        if(this.valeurSynaptique > 1)
        {
            valeurSynaptique = 1;
        }
        if(this.valeurSynaptique < 0)
        {
            valeurSynaptique = 0;
        }
    }

    public double getValeurSynaptique() {
        return valeurSynaptique;
    }

    public void setValeurSynaptique(double valeurSynaptique) {
        this.valeurSynaptique = valeurSynaptique;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }
    
    
    
}
