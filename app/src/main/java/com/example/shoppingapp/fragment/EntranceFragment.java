package com.example.shoppingapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntranceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EntranceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EntranceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EntranceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EntranceFragment newInstance(String param1, String param2) {
        EntranceFragment fragment = new EntranceFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewScreen = inflater.inflate(R.layout.fragment_entrance, container, false);

        /* what we want to do
           1. find the buttons for display */
        Button buttonReg= viewScreen.findViewById(R.id.entranceFrag_signUpButton);
        Button buttonLogIn= viewScreen.findViewById(R.id.entranceFrag_loginButton);

        //2. listener func- what to do when click on the two
        buttonReg.setOnClickListener(v -> Navigation.findNavController(viewScreen)
                .navigate(R.id.action_entranceFragment_to_registerationFragment));

        buttonLogIn.setOnClickListener((v -> {

            // call the logInFunc that is in the MainActivity from this fragment
            MainActivity mainActivity= (MainActivity)getActivity();
            if (mainActivity != null)
                mainActivity.logInFunc(viewScreen);
        }));
        return  viewScreen;
    }
}