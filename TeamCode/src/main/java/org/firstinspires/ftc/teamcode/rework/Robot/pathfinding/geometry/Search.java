package org.firstinspires.ftc.teamcode.rework.Robot.pathfinding.geometry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;



public class Search {

    private final double ROBOT_RADIUS = 0;
    private Vector target;

    private HashSet<Vector> vectors = new HashSet<>();
    private Vector targetVector;

    private ArrayList<Vector> targets = new ArrayList<>();
    public Search() {
        target = new Vector(0, 0);
       
    }

    public void searchAStar(Vector pos) {
        setTargetVector(target);
        HashSet<Vector> closed = new HashSet<>();
        PriorityQueue<Vector> open = new PriorityQueue<>();
        cleanTree();
        Vector start = new Vector(pos);
        addVector(start);
        start.calculateCosts(targetVector);
        open.add(start);
        while (!open.isEmpty()) {
            Vector current = open.poll();
            closed.add(current);
            if (current.equals(targetVector)) {
                targets.add(current);
                Vector parent;
                while ((parent = current.parent) != null) {
                    targets.add(0, parent);
                    current = parent;
                }
                open.clear();
            } else {
                for (Vector vector : current.neighbors) {
                    if (closed.contains(vector)) {
                        continue;
                    }
                    if (!open.contains(vector)) {
                        open.add(vector);
                    }
                    vector.update(current);
                }
            }
        }
        for (int i = 0; i < targets.size(); i++) {
            System.out.println("x: " + targets.get(i).x + " y: " + targets.get(i).y);
        }
        removeVector(start);
    }

  private boolean inLineOfSight(Vector v1, Vector v2) { // TODO: Add some kind of playing field
        return false;
  }

    /**
     * Is there a direct path between here and the other one?
     * @param n
     */
  private void constrainedAdd(Vector n) {
        for (Vector vector : vectors) {
            if (inLineOfSight(n, vector)) {
                vector.neighbors.add(n);
                n.neighbors.add(vector);
            }
        }
        targets.add(n);
    }
    private void addVector(Vector n) {
        constrainedAdd(n);
    }

    private void removeVector(Vector n) {

        vectors.remove(n);
        for (Vector vector : vectors) {
            vector.neighbors.remove(n);
        }
    }

    public void setTargetVector(Vector v) {
        removeVector(targetVector);
        targetVector = new Vector();
        addVector(targetVector);
        for (Vector vector : vectors) {
            vector.calculateCosts(targetVector);
        }
    }

    private void cleanTree() {
        for (Vector vector : vectors) {
            vector.g = Double.POSITIVE_INFINITY;
        }
    }

    public HashSet<Vector> getVectors() {
        return vectors;
    }

    public static void main(String[] argv) {
      Search search = new Search();
      search.addVector(new Vector(0, 0));
      search.addVector(new Vector(3, 6));
      search.addVector(new Vector(1, 9));
      search.addVector(new Vector(7, 2));
      search.addVector(new Vector(4, 4));
      search.setTargetVector(new Vector(0, 0));
      search.searchAStar(new Vector(5,5));


    }
}
