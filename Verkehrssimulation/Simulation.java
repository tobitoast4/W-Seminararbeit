
import greenfoot.*;  
import java.util.List;

public class Simulation extends World
{
    Zelle_Line[] zelle_line;
    Zelle[] zelle;
    Auto[] auto;
    int height, width;
    int radius;
    Text avgSpeedAnzeige, text_dichte, text_p, text_vMax, text_p2;
    int anzahlZellen;
    int anzahlAutos;
    int vMax;
    int p;
    int p2;
    double dichte_global;

    Pfeil pfx1, pfy1,  pfx2, pfy2, pfx3, pfy3, pfx4, pfy4;

    Data data;

    Point[] point1; // wird in addPfeile() instanziiert
    Point[] point2; //   ...
    Point[] point3; //   ...
    Auto[][] point4; //   ...
    boolean diagramme; //bezieht sich auf die unteren vier Diagramme
    boolean spawnRandom;
    boolean start;
    boolean nasch;
    Button button_start, button_pause;
    Button dichte_more, dichte_less, dichte_more_10, dichte_less_10;
    Button p_more, p_less, p_more_10, p_less_10;
    Button vMax_more, vMax_less;
    Button p2_more, p2_less, p2_more_10, p2_less_10;

    Auto[][] table;       //bezieht sich auf oberes, großes Diagramm
    int höheDiagramm;     // -------------------"-------------------
    int diagrammLine;     // -------------------"-------------------
    int pxl;              // -------------------"-------------------

    public Simulation(int anzZellen_n, int anzAutos_n, int vMax_n, int p_n, double dichte_n, boolean diagramme_n, boolean spawnRandom_n, boolean nasch_n)
    {    
        super(1500, 900, 1); 
        Greenfoot.setSpeed(100);
        height = getHeight();
        width = getWidth();
        diagrammLine = 0;
        button_start = new Button(0);
        button_pause = new Button(0);
        start = false;        
        anzahlZellen = anzZellen_n;
        dichte_global = dichte_n;
        anzahlAutos = anzAutos_n;
        vMax = vMax_n;
        nasch = nasch_n;
        p = p_n; // p wird p*0.01 d.h wenn p = 50 dann ist die trödelwahrscheinlichkeit 0.5
        if(nasch == true){ 
            p2 = p;
        } else {
            p2 = 2*p;
        }

        if(p2 > 100){
            p2 = 100;
        }

        diagramme = diagramme_n; //wenn true werden die unteren diagramme auch angezeigt
        spawnRandom = spawnRandom_n;
        if(diagramme == true){
            data = new Data();
            addPfeile();
            höheDiagramm = 550;
        } else {
            höheDiagramm = height;
        }        

        pxl = 2; //Bestimmt die Breite der Punkte des oberen Diagramms in Pixeln (Textur muss seperat geändert werden!)
        table = new Auto[höheDiagramm/pxl][700/pxl];

        addObject(new Pfeil(), 390, 825); //Pfeil in der Mitte des Kreises, der die Fahrtrichtung angibt
        createZellen(anzahlZellen);
        if(spawnRandom == true){
            createAutosRandom();
        } else {
            createAutos(anzahlAutos);
        }
        NaSchRegeln(); //direkt nach spawnen der Autos MUSS auf Sichherheitsabstand geprüft werden, da dies in der Methode act() erst nach dem move() passiert
        addTexts();   
        addButtons();
        drawDiagramm();
    }

    public void act(){
        if(start == false){
            checkButtons();
        }
        if(start == true){
            if(diagramme == true){ 
                drawDiagramms();//AllE Berechnungen in dieser Methode beziehen sich auf 100 zellen ab zelle[0] (berechnet und zeichnet Werte der unteren Diagramme ein
            }
            move(); //bewegt die Autos um v(siehe Auto.cls) Zellen weiter
            checkButtons();
            NaSchRegeln(); //berechnet v für die nächste Runde
            setColors();
            drawDiagramm(); //zeichnet eine Zeile des oberen Diagramms
            avgSpeedRefresh();  // aktualisiert die Durchschnittsgeschwindigkeitsanzeige
        }
    }

