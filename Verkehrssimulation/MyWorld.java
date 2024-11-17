import greenfoot.*;

public class MyWorld extends World
{
    private int anzahlZellen;
    private int anzahlAutos;
    private int vMax;
    private int p; //Trödelparameter
    private int dichte;
    private boolean spawnRandom;
    private boolean diagramme; //falls false, dann werden untere 4 Diagramme nicht erstellt
    private boolean nasch; //falls true, dann Nagel-Schreckenberg-Modell; falls false VDR-Modell

    private Text anzahlZellen_text, anzahlAutos_text, vMax_text, p_text;
    private Button zellen_less, zellen_more, autos_less, autos_more;
    private Button vMax_less, vMax_more, p_less, p_more;

    private Button zellen_less_10, zellen_more_10, autos_less_10, autos_more_10;
    private Button vMax_less_10, vMax_more_10, p_less_10, p_more_10;

    private Button button_spawn, button_diagramme;
    private Button button_nasch, button_vdr;

    private Button start;

    public MyWorld()
    {    
        super(1500, 900, 1); 
        Greenfoot.setSpeed(100);
        //Standarteinstellung:
        anzahlZellen = 350;
        dichte = 20;
        anzahlAutos = (int) Math.round(anzahlZellen * 20 / 100);
        vMax = 5;
        p = 15;
        spawnRandom = false; //Bei Änderung dieser Einstellung (im Code) muss auch der Button geändert werden!!!!!
        diagramme = true;
        nasch = false; 

        zellen_less = new Button(-1);
        zellen_more = new Button(1);
        autos_less = new Button(-1);
        autos_more = new Button(1);
        vMax_less = new Button(-1);
        vMax_more = new Button(1);
        p_less = new Button(-1);
        p_more = new Button(1);

        zellen_less_10 = new Button(-10);
        zellen_more_10 = new Button(10);
        autos_less_10 = new Button(-10);
        autos_more_10 = new Button(10);
        vMax_less_10 = new Button(-10);
        vMax_more_10 = new Button(10);
        p_less_10 = new Button(-10);
        p_more_10 = new Button(10);

        button_spawn = new Button(false);
        button_diagramme = new Button(true);

        button_nasch = new Button("nasch");
        button_vdr = new Button("vdr");

        start = new Button(0);

        addTexts();
        addButtons();
    }

    public void act(){
        checkButtons();
        refresh();
    }

    public void addTexts(){
        addObject(new Text("Nagel-Schreckenberg-/VDR-Modell", 40), 785, 65);

        addObject(new Text("Anzahl der Zellen", 25, 270), 797, 180);
        anzahlZellen_text = new Text("" + anzahlZellen, 25, 60);
        addObject(anzahlZellen_text, 758, 223);

        addObject(new Text("Verkehrsdichte", 25, 160), getWidth()/2, 280);
        anzahlAutos_text = new Text("" + dichte + "% ", 25, 80);
        addObject(anzahlAutos_text, getWidth()/2+16, 323);

        addObject(new Text("Maximale Geschwindigkeit", 25, 260), 745, 380);
        vMax_text = new Text("" + vMax, 25, 60);
        addObject(vMax_text, 767, 423);

        addObject(new Text("Trödelwahrscheinlichkeit", 25, 250), 750, 480);
        p_text = new Text("" + p + "%  ", 25, 80);
        addObject(p_text, 765, 523);

        addObject(new Text("Autos zufällig spawnen?", 26, 240), 750, 600);
        addObject(new Text("Untere Diagramme erstellen?", 26, 290), 750, 700);
    }

    public void refresh(){
        removeObject(anzahlZellen_text);
        anzahlZellen_text = new Text("" + anzahlZellen, 25, 60);
        addObject(anzahlZellen_text, 758, 223);

        removeObject(anzahlAutos_text);
        anzahlAutos_text = new Text("" + dichte + "% ", 25, 80);
        if(dichte == 100){
            addObject(anzahlAutos_text, getWidth()/2+12, 323);
        } else if (dichte == 0){
            addObject(anzahlAutos_text, getWidth()/2+21, 323);
        }else {
            addObject(anzahlAutos_text, getWidth()/2+16, 323);
        }

        removeObject(vMax_text);
        vMax_text = new Text("" + vMax, 25, 60);
        addObject(vMax_text, 767, 423);

        removeObject(p_text);
        p_text = new Text("" + p + "% ", 25, 80);
        if(p == 100){
            addObject(p_text, getWidth()/2+11, 523);
        } else if (p == 0){
            addObject(p_text, getWidth()/2+20, 523);
        }else {
            addObject(p_text, getWidth()/2+15, 523);
        }
    }

