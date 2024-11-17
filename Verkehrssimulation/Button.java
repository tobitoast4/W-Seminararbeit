import greenfoot.*;  

public class Button extends Actor
{
    public Button(){

    }
    
    public Button(boolean b){
        if(b == true){
            setImage("button_yes.png");
        }
        if(b == false){
             setImage("button_no.png");
        }
    }
    
    public Button(String s){
        if(s == "nasch"){
            setImage("button_nasch.png");
        }
        if(s == "vdr"){
            setImage("button_vdr.png");
        }
    }

    public Button(int i){
        if(i == 1){
            setImage("button_add.png");
        }
        if(i == -1){
            setImage("button_sub.png");
        }
        if(i == 10){
            setImage("button_add_10.png");
        }
        if(i == -10){
            setImage("button_sub_10.png");
        }
        if(i == 0){
            setImage("button_start.png");
        }       
    }

    public void act() 
    {   

    }    

    public boolean isClicked(){
        if(Greenfoot.mouseClicked(this)){
            return true;
        } else {
            return false;
        }    
    }
    
    public void remove(){
        
    
    }
}