    public void createZellen(int anzahl){
        zelle_line = new Zelle_Line[anzahl];
        zelle = new Zelle[anzahl];
        for (int i = 0; i < anzahl; i++){
            zelle_line[i] = new Zelle_Line();
        }
        for (int i = 0; i < anzahl; i++){
            zelle[i] = new Zelle();
        }

        radius = height/100*42;
        double d = 2*Math.PI/anzahl;
        for (int i = 0; i < anzahl; i++){ //Berechnung der Position der einzelnen Barrieren + Platzierung
            int x = (int) Math.round(radius * Math.cos(i*d));
            x = x + width/2-365;
            int y = (int) Math.round(radius * Math.sin(i*d));
            y = height/2 - y + 50;
            addObject(zelle_line[i], x, y);
            zelle_line[i].turn(90-i*360/anzahl);
        }      
        for (int i = 0; i < anzahl; i++){ //Berechnung der Position der einzelnen Zellen + Platzierung
            int x = (int) Math.round(radius * Math.cos((i+(0.5))*d));
            x = x + width/2-365;
            int y = (int) Math.round(radius * Math.sin((i+(0.5))*d));
            y = height/2 - y + 50;
            addObject(zelle[i], x, y);
            // zelle[i].turn(90-i*360/anzahl); //optional, da Zellen ohnehin unsichtbar
        } 

        //Erstellung zweier extra Linien zur Markierung von zelle_linie[0]
        Zelle_Line z0 = new Zelle_Line();
        Zelle_Line z1 = new Zelle_Line();
        addObject(z0, zelle_line[0].getX()+10, zelle_line[0].getY());
        addObject(z1, zelle_line[0].getX()-10, zelle_line[0].getY());
        z0.setRotation(90);
        z1.setRotation(90);
    }

    public void createAutos(int anzahl){
        auto = new Auto[anzahlZellen];
        for(int i = 0; i < anzahlAutos; i++){ //Bestimmung der Position jeden Autos ANHAND der Zellen, nicht des Winkels
            int pos = (int) Math.round(anzahlZellen * i /anzahlAutos);
            zelle[pos].setGefüllt(true);
            int x = zelle[pos].getX();
            int y = zelle[pos].getY();
            auto[i] = new Auto(vMax, p);
            addObject(auto[i], x, y);  
            auto[i].setColor();
        }
    }

    public void createAutosRandom(){
        auto = new Auto[anzahlZellen];
        for (int i = 0; i < anzahlAutos; i++){
            int k = Greenfoot.getRandomNumber(anzahlZellen);
            while(zelle[k].getGefüllt() == true){ 
                k = Greenfoot.getRandomNumber(anzahlZellen);
            } 
            auto[i] = new Auto(vMax, p);
            addObject(auto[i], zelle[k].getX(), zelle[k].getY());
            zelle[k].setGefüllt(true);
            auto[i].setColor();
        }
    }

    public void setColors(){
        for(int i = 0; i < anzahlAutos; i++){
            if(auto[i] != null){
                auto[i].setColor();
            }
        }
    }