    public void addButtons(){
        addObject(zellen_less, 680, 200);
        addObject(zellen_more, getWidth()-680, 200);
        addObject(autos_less, 680, 300);
        addObject(autos_more, getWidth()-680, 300);
        addObject(vMax_less, 680, 400);
        addObject(vMax_more, getWidth()-680, 400);
        addObject(p_less, 680, 500);
        addObject(p_more, getWidth()-680, 500);

        addObject(zellen_less_10, 630, 200);
        addObject(zellen_more_10, getWidth()-630, 200);
        addObject(autos_less_10, 630, 300);
        addObject(autos_more_10, getWidth()-630, 300);
        addObject(vMax_less_10, 630, 400);
        addObject(vMax_more_10, getWidth()-630, 400);
        addObject(p_less_10, 630, 500);
        addObject(p_more_10, getWidth()-630, 500);

        addObject(button_spawn, 753, 625);
        addObject(button_diagramme, 753, 725);

        addObject(button_vdr, getWidth()/2+3, 110);

        addObject(start, getWidth()/2+3, 830);
    }

    public void checkButtons(){
        if(zellen_more.isClicked() == true){
            anzahlZellen++;
        }
        if(zellen_less.isClicked() == true){
            anzahlZellen--;
        }
        if(autos_more.isClicked() == true){
            dichte++;
            anzahlAutos = (int) Math.round(anzahlZellen * dichte / 100);
        }
        if(autos_less.isClicked() == true){
            dichte--;
            anzahlAutos = (int) Math.round(anzahlZellen * dichte / 100);
        }
        if(vMax_more.isClicked() == true){
            vMax++;
        }
        if(vMax_less.isClicked() == true){
            vMax--;
        }
        if(p_more.isClicked() == true){
            p++;
        }
        if(p_less.isClicked() == true){
            p--;
        }

        if(zellen_more_10.isClicked() == true){
            anzahlZellen = anzahlZellen + 10;
            anzahlAutos = (int) Math.round(anzahlZellen * dichte / 100);
        }
        if(zellen_less_10.isClicked() == true){
            anzahlZellen = anzahlZellen - 10;
            anzahlAutos = (int) Math.round(anzahlZellen * dichte / 100);
        }
        if(autos_more_10.isClicked() == true){
            dichte = dichte + 10;
            anzahlAutos = (int) Math.round(anzahlZellen * dichte / 100);
        }
        if(autos_less_10.isClicked() == true){
            dichte = dichte -10;
            anzahlAutos = (int) Math.round(anzahlZellen * dichte / 100);
        }
        if(vMax_more_10.isClicked() == true){
            vMax = vMax + 10;
        }
        if(vMax_less_10.isClicked() == true){
            vMax = vMax - 10;
        }
        if(p_more_10.isClicked() == true){
            p = p + 10;
        }
        if(p_less_10.isClicked() == true){
            p = p - 10;
        }

        if(anzahlZellen < 0){
            anzahlZellen = 0;
        }
        if(anzahlAutos < 0){
            anzahlAutos = 0;
        }
        if(anzahlAutos > anzahlZellen){
            anzahlAutos = anzahlZellen;
        }
        if(vMax < 0){
            vMax = 0;
        }
        if(p < 0){
            p = 0;
        }
        if(p > 100){
            p = 100;
        }
        if(dichte > 100){
            dichte = 100;
        }
        if(dichte < 0){
            dichte = 0;
        }
        if(button_spawn.isClicked() == true){
            removeObject(button_spawn);
            if(spawnRandom == true){
                button_spawn = new Button(false);
                addObject(button_spawn, 753, 625);
                spawnRandom = false;
            } else {
                button_spawn = new Button(true);
                addObject(button_spawn, 753, 625);
                spawnRandom = true;
            }
        }
        if(button_diagramme.isClicked() == true){
            removeObject(button_diagramme);
            if(diagramme == true){
                button_diagramme = new Button(false);
                addObject(button_diagramme, 753, 725);
                diagramme = false;
            } else {
                button_diagramme = new Button(true);
                addObject(button_diagramme, 753, 725);
                diagramme = true;
            }
        }

        if(start.isClicked() == true){
            Simulation w = new Simulation(anzahlZellen, anzahlAutos, vMax, p, dichte, diagramme, spawnRandom, nasch);
            Greenfoot.setWorld(w);
        }
        if(button_nasch.isClicked() == true){
            removeObject(button_nasch);
            addObject(button_vdr, getWidth()/2, 110);
            nasch = false;
        }
        if(button_vdr.isClicked() == true){
            removeObject(button_vdr);
            addObject(button_nasch, getWidth()/2, 110);
            nasch = true;
        }
    }

}
