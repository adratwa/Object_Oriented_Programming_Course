package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrassField extends WorldMap implements IPositionChangeObserver{

    private int numberOfGrass;

    public List<Grass> getGrassList() {
        return grassList;
    }

    private List<Grass> grassList;

    public GrassField(int numberOfGrass) {
        this.grassList = new ArrayList<>();
        this.numberOfGrass = numberOfGrass;
        for (int i=1; i <= numberOfGrass;i++ ){
            createGrass();
        }
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        // if animal steps on the grass, the grass vanishes and appears in new free place
        if (this.isOccupiedByGrass(position)) {
            this.grassList.removeIf(grass -> grass.isAt(position));
            createGrass();

        }
        return (this.objectAt(position) instanceof Grass || this.objectAt(position) == null);

    }


    public boolean isOccupiedByGrass(Vector2d position) {
        for (Grass grass : this.grassList) {
            if (grass.isAt(position) ) { return true;}
        }
        return false;
    }


    @Override
    public Object objectAt(Vector2d position) {
        Object object = super.objectAt(position);
        if (object == null) {
            for (Grass grass : this.grassList) {
                if (grass.isAt(position) ) { return grass;}
            }
        }

        return object;
    }

    // to jest niedeterministyczne, to nie jest dobry pomysl
    // moznaby to zorbic za pomoca collections shuffle
    public void createGrass() {
        Random random = new Random();
        while (true) {
            int x = random.nextInt((int) (Math.sqrt(this.numberOfGrass * 10) - 0));
            int y = random.nextInt((int) (Math.sqrt(this.numberOfGrass * 10) - 0));
            Vector2d vector = new Vector2d(x, y);
            Grass grass = new Grass(new Vector2d(x, y));
            if (!this.isOccupied(vector)) {
                this.grassList.add(grass);
                break;
            }
        }
    }

    @Override
    public Vector2d calculateUpperBound() {

        Vector2d upperBound = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);

        for (Grass grass : this.grassList) {
            upperBound = upperBound.upperRight(grass.getPositionBunchOfGrass());
        }
        for (Vector2d key: this.animals.keySet()) {
            upperBound = upperBound.upperRight(key);
        }

        return upperBound;

    }

    @Override
    public Vector2d calculateLowerBound() {

        Vector2d lowerBound = new Vector2d(Integer.MAX_VALUE, Integer.MAX_VALUE);

        for (Grass grass : this.grassList) {
            lowerBound = lowerBound.lowerLeft(grass.getPositionBunchOfGrass());
        }

        for (Vector2d key: this.animals.keySet()) {
            lowerBound = lowerBound.lowerLeft(key);
        }

        return lowerBound;

    }


}