    public void NaSchRegeln(){
        for (int i = 0; i < anzahlAutos; i++){
            if(auto[i] != null){

                //Regel 0 (aus VDR-Modell)
                int p_n = p; 
                if(nasch == false){
                    if(auto[i].getSpeed() == 0){ //p_n(eu) entspricht HIER p2
                        p_n = p2;
                    }
                } 

                //Regel 1
                if (auto[i].getSpeed() < vMax){
                    auto[i].setSpeed(auto[i].getSpeed()+1);
                }

                //Regel 2
                int z = -1;
                for (int k = 0; k < anzahlZellen; k++){
                    if((zelle[k].getX() == auto[i].getX()) && (zelle[k].getY() == auto[i].getY())){
                        z = k;
                        break;
                    }            
                }
                if(z < 0){
                    System.out.println("Nicht allle Autos befinden sich in einer Zelle!!!");
                }

                int frei = 0;
                for(int j = 0; j < auto[i].getSpeed(); j++){
                    if(auto[i].getSpeed() == 0){
                        break;
                    }
                    int sum = z + j + 1; 
                    if(sum >= anzahlZellen){
                        sum = sum - anzahlZellen;
                    }
                    if(zelle[sum].getGefüllt() == false){
                        frei++;
                    } else if(zelle[sum].getGefüllt() == true){
                        break;
                    }
                }
                auto[i].setSpeed(frei);

                //Regel 3
                if (p != 0){
                    if(auto[i].getSpeed()>0){
                        if(Greenfoot.getRandomNumber(100) < p_n){
                            auto[i].setSpeed(auto[i].getSpeed()-1);
                        }
                    }
                }
            }
        } 
    }

    public void move(){ //Regel 4
        for(int i = 0; i < anzahlAutos; i++){
            if(auto[i] != null){
                int v = auto[i].getSpeed();
                int z = -1;
                //Zelle in der sich Auto nr. i befindet wird ermittelt
                for (int k = 0; k < anzahlZellen; k++){
                    if((zelle[k].getX() == auto[i].getX()) && (zelle[k].getY() == auto[i].getY())){
                        z = k;
                    }
                }
                if(z < 0){  //check ob alle Autos in Zellen
                    System.out.println("Nicht alle Autos befinden sich in einer Zelle!!!");
                }
                zelle[z].setGefüllt(false); //da Auto ja die Zelle verlässt

                //check ob Auto nicht "über die Anzahl der Zellen hinausfährt"
                int sum = z + v;
                if(sum >= anzahlZellen){
                    sum = sum - anzahlZellen;
                }

                //Neuplatzierung des Autos
                int x_new = zelle[sum].getX();
                int y_new = zelle[sum].getY();
                auto[i].setLocation(x_new, y_new);
                zelle[sum].setGefüllt(true);
            }
        }
    }

    public void addTexts(){
        if(nasch == true){
            addObject(new Text("Nagel-Schreckenberg-Modell", 35), 500, 65);
        }
        if(nasch == false){
            addObject(new Text("VDR-Modell", 35), 598, 70);
        }
        addObject(new Text("Durchschnittliche Geschwindigkeit:", 20), 540, 245);
        text_vMax = new Text("Maximale Geschwindigkeit:     " + vMax, 20, 240);
        addObject(text_vMax, 380, 395);
        int dichte_n = (int) Math.round(dichte_global);
        text_dichte = new Text("Anzahl der Autos:   " + anzahlAutos + "    (Dichte: " + dichte_n + "%)", 20, 280);
        addObject(text_dichte, 383, 495);
        text_p = new Text("Trödelwahrscheinlichkeit:   " + p + "% ", 20, 250);
        addObject(text_p, 380, 595);

        if(nasch == true){
            text_p2 = new Text("für stehende Autos: siehe oben", 20, 235);
        }
        if(nasch == false){
            text_p2 = new Text("für stehende Autos:   " + p2 + "% ", 20, 190);
        }
        addObject(text_p2, 374, 695);

        if(diagramme == true){ // erstellt Beschriftung der Achsen
            addObject(new Text("Zeit t", 14, 80), 1085, 745);
            addObject(new Text("Zeit t", 14, 80), 1085+350, 745);
            addObject(new Text("v", 14, 30), 1085+390, 1200);
            addObject(new Text("Anzahl", 14, 80), 1095+45, 780);
            addObject(new Text("d. Autos", 14, 80), 1085+45, 800);
            addObject(new Text(0), 786, 590);
            addObject(new Text(1), 786+350, 590);
            addObject(new Text(0), 786, 590+170);
            addObject(new Text(1), 1110, 890);
        }

        double totalSpeed = 0;
        for (int i = 0; i < anzahlAutos; i++){
            totalSpeed = totalSpeed + auto[i].getSpeed();
        }   
        double avgSpeed = totalSpeed/anzahlAutos;
        avgSpeedAnzeige = new Text(""+ avgSpeed, 30);
        addObject(avgSpeedAnzeige, 540, 300); 
        addObject(button_start, 390, 770);
    }

