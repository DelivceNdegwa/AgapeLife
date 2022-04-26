package com.example.agapelife.user_ui.user_appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.agapelife.databinding.FragmentUserAppointmentsBinding;

public class UserAppointmentsFragment extends Fragment {

    private UserAppointmentsViewModel userAppointmentsViewModel;
    private FragmentUserAppointmentsBinding binding;

    Button signOut;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userAppointmentsViewModel =
                new ViewModelProvider(this).get(UserAppointmentsViewModel.class);

        binding = FragmentUserAppointmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //signOut = binding.signOut;

//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}