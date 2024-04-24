package ncolrod.socialfut.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ncolrod.socialfut.R;
import ncolrod.socialfut.data.RetrofitRepository;
import ncolrod.socialfut.utils.BackendComunication;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    RetrofitRepository retrofitService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        //BackendComunication.getRetrofitService().login();
        // TODO: Use the ViewModel
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

}