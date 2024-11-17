import greenfoot.*;  

public class Text extends Actor
{
    private String text; 
    
    public Text(int i)  
    {  
        if(i == 0){
            setImage("verkehrsfluss_symbol.PNG");
        }
        if(i == 1){
            setImage("dichte_symbol.PNG");        
        }
    }  
    
    public Text(String t, int size){
        text = t;
        Color bgColor = new Color(0, 0, 0, 0); // transparent background
        Color fontColor = Color.BLACK;
        int fontsize = size;

        GreenfootImage txtImg = new GreenfootImage(text, fontsize, fontColor, bgColor);
        GreenfootImage img = new GreenfootImage (600, 80);
        
        img.setColor(bgColor);
        img.fill();
        img.drawImage(txtImg, 10, 5);
        setImage(img);

        // https://www.greenfoot.org/topics/4227
    }
    
    public Text(String t, int size, int breite){
        text = t;
        Color bgColor = new Color(0, 0, 0, 0); // transparent background
        Color fontColor = Color.BLACK;
        int fontsize = size;

        GreenfootImage txtImg = new GreenfootImage(text, fontsize, fontColor, bgColor);
        GreenfootImage img = new GreenfootImage (breite, 80);
        
        img.setColor(bgColor);
        img.fill();
        img.drawImage(txtImg, 10, 5);
        setImage(img);

        // https://www.greenfoot.org/topics/4227
    }
    
    public Text(String t){
        text = t;
        Color bgColor = new Color(0, 0, 0, 0); // transparent background
        Color fontColor = Color.BLACK;
        int fontsize = 30;

        GreenfootImage txtImg = new GreenfootImage(text, fontsize, fontColor, bgColor);
        GreenfootImage img = new GreenfootImage (50, 50);
        
        img.setColor(bgColor);
        img.fill();
        img.drawImage(txtImg, 10, 5);
        setImage(img);

        // https://www.greenfoot.org/topics/4227
    }

    public void setText(String t){  
        text = t;
    }   
}
