package com.svalero.musicandroid.presenter;

import com.svalero.musicandroid.contract.UserLoginContract;
import com.svalero.musicandroid.domain.TokenResponse;
import com.svalero.musicandroid.domain.User;
import com.svalero.musicandroid.model.UserLoginModel;

public class UserLoginPresenter implements UserLoginContract.Presenter, UserLoginContract.Model.OnLoginUserListener{
    private UserLoginContract.Model model;
    private UserLoginContract.View view;

    public UserLoginPresenter(UserLoginContract.View view){
        this.view = view;
        this.model = new UserLoginModel();
    }
    @Override
    public void onLoginUserSuccess(TokenResponse token) {
        view.getSessionToken(token);
    }

    @Override
    public void onLoginUserError(String message) {
        view.showErrorMessage(message);
    }

    @Override
    public void loginUser(User user) {
        model.loginUser(user, this);
    }
}
