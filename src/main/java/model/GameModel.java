//src/main/java/model/GameModel.java
package model;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.entity.Agent;
import model.entity.Block;
import model.entity.Bridge;
import model.entity.Items;
import model.entity.MovableEntity;
import model.entity.Propulsor;
import model.entity.Robot;
import model.entity.Switch;
import model.physic.Direction;


public class GameModel {
    private Items[][] map;
    private Block[] blocks;
    private List<Bridge> bridges;
    private List<Propulsor> propulsors;
    private List<Switch> switches;
    private Agent player; 
    private Robot robot;

    public GameModel(int width, int height) {
        // lire le map depuis l'instance de Level 
        this.bridges = new ArrayList<>();
        this.switches = new ArrayList<>();
    }

    public void addBridge(Bridge b) { this.bridges.add(b); }
    public void addSwitch(Switch s) { this.switches.add(s); }
    public void setPlayer(Agent player) { this.player = player; }
    public void setRobot(Robot robot) { this.robot = robot; }
    public Agent getPlayer() { return this.player; }
    public Robot getRobot() { return this.robot; }
    //le coeur de la boucle de jeu
    public void update() {
        //on check qui marche sur quoi
        for (Switch sw : switches) {
            boolean isPressed = false;
            // si on ajoutera plus de robots ou des entites mobiles alors on peut generaliser avec une liste 
            // un agent dessus suffit pour declencher 
            isPressed = player.getPos().equals(sw.getPos()) || robot.getPos().equals(sw.getPos());
            // il faut aussi considerer l'effet d'un bloc immobile placé dessus 
            for (Block block : blocks){
                if (block.getPos().equals(sw.getPos())){
                    isPressed = isPressed | true ; 
                    return;
                }
            }
            sw.updateStatus(isPressed);
        }
        // il suffit de parcourir les listes des entites mobiles, ici on a que deux, un robot un player 
        // donc meme pas besoin de parcourir une liste des entites mobiles dans GameModel
        // je comprends pas pourquoi vous voulez tout stocker dans Board alors c'est dans GameModel qu'on utilise
        // en plus, Board comme il est nommé, est dédié aux éléments immobiles. 
        //on update l'etat des ponts
        for (Bridge bridge : bridges) {
            bridge.updateStatus();
        }
    }
    public class Connection {
        Bridge bridge2;
        Propulsor propulsor2;
        ArrayList<Switch> switches2;
        public Connection(Bridge bridge, ArrayList<Switch> switches){
            this.bridge2 = bridge;
            this.switches2 = switches;
        }
        public Connection(Propulsor propulsor, ArrayList<Switch> switches){
            this.propulsor2 = propulsor;
            this.switches2 = switches;
        }

        //vérifie que la liste en paramètre corresponde à la liste du pont/propulseur
        public boolean checkIfConnected(ArrayList<Switch> switchesToCheck) {
            if (this.switches2.isEmpty()) return false;
            if (this.switches2.size() != switchesToCheck.size()) return false;
            else {
                for (int i = 0 ; i < this.switches2.size() ; i++) {
                    if (this.switches2.get(i) == switchesToCheck.get(i)) return false;
                }
                return true;
            }
        }

        public boolean areSwitchesPressed() {
            for (int i = 0; i < this.switches2.size(); i++) {
                    if (!this.switches2.get(i).getIsPressed()) return false;
                }
            return true;
        }

        /**
         * @param switchesToCheck switches vérifiant si la liste appartient au pont/prolseur et si ils sont activé
         */
        public void activate(ArrayList<Switch> switchesToCheck) {
            if (checkIfConnected(switchesToCheck)) {
                if (areSwitchesPressed()) {
                    if (this.bridge2 != null) this.bridge2.isActive();
                    else if (this.propulsor2 != null) this.propulsor2.getIsActivate();
                }
            }
        }
    }
}
