public class Data  
{
    private int runde;
    private int autosAnz;
    private int auto_nr; //nummer des letzten Autos, welches gemessen wurde
    private int xWert; //gibt an an welchem x-Wert der nächste Punkt gesetzt wird (für diagramm1)
    private int xWert2; //gibt an an welchem x-Wert der nächste Punkt gesetzt wird (für diagramm 2)
    
    private int[] flow_value;
    private int counter;
    
    public Data()
    {
        runde = 0;
        autosAnz = 0;
        auto_nr = -1;
        xWert = 0;
        xWert2 = 0;
        
        flow_value = new int[1000];
        flow_value[1000-1] = -1;
        counter = 0;
    }
    
    public void setXWert2(int x){
        xWert2 = x;
    }
    
    public int getXWert2(){
        return xWert2;
    }
    
    public void setXWert(int x){
        xWert = x;
    }
    
    public int getXWert(){
        return xWert;
    }
    
    public void setRunde(int r){
        runde = r;
    }
    
    public int getRunde(){
        return runde;
    }
    
    public void setAutosAnz(int i){
        autosAnz = i;
    }
    
    public int getAutosAnz(){
        return autosAnz;
    }
    
    public void setAutoNr(int i){
        if(i == -1){
        }
        if(i == auto_nr){
        }
        if(i != auto_nr){
            autosAnz++;
        }
        auto_nr = i;
    }
    
    public int getAutoNr(){
        return auto_nr;
    }
    
    public void addFlowValue(int f){
        flow_value[counter] = f;
        
        counter++;
        if(counter >=1000){
            counter = 0;
        }
        
        
        int anz;
        if(flow_value[1000-1] == -1){
            anz = counter;
        } else {
            anz = 1000;
        }

        int avgFlow = 0;
        for (int i = 0; i < anz; i++){
            avgFlow = avgFlow + flow_value[i];        
        }
        
        avgFlow = avgFlow/anz+1;
        System.out.println("" + avgFlow);
    }
    
   

}
