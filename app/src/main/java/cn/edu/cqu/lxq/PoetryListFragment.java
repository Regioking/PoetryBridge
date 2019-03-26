package cn.edu.cqu.lxq;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PoetryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PoetryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PoetryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //从数据库取数据存进这个list中
    private List<Poetry> poetryList = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PoetryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PoetryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PoetryListFragment newInstance(String param1, String param2) {
        PoetryListFragment fragment = new PoetryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        poetryList = LitePal.findAll(Poetry.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poetry_list, container, false);
        // Inflate the layout for this fragment
//        String[] testdata = {"静夜思","将进酒","行路难","石壕吏","静夜思","将进酒","行路难","石壕吏"
//        ,"静夜思","将进酒","行路难","石壕吏"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(PoetryListFragment.this.getContext(),
//                android.R.layout.simple_list_item_1,testdata);
//        ListView listView =(ListView)view.findViewById(R.id.poetryList);
//        listView.setAdapter(adapter);


//        for (Poetry poetry: poetryList){
//            Log.d("PoetryListFragment test",poetry.getChineseTitle());
//        }

//////////////这是listView的部分 ，现在改用recyclerView
//        PoetryAdapter poetryAdapter =
//                new PoetryAdapter(PoetryListFragment.this.getContext(),R.layout.poetrylist_item,poetryList);
//        ListView listView =(ListView)view.findViewById(R.id.poetryList);
//        listView.setAdapter(poetryAdapter);


        ////////////这是recyclerView
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclePoetryAdapter adapter = new RecyclePoetryAdapter(poetryList);
        recyclerView.setAdapter(adapter);

        return view;
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
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
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
