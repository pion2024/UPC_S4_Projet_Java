package model.entity;

import model.physic.Direction;

public class Cable extends Items {
    public enum State {
        VERTICAL,
        HORIZONTAL,
    }
    private State state;
    private boolean isActivated;
    public Cable(Switch sw, State state, Direction dir) {
        super(true, CellType.CABLE, dir);
        this.isActivated = sw.getIsPressed();
        this.state = state;
    }

    public boolean getIsActivated() {
        return this.isActivated;
    }

    public void setIsActivated(boolean newIsActivated) {
        this.isActivated = newIsActivated;
    }

    // public ArrayList<State> cableManagement(Switch sw, Bridge bridge, Board board) {
    //     if (sw.getDir().getDi() == bridge.getDir().getDi() && sw.getDir().getDj() >= bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDj() - 1 ; i > bridge.getDir().getDj() ; i--) {
    //             this.board.setItem(sw.getDir().getDi(), i, this);
    //         }
    //     }
    //     if (sw.getDir().getDi() == bridge.getDir().getDi() && sw.getDir().getDj() <= bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDj() + 1 ; i < bridge.getDir().getDj() ; i++) {
    //             this.board.setItem(sw.getDir().getDi(), i, this);
    //         }
    //     }
    //     if (sw.getDir().getDi() >= bridge.getDir().getDi() && sw.getDir().getDj() == bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDi() - 1 ; i > bridge.getDir().getDi() ; i--) {
    //             this.board.setItem(i, sw.getDir().getDj(), this);
    //         }
    //     }
    //     if (sw.getDir().getDi() <= bridge.getDir().getDi() && sw.getDir().getDj() >= bridge.getDir().getDj()){
    //         for (int i = sw.getDir().getDj() + 1 ; i < bridge.getDir().getDj() ; i++) {
    //             this.board.setItem(i, sw.getDir().getDj(), this);
    //         }
    //     }
    // }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onSteppedOn'");
    }
}
