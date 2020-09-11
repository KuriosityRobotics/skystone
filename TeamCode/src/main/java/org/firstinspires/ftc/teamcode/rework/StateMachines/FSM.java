package org.firstinspires.ftc.teamcode.rework.StateMachines;

import android.util.Log;

public class FSM {


    String[] states;
    String[] transitions;
    public String[][] stateTable;
    String currentState;

    public FSM(String[] states, String[] transitions, String[][] stateTable, String currentState) {
        this.states = states;
        this.transitions = transitions;
        this.stateTable = stateTable;
        this.currentState = currentState;
    }

    public FSM(String[] states, String[] transitions, String[][] stateTable) {
        this.states = states;
        this.transitions = transitions;
        this.stateTable = stateTable;
        this.currentState = states[0];
    }

    public void registerTransition(String transition){

        int stateNum = getOrdinal(currentState, states);
        int transitionNum = getOrdinal(transition, transitions);

        if (stateNum == -1 || transitionNum == -1){
            return;
        }

        if (stateTable[stateNum][transitionNum] != null){
            Log.i("FSM", "-----------");

            Log.i("FSM", "old" + currentState);
            Log.i("FSM", "transition " + transition);

            String nextState = stateTable[stateNum][transitionNum];
            currentState = nextState;

            Log.i("FSM", "new " + currentState);
        }
    }

    private int getOrdinal(String s, String[] strings){
        for (int i = 0; i < strings.length; i++){
            if (s.equals(strings[i])){
                return i;
            }
        }
        return -1;
    }
}
