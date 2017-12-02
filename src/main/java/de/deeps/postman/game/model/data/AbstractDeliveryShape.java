package de.deeps.postman.game.model.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiConsumer;

public abstract class AbstractDeliveryShape {

    //constants
    @Getter private static final int MATRIX_SIZE = 3, ROTATIONS_UNTIL_ORIGIN = 4;

    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE) private boolean[][] matrix;

    //initialization
    protected AbstractDeliveryShape() {
        setMatrix(new boolean[getMATRIX_SIZE()][getMATRIX_SIZE()]);
        formShape();
    }

    protected abstract void formShape();

    void enableTile(int row, int column){
        getMatrix()[row][column] = true;
    }

    //conditionals
    private boolean isEnabled(int row, int column){
        return getMatrix()[row][column];
    }

    //convenience
    /**
     * This method rotates the matrix clockwise around the center position [1, 1].
     * The result gets stored into the object´s matrix field.
     */
    public void rotateClockwise(){
        boolean[][] rotatedMatrix = new boolean[getMATRIX_SIZE()][getMATRIX_SIZE()];
        for (int iRow = 0; iRow < getMATRIX_SIZE(); iRow++){
            for (int iColumn = 0; iColumn < getMATRIX_SIZE(); iColumn++){
                rotatedMatrix[iColumn][getMATRIX_SIZE() - 1 - iRow] = getMatrix()[iRow][iColumn];
            }
        }
        setMatrix(rotatedMatrix);
    }

    /**
     * This method rotates the matrix anti-clockwise around the center position [1, 1], by rotating three times clockwise.
     * The result gets stored into the object´s matrix field.
     */
    public void rotateAntiClockwise(){
        for (int iRotate = 0; iRotate < getROTATIONS_UNTIL_ORIGIN() - 1; iRotate++){
            rotateClockwise();
        }
    }

    void forEachEnabledCellDo(BiConsumer<Integer, Integer> action){
        for (int iRow = 0; iRow < getMATRIX_SIZE(); iRow++){
            for (int iColumn = 0; iColumn < getMATRIX_SIZE(); iColumn++){
                if (isEnabled(iRow, iColumn)){
                    action.accept(iRow, iColumn);
                }
            }
        }
    }

}
