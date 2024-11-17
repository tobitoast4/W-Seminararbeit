import greenfoot.*;  

public class Auto extends Actor
{
    private int v;
    private int vMax;
    private int p; // ---- >(1/p) >> wahrscheinlichkeit mit der ein auto um 1 abbremst

    public Auto(int vM, int pNew){
        vMax = vM;
        p = pNew;
        v = vMax;
    }

    public Auto(){
        vMax = 4;
        p = 5;
        v = 1;
    }
    
    public Auto(int i){
        setImage("diagramm_point.png");
    }

    public Auto(int vM, int pNew, int v_n){
        vMax = vM;
        p = pNew;
        v = v_n;
        setImage("zelle_empty.png");
    }

    public void setColor(){
        int pic_nr;
        if(vMax == 0){
            pic_nr = 0;
        } else {
            pic_nr = (int) Math.round((v*10/vMax));
        }
        // int pic_nr = Greenfoot.getRandomNumber(vMax+1);
        switch(pic_nr){ 
            case 0: 
            setImage("v0.png");
            break; 
            case 1: 
            setImage("v1.png");
            break; 
            case 2: 
            setImage("v2.png");
            break; 
            case 3: 
            setImage("v3.png");
            break; 
            case 4: 
            setImage("v4.png");
            break; 
            case 5: 
            setImage("v5.png");
            break; 
            case 6: 
            setImage("v6.png");
            break;
            case 7: 
            setImage("v7.png");
            break; 
            case 8: 
            setImage("v8.png");
            break; 
            case 9: 
            setImage("v9.png");
            break; 
            default: 
            setImage("v9.png");
        } 
    } 

    public void setColorPxl(){ //bestimmt die Farben der Pixel fürs Diagramm
        int pic_nr;
        if(vMax == 0){
            pic_nr = 0;
        } else {
            pic_nr = (int) Math.round((v*10/vMax));
        }
        // int pic_nr = Greenfoot.getRandomNumber(vMax+1);
        switch(pic_nr){ 
            case 0: 
            setImage("pxl0.png");
            break; 
            case 1: 
            setImage("pxl1.png");
            break; 
            case 2: 
            setImage("pxl2.png");
            break; 
            case 3: 
            setImage("pxl3.png");
            break; 
            case 4: 
            setImage("pxl4.png");
            break; 
            case 5: 
            setImage("pxl5.png");
            break; 
            case 6: 
            setImage("pxl6.png");
            break;
            case 7: 
            setImage("pxl7.png");
            break; 
            case 8: 
            setImage("pxl8.png");
            break; 
            case 9: 
            setImage("pxl9.png");
            break; 
            default: 
            setImage("pxl9.png");
        } 
    } 

    public int getSpeed(){
        return v;
    }

    public void setSpeed(int j){
        v = j;
    }

    void test(){
        System.out.println(200 * 90 / 90);
    }
    
    public void setVMax(int n){
        vMax = n;
    }
    
    public boolean checkDoubles(){
        if(isTouching(Auto.class) == true){
            return true;
        } else {
            return false;
        }
    }
}
