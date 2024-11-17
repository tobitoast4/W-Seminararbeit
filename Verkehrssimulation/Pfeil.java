import greenfoot.*;  

public class Pfeil extends Actor
{
    public Pfeil(){
        
    }
    
    public Pfeil(String achse){
        if(achse == "x"){
            setImage("pfeil_x_achse.png");
        }
        if(achse == "y"){
            setImage("pfeil_y_achse.png");
        }
    }
}
