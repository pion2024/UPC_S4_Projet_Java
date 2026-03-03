package model;

import java.util.ArrayList;
import model.entity.Bridge;
import model.entity.Propulsor;
import model.entity.Switch;

public class GameModel {
    public class Connection {

        Bridge bridge;
        Propulsor propulsor;
        ArrayList<Switch> switches;
        public Connection(Bridge bridge, ArrayList<Switch> switches){
            this.bridge = bridge;
            this.switches = switches;
        }
        public Connection(Propulsor propulsor, ArrayList<Switch> switches){
            this.propulsor = propulsor;
            this.switches = switches;
        }

        //vérifie que la liste en paramètre corresponde à la liste du pont/propulseur
        public boolean checkIfConnected(ArrayList<Switch> switchesToCheck) {
            if (this.switches.isEmpty()) return false;
            if (this.switches.size() != switchesToCheck.size()) return false;
            else {
                for (int i = 0 ; i < this.switches.size() ; i++) {
                    if (this.switches.get(i) == switchesToCheck.get(i)) return false;
                }
                return true;
            }
        }

        public boolean areSwitchesPressed() {
            for (Switch s : switches) {
                if (!s.getIsPressed()) return false;
            }
            return true;
        }

        /**
         * décommenter la partie commenté lorsque la fonction toggle sera implémenté dans Propulsor
         * @param switchesToCheck switches vérifiant si la liste appartient au pont/prolseur et si ils sont activé
         */
        public void activate(ArrayList<Switch> switchesToCheck) {
            if (checkIfConnected(switchesToCheck)) {
                boolean active = areSwitchesPressed();
            
                if (bridge != null) {
                    bridge.setActivated(active);
                }
                if (propulsor != null) {
                    propulsor.setActivated(active);
                }
            }
        }
    }
}
