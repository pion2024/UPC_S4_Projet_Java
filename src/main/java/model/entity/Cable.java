package model.entity;

import java.util.ArrayList;

import model.board.Board;

public class Cable extends Items {
    private enum State {
        VERTICAL,
        HORIZONTAL;
    }
    private ArrayList<State> states;
    private boolean isActivated;
    private Board board;
    public Cable(Switch sw, Bridge bridge, Board board) {
        super(true, CellType.CABLE);
        this.isActivated = sw.getIsPressed();
        cableManagement(sw, bridge, board);
    }

    public void cableManagement(Switch sw, Bridge bridge, Board board) {
        if (sw.getDir().getDi() == bridge.getDir().getDi() && sw.getDir().getDj() >= bridge.getDir().getDj()){
            for (int i = sw.getDir().getDj() - 1 ; i > bridge.getDir().getDj() ; i--) {
                this.board.setItem(sw.getDir().getDi(), i, this);
            }
        }
        if (sw.getDir().getDi() == bridge.getDir().getDi() && sw.getDir().getDj() <= bridge.getDir().getDj()){
            for (int i = sw.getDir().getDj() + 1 ; i < bridge.getDir().getDj() ; i++) {
                this.board.setItem(sw.getDir().getDi(), i, this);
            }
        }
        if (sw.getDir().getDi() >= bridge.getDir().getDi() && sw.getDir().getDj() == bridge.getDir().getDj()){
            for (int i = sw.getDir().getDi() - 1 ; i > bridge.getDir().getDi() ; i--) {
                this.board.setItem(i, sw.getDir().getDj(), this);
            }
        }
        if (sw.getDir().getDi() <= bridge.getDir().getDi() && sw.getDir().getDj() >= bridge.getDir().getDj()){
            for (int i = sw.getDir().getDj() + 1 ; i < bridge.getDir().getDj() ; i++) {
                this.board.setItem(i, sw.getDir().getDj(), this);
            }
        }
    }

    @Override
    public void onSteppedOn(MovableEntity stepper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onSteppedOn'");
    }
}
