package com.example.nicole.fitness_predictor;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "xData";
    private static final String ARG_PARAM2 = "yData";
    private static final String ARG_PARAM3 = "t";
    private static final String ARG_PARAM4 = "yLabel";
    private static final String ARG_PARAM5 = "xLabel";

//    private double[] xAxisData;
//    private double[] yAxisData;
//    private String title;
//    private String yAxisLabel;
//    private String xAxisLabel;

    //SAMPLE DATA, delete later
    private double[] xAxisData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private double[] yAxisData = {14.5, 13.8, 12.9, 14.2, 14.0, 13.2, 13.7, 14.2, 15.2, 14.8};
    private String title = "Average Speed vs. Day";
    private String yAxisLabel = "Average speed";
    private String xAxisLabel = "Day";

    private OnFragmentInteractionListener mListener;

    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param xData Parameter 1.
     * @param yData Parameter 2.
     * @param t Parameter 3.
     * @param yLabel Parameter 4
     * @param xLabel Parameter 5
     * @return A new instance of fragment GraphFragment.
     */
    public static GraphFragment newInstance(double[] xData, double[] yData, String t, String yLabel, String xLabel) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putDoubleArray(ARG_PARAM1, xData);
        args.putDoubleArray(ARG_PARAM2, yData);
        args.putString(ARG_PARAM3, t);
        args.putString(ARG_PARAM4, yLabel);
        args.putString(ARG_PARAM5, xLabel);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //set instance variables for graph
            xAxisData = getArguments().getDoubleArray(ARG_PARAM1);
            yAxisData = getArguments().getDoubleArray(ARG_PARAM2);
            title = getArguments().getString(ARG_PARAM3);
            yAxisLabel = getArguments().getString(ARG_PARAM4);
            xAxisLabel = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph, container, false);

        //List of data points obtained from GraphFragment initialization
        ArrayList<DataPoint> datalist = new ArrayList<DataPoint>();

        for(int i = 0; i < xAxisData.length; i++){
            datalist.add(new DataPoint (xAxisData[i], yAxisData[i]));
        }
        DataPoint[] data = new DataPoint[datalist.size()];
        data = datalist.toArray(data);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data);

        GraphView graph = (GraphView) v.findViewById(R.id.graph);

        //Style graph
        series.setColor(Color.rgb(251, 177, 60));
        series.setDrawDataPoints(true);
        series.setThickness(8);

        graph.setTitle(title);
        graph.setTitleTextSize(60);
        graph.getGridLabelRenderer().setHorizontalAxisTitle(xAxisLabel);
        graph.getGridLabelRenderer().setVerticalAxisTitle(yAxisLabel);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(getMax()+5);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(8);

        //scrollable on x axis
        graph.getViewport().setScrollable(true);

        graph.addSeries(series);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private int getMax() {
        if (yAxisData.length > 0) {
            int max = (int) yAxisData[0];

            for (int i = 1; i < yAxisData.length; i++) {
                if (yAxisData[i] > max)
                    max = (int) yAxisData[i];
            }

            return max;
        } else {
            // Not sure what a good default here would be ?
            return 10;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}