    public void addPfeile(){
        //Diagramm 1
        pfx1 = new Pfeil("x"); 
        addObject(pfx1, 948, 710);
        pfy1 = new Pfeil("y");
        addObject(pfy1, 800, 635);
        pfy1.turn(270);
        //Diagramm 2
        pfx2 = new Pfeil("x"); 
        addObject(pfx2, 948+350, 710);
        pfy2 = new Pfeil("y");
        addObject(pfy2, 800+350, 635);
        pfy2.turn(270);       
        //Diagramm 3
        pfx3 = new Pfeil("x"); 
        addObject(pfx3, 948, 880);
        pfy3 = new Pfeil("y");
        addObject(pfy3, 800, 805);
        pfy3.turn(270);
        //Diagramm 4
        pfx4 = new Pfeil("x"); 
        addObject(pfx4, 948+350, 880);
        pfy4 = new Pfeil("y");
        addObject(pfy4, 800+350, 805);
        pfy4.turn(270);

        point1 = new Point[140];
        point2 = new Point[140];
        point3 = new Point[140];
        point4 = new Auto[140][70];

    }

    public void avgSpeedRefresh(){
        double totalSpeed = 0;
        for (int i = 0; i < anzahlAutos; i++){
            if(auto[i] != null){
                totalSpeed = totalSpeed + auto[i].getSpeed();
            }
        }      
        removeObject(avgSpeedAnzeige); 
        double avgSpeed = totalSpeed/anzahlAutos; // Neuberechnung der Durchschnittsgeschwindigkeit
        avgSpeedAnzeige = new Text(" "+ avgSpeed, 30);
        addObject(avgSpeedAnzeige, 540, 300);        
    }  

    public void drawDiagramm(){ //oberes Diagramm
        int höhe = höheDiagramm;
        if((5 + pxl * diagrammLine) > höhe){
            for(int i = 0; i < 700/pxl; i++){ //entfernen der obersten Zeile, falls unten kein Platz mehr
                if(table[0][i] != null){
                    removeObject(table[0][i]);
                }
            }

            for(int z = 1; z < (höhe/pxl)-1; z++){
                for(int s = 0; s < 700/pxl; s++){
                    if(table[z][s] != null){
                        int x = table[z][s].getX();
                        int y = table[z][s].getY()-pxl;
                        table[z][s].setLocation(x, y);
                    }
                }
            }

            Auto[][] table_n = new Auto[höhe/pxl][700/pxl];
            for(int z = 0; z <((höhe/pxl)-1); z++){
                for(int s = 0; s < 700/pxl; s++){
                    if(table[z+1][s] != null){
                        table_n[z][s] = table[z+1][s];
                    }                
                }            
            }
            table = table_n;
            diagrammLine--;
        }
        if((1 + pxl * diagrammLine) < height){
            int[] auto_row = new int[700/pxl];
            int anzAutosProZ = 700/pxl;  // anzahl der Autos pro Zeile
            if(anzAutosProZ > anzahlZellen){
                anzAutosProZ = anzahlZellen;
            }
            for(int z = 0; z < anzAutosProZ; z++){
                if(zelle[z].getGefüllt() == true){
                    for(int i = 0; i < anzahlAutos; i++){
                        if(auto[i] != null){
                            if(zelle[z].getX() == auto[i].getX() && zelle[z].getY() == auto[i].getY()){
                                auto_row[z] = auto[i].getSpeed();
                                break;
                            }
                        }
                    }            
                } else {
                    auto_row[z] = -1;
                }
            }    

            for(int i = 0; i < anzAutosProZ; i++){
                if(auto_row[i] >= 0){
                    Auto a = new Auto(vMax, p, auto_row[i]);
                    table[diagrammLine][i] = a;
                    addObject(table[diagrammLine][i], 800 + pxl * i, 3 + pxl * diagrammLine);
                    table[diagrammLine][i].setColorPxl();
                }
            }
            diagrammLine++;
        }
    }

