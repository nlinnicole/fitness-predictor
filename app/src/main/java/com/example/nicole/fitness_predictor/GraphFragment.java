package com.example.nicole.fitness_predictor;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;


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
    private static final String ARG_PARAM1 = "xAxisData";
    private static final String ARG_PARAM2 = "yAxisData";

    //private double[] xAxisData;
    //private double[] yAxisData;

    //SAMPLE DATA, delete later
    private double[] xAxisData = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private double[] yAxisData = {14.5, 13.8, 12.9, 14.2, 14.0, 13.2, 13.7, 14.2, 15.2, 14.8};

    private OnFragmentInteractionListener mListener;

    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param xAxisData Parameter 1.
     * @param yAxisData Parameter 2.
     * @return A new instance of fragment GraphFragment.
     */
    public static GraphFragment newInstance(double[] xAxisData, double[] yAxisData) {
        GraphFragment fragment = new GraphFragment();
        Bundle args = new Bundle();
        args.putDoubleArray(ARG_PARAM1, xAxisData);
        args.putDoubleArray(ARG_PARAM2, yAxisData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            xAxisData = getArguments().getDoubleArray(ARG_PARAM1);
            yAxisData = getArguments().getDoubleArray(ARG_PARAM2);
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

        //Style graph
        series.setTitle("Fitness Graph");
        series.setColor(Color.BLUE);
        series.setDrawDataPoints(true);
        series.setThickness(8);

        GraphView graph = (GraphView) v.findViewById(R.id.graph);
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
