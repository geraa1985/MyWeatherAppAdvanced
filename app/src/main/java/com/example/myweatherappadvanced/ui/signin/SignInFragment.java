package com.example.myweatherappadvanced.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myweatherappadvanced.MainActivity;
import com.example.myweatherappadvanced.databinding.FragmentSignInBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignInFragment extends Fragment {

    FragmentSignInBinding binding;
    private static final int RC_SIGN_IN = 111;
    private static final String TAG = "GoogleAuth";
    private GoogleSignInClient googleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.getFab().setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        binding.signInButton.setOnClickListener(v -> signIn());
        binding.singOutButton.setOnClickListener(v-> signOut());
    }

    @Override
    public void onStart() {
        super.onStart();

        enableSign();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireActivity());
        if (account != null) {
            disableSign();
            updateUI(account.getEmail());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(requireActivity(), task -> {
                    SignInFragment.this.updateUI("email");
                    SignInFragment.this.enableSign();
                });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            disableSign();
            assert account != null;
            updateUI(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updateUI(String idToken) {
        binding.token.setText(idToken);
    }

    private void enableSign(){
        binding.singOutButton.setEnabled(true);
        binding.singOutButton.setEnabled(false);
    }

    private void disableSign(){
        binding.singOutButton.setEnabled(false);
        binding.singOutButton.setEnabled(true);
    }

}
