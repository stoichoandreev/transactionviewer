package test.badoo.sniper.badootest.graph;

/**
 * Created by sniper on 1/8/16.
 */

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import test.badoo.sniper.badootest.constants.Preferences;
import test.badoo.sniper.badootest.constants.StaticData;

public class Graph {

    private static final String TAG = Graph.class.getCanonicalName();
    public static final int MY_CURRENCY_INDEX = 0;//0 is GBP index
    private static Graph mInstance;
    private static List<Vertex> vertexes;
    private static List<Edge> edges;
    private static String NODES_ARRAY [] = { Preferences.GBP,Preferences.USD, Preferences.CAD,Preferences.AUD,Preferences.EUR };

    private Graph(){}
//    private Graph(List<Vertex> vertexes, List<Edge> edges) {
//        this.vertexes = vertexes;
//        this.edges = edges;
//    }
    public static Graph getInstance(){
        if(mInstance == null){
            mInstance = new Graph();
            vertexes = new ArrayList<>();
            edges = new ArrayList<>();
            initNotes();
            initEdges();
        }

        return mInstance;
    }
    private static void initNotes() {
        for (int i = 0; i < NODES_ARRAY.length; i++) {
            Vertex location = new Vertex("Node_" + i, "Node_" + i);
            vertexes.add(location);
        }
    }
    private static void initEdges() {
        final int size = StaticData.getInstance().getRates().size();
        for (int i = 0; i < size; i++) {
            addLane(
                    ("Edge_"+i),
                    getNodeIndex(StaticData.getInstance().getRates().get(i).getFrom()),
                    getNodeIndex(StaticData.getInstance().getRates().get(i).getTo()),
                    StaticData.getInstance().getRates().get(i).getFloatRate());
        }
    }

    public List<Vertex> getVertexes() {
        return vertexes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void reset(){
        mInstance = null;
    }

    private static void addLane(String laneId, int sourceLocNo, int destLocNo,
                         float rate) {
        try {
            Edge lane = new Edge(laneId,vertexes.get(sourceLocNo), vertexes.get(destLocNo), rate);
            edges.add(lane);
        }catch (ArrayIndexOutOfBoundsException ex){
            Log.e(TAG,"Probably in our data set has new currency and we don't know about that!!!!");
        }
    }
    public static int getNodeIndex(String noteName){
        if (noteName.equals(Preferences.GBP)){
            return 0;
        }else if (noteName.equals(Preferences.USD)) {
            return 1;
        }else if(noteName.equals(Preferences.CAD)){
            return 2;
        }else if(noteName.equals(Preferences.AUD)){
            return 3;
        }else if(noteName.equals(Preferences.EUR)) {
            return 4;
        }else {
            return -1;
        }
    }
}