    public void addButtons(){
        dichte_more = new Button(1);
        addObject(dichte_more, 550, 470);
        dichte_less = new Button(-1);
        addObject(dichte_less, 210, 470);
        dichte_more_10 = new Button(10);
        addObject(dichte_more_10, 590, 470);
        dichte_less_10 = new Button(-10);
        addObject(dichte_less_10, 170, 470);

        p_more = new Button(1);
        addObject(p_more, 540, 570);
        p_less = new Button(-1);
        addObject(p_less, 220, 570);
        p_more_10 = new Button(10);
        addObject(p_more_10, 580, 570);
        p_less_10 = new Button(-10);
        addObject(p_less_10, 180, 570);

        vMax_more = new Button(1);
        addObject(vMax_more, 540, 370);
        vMax_less = new Button(-1);
        addObject(vMax_less, 220, 370);

        if(nasch == false){
            p2_more = new Button(1);
            addObject(p2_more, 510, 670);
            p2_less = new Button(-1);
            addObject(p2_less, 250, 670);
            p2_more_10 = new Button(10);
            addObject(p2_more_10, 550, 670);
            p2_less_10 = new Button(-10);
            addObject(p2_less_10, 210, 670);
        }
    }

    public void checkButtons(){
        if((button_start.isClicked() == true) && (start == false)){
            start = true;
            removeObject(button_start);
            addObject(button_pause, 390, 770);
        } 
        if((button_pause.isClicked() == true) && (start == true)){
            start = false;
            removeObject(button_pause);
            addObject(button_start, 390, 770);
        } 

        if(dichte_more.isClicked() == true){
            if(anzahlAutos < anzahlZellen){
                if(auto[anzahlAutos] == null && anzahlAutos < anzahlZellen){
                    auto[anzahlAutos] = new Auto(vMax, p);

                    //zufälliger Zelle wird berechnet
                    int z = Greenfoot.getRandomNumber(anzahlZellen);
                    while(zelle[z].getGefüllt() == true){
                        z = Greenfoot.getRandomNumber(anzahlZellen);
                    }           

                    if(zelle[z].getGefüllt() == false){
                        zelle[z].setGefüllt(true);
                        addObject(auto[anzahlAutos], zelle[z].getX(), zelle[z].getY());
                        for(int k = z; k >= z-vMax; k--){
                            int m = k;
                            if(m < 0){ //verhindert, dass auto[-1] gecheckt wird (vorallem wichtig für dichte_more_10)
                                m = m+anzahlZellen;
                            } 
                            if(zelle[m].getGefüllt() == true){
                                for(int i = 0; i < anzahlAutos; i++){ //verhindert direkten zusammenstoß nach spawn
                                    if((auto[i].getX() == zelle[m].getX()) && (auto[i].getY() == zelle[m].getY())){
                                        auto[i].setSpeed(0);
                                    }
                                }
                            }
                        }

                    }

                    anzahlAutos++;
                }
                //Textfeld aktualisieren
                removeObject(text_dichte);
                int dichte_n = (int) Math.round(anzahlAutos*100/anzahlZellen);
                text_dichte = new Text("Anzahl der Autos:   " + anzahlAutos + "    (Dichte: " + dichte_n + "%)", 20, 280);
                addObject(text_dichte, 383, 495);
            }
        }
        if(anzahlAutos > 1){
            if(dichte_less.isClicked() == true){
                for(int z = 0; z < anzahlZellen; z++){
                    if((auto[anzahlAutos-1].getX() == zelle[z].getX()) && (auto[anzahlAutos-1].getY() == zelle[z].getY())){
                        zelle[z].setGefüllt(false);
                    }
                }
                removeObject(auto[anzahlAutos-1]);
                auto[anzahlAutos-1] = null;
                anzahlAutos--;
                //Textfeld aktualisieren
                removeObject(text_dichte);
                int dichte_n = (int) Math.round(anzahlAutos*100/anzahlZellen);
                text_dichte = new Text("Anzahl der Autos:   " + anzahlAutos + "    (Dichte: " + dichte_n + "%)", 20, 280);
                addObject(text_dichte, 383, 495);
            }
        }
        if(dichte_more_10.isClicked() == true){
            int w = 10;
            if(anzahlAutos == anzahlZellen){ 
                w = 0;
            } else if(anzahlAutos+10 > anzahlZellen){
                w = anzahlZellen - anzahlAutos - 1;
                // w = 1;
            }
            for(int j = 0; j < w; j++){
                if(auto[anzahlAutos] == null && anzahlAutos < anzahlZellen){
                    auto[anzahlAutos] = new Auto(vMax, p, 0);

                    //zufällige Zelle wird berechnet
                    int z = Greenfoot.getRandomNumber(anzahlZellen);
                    while(zelle[z].getGefüllt() == true){
                        z = Greenfoot.getRandomNumber(anzahlZellen);
                    }  

                    if(zelle[z].getGefüllt() == false){
                        zelle[z].setGefüllt(true);
                        addObject(auto[anzahlAutos], zelle[z].getX(), zelle[z].getY());
                        for(int k = z; k >= z-vMax; k--){ // autos hinter dem neuen auto bekommen v = 0
                            int m = k;
                            if(m < 0){
                                m = m+anzahlZellen;
                            } 
                            if(zelle[m].getGefüllt() == true){
                                for(int i = 0; i < anzahlAutos; i++){
                                    if((auto[i].getX() == zelle[m].getX()) && (auto[i].getY() == zelle[m].getY())){
                                        auto[i].setSpeed(0);
                                    }
                                }
                            }
                        }

                    }
                    anzahlAutos++;
                }
            }
            //Textfeld aktualisieren
            removeObject(text_dichte);
            int dichte_n = (int) Math.round(anzahlAutos*100/anzahlZellen);
            text_dichte = new Text("Anzahl der Autos:   " + anzahlAutos + "    (Dichte: " + dichte_n + "%)", 20, 280);
            addObject(text_dichte, 383, 495);
        }
        if(dichte_less_10.isClicked() == true){
            int w = 10;
            if(anzahlAutos ==1){
                w = 0;
            } else if(anzahlAutos <= 10){
                w = anzahlAutos - 1;
            }            
            for(int j = 0; j < w; j++){
                for(int z = 0; z < anzahlZellen; z++){
                    if((auto[anzahlAutos-1].getX() == zelle[z].getX()) && (auto[anzahlAutos-1].getY() == zelle[z].getY())){
                        zelle[z].setGefüllt(false);
                    }
                }
                removeObject(auto[anzahlAutos-1]);
                auto[anzahlAutos-1] = null;
                anzahlAutos--;
            }
            //Textfeld aktualisieren
            removeObject(text_dichte);
            int dichte_n = (int) Math.round(anzahlAutos*100/anzahlZellen);
            text_dichte = new Text("Anzahl der Autos:   " + anzahlAutos + "    (Dichte: " + dichte_n + "%)", 20, 280);
            addObject(text_dichte, 383, 495);
        }

        if(p_more.isClicked() == true){
            removeObject(text_p);
            p++;
            if(p > 100){
                p = 100;
            }
            text_p = new Text("Trödelwahrscheinlichkeit:   " + p + "% ", 20, 250);
            addObject(text_p, 380, 595);
        }
        if(p_less.isClicked() == true){
            removeObject(text_p);
            p--;
            if(p < 0){
                p = 0;
            }
            text_p = new Text("Trödelwahrscheinlichkeit:   " + p + "% ", 20, 250);
            addObject(text_p, 380, 595);
        }
        if(p_more_10.isClicked() == true){
            removeObject(text_p);
            p = p + 10;
            if(p > 100){
                p = 100;
            }
            text_p = new Text("Trödelwahrscheinlichkeit:   " + p + "% ", 20, 250);
            addObject(text_p, 380, 595);
        }
        if(p_less_10.isClicked() == true){
            removeObject(text_p);
            p = p - 10;
            if(p < 0){
                p = 0;
            }
            text_p = new Text("Trödelwahrscheinlichkeit:   " + p + "% ", 20, 250);
            addObject(text_p, 380, 595);
        }
        if(vMax_more.isClicked()){
            removePoints();
            removeObject(text_vMax);
            vMax++;
            for(int i = 0; i < anzahlAutos; i++){
                auto[i].setVMax(vMax);
            }
            text_vMax = new Text("Maximale Geschwindigkeit:     " + vMax, 20, 240);
            addObject(text_vMax, 380, 395);
        }
        if(vMax_less.isClicked()){
            removePoints();
            removeObject(text_vMax);
            vMax--; 
            if(vMax < 0){
                vMax = 0;
            }
            for(int i = 0; i < anzahlAutos; i++){
                auto[i].setVMax(vMax);
                if(auto[i].getSpeed() > vMax){
                    auto[i].setSpeed(vMax);
                }
            }
            text_vMax = new Text("Maximale Geschwindigkeit:     " + vMax, 20, 240);
            addObject(text_vMax, 380, 395);
        }

        if(nasch == false){
            if(p2_more.isClicked() == true){
                removeObject(text_p2);
                p2++;
                if(p2 > 100){
                    p2 = 100;
                }
                text_p2 = new Text("für stehende Autos:   " + p2 + "% ", 20, 190);
                addObject(text_p2, 374, 695);
            }
            if(p2_less.isClicked() == true){
                removeObject(text_p2);
                p2--;
                if(p2 < 0){
                    p2 = 0;
                }
                text_p2 = new Text("für stehende Autos:   " + p2 + "% ", 20, 190);
                addObject(text_p2, 374, 695);
            }
            if(p2_more_10.isClicked() == true){
                removeObject(text_p2);
                p2 = p2 + 10;
                if(p2 > 100){
                    p2 = 100;
                }
                text_p2 = new Text("für stehende Autos:   " + p2 + "% ", 20, 190);
                addObject(text_p2, 374, 695);
            }
            if(p2_less_10.isClicked() == true){
                removeObject(text_p2);
                p2 = p2 - 10;
                if(p2 < 0){
                    p2 = 0;
                }
                text_p2 = new Text("für stehende Autos:   " + p2 + "% ", 20, 190);
                addObject(text_p2, 374, 695);
            }
        }

    }

