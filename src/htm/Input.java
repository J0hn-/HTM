/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htm;

/**
 *
 * @author HP
 */
public class Input {
    
    private boolean value;
    
    public Input() {
        this.value=false;
    }
    
    public Input(boolean initialeValue){
        this.value=initialeValue;
    }

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
