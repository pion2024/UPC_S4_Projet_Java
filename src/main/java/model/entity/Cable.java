package model.entity;

import model.entity.Cable.CableSource;
import model.entity.Cable.CableTarget;
import model.physic.Direction;
import model.physic.Position;

public class Cable extends Items{
    private Position pos;
    private CableSource source;
    private CableTarget target;
    private boolean in;
    private boolean out;
    
    public Cable(CableSource source, Direction dir) {
        super(true, CellType.CABLE, dir);
        this.pos = new Position(source.getDi(), source.getDj());
        this.source = source;
        this.in = source.getIsPressed();
        this.out = this.in;
    }

    public Cable(Position pos, Direction dir, CableTarget target) {
        super(true, CellType.CABLE, dir);
        this.pos = new Position(pos.getI(), pos.getJ());
        this.target = target;
        this.in = false;
        this.out = this.in;
    }

    public Cable(Position pos, Direction dir) {
        super(true, CellType.CABLE, dir);
        this.pos = new Position(pos.getI(), pos.getJ());
        this.in = false;
        this.out = this.in;
    }

    //Getters
    public boolean getOutput() {
        return this.out;
    }

    public boolean isTraversable() {
        return traversable;
    }

    public CellType getType() {
        return type;
    }

    public Position getPos() {
        return this.pos;
    }

    public int getI() {
        return this.pos.getI();
    }

    public int getJ() {
        return this.pos.getJ();
    }

    //Setters

    public void setInput(Cable cable) {
        this.in = cable.getOutput();
        this.out = cable.getOutput();
    }

    public void setInput(boolean newInput) {
        this.in = newInput;
        this.out = newInput;
    }

    public void setInput(Cable cable1, Cable cable2) {
            this.in = (cable1.getOutput() && cable2.getOutput());
            this.out = this.in;
        }

        public void setInput(Cable cable1, Cable cable2, Cable cable3) {
            this.in = (cable1.getOutput() && cable2.getOutput() && cable3.getOutput());
            this.out = this.in;
        }

        public void updateStatus() {
            
        }


    @Override
    public void onSteppedOn(MovableEntity stepper) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onSteppedOn'");
    }

    public static class Intersection extends Items implements CableSource, CableTarget {
        private Cable input1;
        private Cable input2;
        private Cable input3;
        private Cable output;
        private Position pos;

        public Intersection(Position pos) {
            super(true, CellType.CABLE, null);
            this.pos = pos;
            this.input1 = null;
            this.input2 = null;
            this.input3 = null;
            this.output = null;
        }

        public int getI() {
            return this.pos.getI();
        }

        public int getJ() {
            return this.pos.getJ();
        }

        public int getDi() {
            return this.output.getDi();
        }

        public int getDj() {
            return this.output.getDj();
        }

        public boolean getIsPressed() {
            if (input3 == null) return this.input1.getOutput() && this.input2.getOutput();
            else return this.input1.getOutput() && this.input2.getOutput() && this.input3.getOutput();
        }

        public void addCable(Cable cable) {
            if (this.input1 == null) this.input1 = cable;
            else if (this.input1 != null && this.input2 == null) {
                this.input2 = cable;
                for (Direction dir : Direction.values()) {
                    if (dir != input1.getDir() && dir != input2.getDir()) {
                        this.output = new Cable(input1.getPos(), dir);
                        break;
                    }
                }
            } 
            else if (this.input1 != null && this.input2 != null && this.input3 == null) {
                this.input3 = cable;
                for (Direction dir : Direction.values()) {
                    if (dir != input1.getDir() && dir != input2.getDir()) {
                        this.output = new Cable(input1.getPos(), dir);
                        break;
                    }
                }
            }
        }

        public void updateStatus() {
            if (this.input1 != null && this.input2 != null && this.input3 == null) 
                this.output.setInput(input1, input2);
            else if (this.input1 != null && this.input2 != null && this.input3 != null)
                this.output.setInput(input1, input2, input3);
        }

        @Override
        public void onSteppedOn(MovableEntity stepper) {
            // ne fais rien 
        }
    }

    public interface CableSource {
        int getI();
        int getJ();
        int getDi();
        int getDj();
        boolean getIsPressed();
    }

    public interface CableTarget {
        int getI();
        int getJ();
        void addCable(Cable cable);
        void updateStatus();
    }
}