    public void drawDiagramms(){
        if(data.getXWert() >=140){
            data.setXWert(data.getXWert()-1);
            moveDiagramm();
        }

        if((data.getRunde()%1) == 0){
            double dichte = calculateDichte();
            double vAvg = calculateVAvg();
            double flow = calculateVerkehrsfluss(dichte, vAvg);

            durchschnittlVerkehrsfluss(flow);
            // System.out.println("Flow "+flow+"    Dichte "+dichte+"    VAVG "+vAvg);
            drawDiagramm1(flow);
            drawDiagramm2(dichte);
            if((data.getXWert()) > 50){ //Verhindert, dass in den ersten Runden "verfälschte" Werte eingezeichnet werden
                drawDiagramm3(flow, dichte);
            }
            drawDiagramm4();

            data.setXWert(data.getXWert()+1);
        }

        data.setRunde(data.getRunde()+1);
    }

    public double calculateDichte(){
        double dichte = 0;
        int m = 100; // zellen, die gemessen werden
        if(anzahlZellen < 100){
            m = anzahlZellen;
        }
        for(int z = 0; z < m; z++){
            if(zelle[z].getGefüllt() == true){
                dichte++;
            }
        }
        dichte = dichte/m;
        return dichte;
    }

    public double calculateVAvg(){ //berechnet Durchschnittsgeschwindigkeit in den ersten 100 Zellen
        double anz = 0; // Anzahl der Autos in diesen 100 zellen
        double vGes = 0; // alle Geschwindigkeiten aller Autos in diesen 100 Zellen zusammen addiert
        int m = 100; // zellen, die gemessen werden
        if(anzahlZellen < 100){
            m = anzahlZellen;
        }
        for(int z = 0; z < m; z++){
            for(int i = 0; i < anzahlAutos; i++){
                if(auto[i] != null){
                    if((zelle[z].getX() == auto[i].getX()) && (zelle[z].getY() == auto[i].getY())){
                        anz++; 
                        vGes = vGes + auto[i].getSpeed();
                    }
                }
            }        
        }
        double vAvg = vGes/anz;
        return vAvg;
    }

