import greenfoot.*;  

public class Point extends Actor
{
    public Point(){
    
    }
    public boolean checkDoubles(){
        if(isTouching(Point.class) == true){
            return true;
        } else {
            return false;
        }
    }
}
