package model;

import java.util.ArrayList;
import java.util.List;

import model.board.Board;
import model.entity.Agent;
import model.entity.Bridge;
import model.entity.Cable;
import model.entity.Items;
import model.entity.Propulsor;
import model.entity.Switch;
import model.entity.Wall;
import model.physic.Position;

public class GameModel {

    private Board board;
    private List<Bridge> bridges;
    private List<Switch> switches;
    private List<Propulsor> propulsors;
    private List<List<Cable>> cables;
    private Agent player;

    public GameModel(int width, int height) {
        // init du monde avec du vide partout (bridges bloqués)
        Items[][] items = new Items[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                items[i][j] = new Wall(new Position(i, j));
            }
        }

        this.board = new Board(items);
        this.bridges = new ArrayList<>();
        this.switches = new ArrayList<>();
    }

    // ======= Méthodes de gestion =======

    public void addBridge(Bridge b) {
        this.bridges.add(b);
    }

    public void addSwitch(Switch s) {
        this.switches.add(s);
    }

    public void setPlayer(Agent player) {
        this.player = player;
        // ajouter le player aux entités mobiles du board
        this.board.getMovableEntities().add(player);
    }

    public Board getBoard() {
        return this.board;
    }

    public Agent getPlayer() {
        return this.player;
    }

    // ======= Boucle de mise à jour =======

    //le coeur de la boucle de jeu
    public void update() {
        // Les switches sont gérés par onEnter / onExit / onInteract => pas besoin de update ici
        

        // On update l'état des ponts
        for (Bridge bridge : bridges) {
            // if (checkIfConnected(bridge, switches)) {
                bridge.updateStatus();
            // }
        }

        for (Propulsor propulsor : propulsors) {
            // if (checkIfConnected(propulsor, switches)) {
                propulsor.updateStatus();
            // }
        }
    }



    // //vérifie que les ponts n'ont pas de switch en dehors de la liste de switch dans le niveau
    // public boolean checkIfConnected(Bridge bridge, List<Switch> switchesToCheck) {
    //     int res = 0;
    //     if (bridges.get(bridges.indexOf(bridge)).getHostSwitches().isEmpty() || switches.isEmpty()) return false;
    //     else {
    //         for (int i = 0 ; i < this.switches.size() ; i++) {
    //             if (bridge.getHostSwitches().get(res) == switchesToCheck.get(i)) {
    //                 res++;
    //             }
    //             if (res == bridge.getHostSwitches().size()) return true;
    //             if (i == switchesToCheck.size() && bridge.getHostSwitches().get(res) != switchesToCheck.get(i)) {
    //                 return false;
    //             }
    //         }
    //         return true;
    //     }
    // }

    // //vérifie que les propulseurs n'ont pas de switch en dehors de la liste de switch dans le niveau
    // public boolean checkIfConnected(Propulsor propulsor, List<Switch> switchesToCheck) {
    //     int res = 0;
    //     if (propulsors.get(propulsors.indexOf(propulsor)).getHostSwitches().isEmpty() || switches.isEmpty()) return false;
    //     else {
    //         for (int i = 0 ; i < this.switches.size() ; i++) {
    //             if (propulsor.getHostSwitches().get(res) == switchesToCheck.get(i)) {
    //                 res++;
    //             }
    //             if (res == propulsor.getHostSwitches().size()) return true;
    //             if (i == switchesToCheck.size() && propulsor.getHostSwitches().get(res) != switchesToCheck.get(i)) {
    //                 return false;
    //             }
    //         }
    //         return true;
    //     }
    // }

    //------------------------------------------------------------------------------

    // public void updateWithCable() {
    //     //active le cable si le switch est pressé/activé
    //     if (this.switches.isEmpty() || (this.bridges.isEmpty() && this.propulsors.isEmpty())) return;
    //     for (int i = 0 ; i < this.switches.size() ; i++) {
    //         if (this.switches.get(i).getIsPressed()) {
    //             for (int j = 0 ; j < this.cables.get(i).size() ; j++) {
    //                 this.cables.get(i).get(j).setIsActivated(true);
    //             }
    //         }
    //     }
    //     //active le pont si le cable est activé
    //     for (Bridge bridge : bridges) {
    //         for (int k = 0 ; k < this.cables.size() ; k++) {
    //             bridge.setActivated(this.cables.get(k).get(0).getIsActivated());
    //         }
    //     }
    // }

    //cablage (pour l'instant ne fait que des cablage en ligne ou en courbe)
    // public List<Cable> cableManagement(Switch sw, Bridge bridge, Board board) {
    //     List<Cable> cable = new ArrayList<>();
    //     if (sw.getDir().getDi() == bridge.getDir().getDi() && sw.getDir().getDj() >= bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDj() - 1 ; i > bridge.getDir().getDj() ; i--) {
    //             cable.add(new Cable(sw, State.VERTICAL, Direction.DOWN));
    //         }
    //     }
    //     if (sw.getDir().getDi() == bridge.getDir().getDi() && sw.getDir().getDj() <= bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDj() + 1 ; i < bridge.getDir().getDj() ; i++) {
    //             cable.add(new Cable(sw, State.VERTICAL, Direction.UP));
    //         }
    //     }
    //     if (sw.getDir().getDi() >= bridge.getDir().getDi() && sw.getDir().getDj() == bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDi() - 1 ; i > bridge.getDir().getDi() ; i--) {
    //             cable.add(new Cable(sw, State.HORIZONTAL, Direction.LEFT));
    //         }
    //     }
    //     if (sw.getDir().getDi() <= bridge.getDir().getDi() && sw.getDir().getDj() == bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDj() + 1 ; i < bridge.getDir().getDj() ; i++) {
    //             cable.add(new Cable(sw, State.HORIZONTAL, Direction.RIGHT));
    //         }
    //     }
    //     if (sw.getDir().getDi() <= bridge.getDir().getDi() && sw.getDir().getDj() >= bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDj() + 1 ; i < bridge.getDir().getDj() ; i++) {
    //             cable.add(new Cable(sw, State.HORIZONTAL, Direction.RIGHT));
    //         }
    //     }
    //     return cable;
    // }
}