    public double calculateVerkehrsfluss(double d, double v){
        double flow = v * d;
        return flow;
    }

    public void drawDiagramm1(double flow){
        int i = data.getXWert();
        int y = (int) Math.round(flow*100);
        point1[i] = new Point();
        addObject(point1[i], 802 + (2*data.getXWert()), 710 - (y));  
    }

    public void drawDiagramm2(double d){
        int i = data.getXWert();
        int y = (int) Math.round(d*100);
        point2[i] = new Point();
        addObject(point2[i], 1152 + (2*data.getXWert()), 710 - (y));  
    }

    public void drawDiagramm3(double f, double d){
        int i = data.getXWert();
        int x = (int) Math.round(d*100);
        int y = (int) Math.round(f*100);
        // System.out.println(y);
        point3[i] = new Point();
        addObject(point3[i], 802 + (2*x), 880 - (1*y));
        if(point3[i].checkDoubles()){ //löscht den Punkt wieder, wenn bereits ein anderer an gleicher Stelle vorhanden ist
            removeObject(point3[i]);
        }
        // System.out.println(point3[i].checkDoubles());
    }

    public void drawDiagramm4(){
        if(anzahlAutos > 0){
            int balkenbreite = (int) Math.round(140/(vMax+1));
            if(vMax < 12){
                balkenbreite = 10;
            }
            removePoints();
            for(int v = 0; v <= vMax; v++){ //geht alle Geschwindigkeiten durch und erstellt so die Balken
                int anzahl = 0;
                for(int i = 0; i < anzahlAutos; i++){ //sucht alle Autos mit besagter Geschwindigkeit
                    if(auto[i].getSpeed() == v){
                        anzahl++;
                    }
                }
                int höhe = (int) Math.round(70*anzahl/anzahlAutos);

                for(int x = v*balkenbreite; x < (v+1)*balkenbreite; x++){
                    for(int y = 0; y < höhe; y++){  
                        point4[x][y] = new Auto(vMax, p, v);
                        // System.out.println("" + x + "|" + y);
                        point4[x][y].setColorPxl();
                        addObject(point4[x][y], 1152+2*x, 879-2*y);
                    }

                }
            }
        }
    }

    public void moveDiagramm(){ //bewegt Punkte der Diagramme 1 und 2
        //Diagramm1
        Point[] p1 = new Point[140];
        removeObject(point1[0]);
        for (int s = 0; s < 139; s++){
            p1[s] = point1[s+1];
            point1[s+1].setLocation(point1[s+1].getX()-2, point1[s+1].getY());
        }
        point1 = p1;    

        //Diagramm2
        Point[] p2 = new Point[140];
        removeObject(point2[0]);
        for (int s = 0; s < 139; s++){
            p2[s] = point2[s+1];
            point2[s+1].setLocation(point2[s+1].getX()-2, point2[s+1].getY());
        }
        point2 = p2; 
    }

    public void removePoints(){ //löscht die Punkt des 4. Diagramms
        for(int x = 0; x < 140; x++){
            for(int y = 0; y < 70; y++){
                if(point4[x][y] != null){
                    removeObject(point4[x][y]);
                }
            }        
        }
    }

    public void durchschnittlVerkehrsfluss(double flow){
        if(data.getRunde() > 300){
            double f1 = flow * 100;
            int f = (int) Math.round(f1);
            data.addFlowValue(f);
        }
    }
}

