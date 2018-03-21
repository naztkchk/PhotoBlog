package com.draxvel.simpleblog.ui.settings;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.draxvel.simpleblog.data.source.usersData.IUsers;
import com.draxvel.simpleblog.data.source.usersData.Users;

public class SettingsPresenter implements ISettingsPresenter{

    private Uri mainImageURI = null;
    private boolean isProfileChanged = false;

    private Users users;

    private ISettingsView iSettingsView;

    SettingsPresenter(ISettingsView iSettingsView){
        this.iSettingsView = iSettingsView;

        users = new Users();

        iSettingsView.setEnabledSaveBtn(false);

        loadStoredInfo();
    }

    void setMainImageURI(Uri mainImageURI) {
        this.mainImageURI = mainImageURI;
    }

    void setProfileChanged(boolean profileChanged) {
        isProfileChanged = profileChanged;
    }

    private void loadStoredInfo() {
        iSettingsView.setVisibleProgressBar(true);

        users.loadCurrentUser(new IUsers.LoadCurrentUserCallback() {
            @Override
            public void OnUploaded(String name, String image) {

                mainImageURI = Uri.parse(image);

                iSettingsView.setName(name);
                iSettingsView.setImage(image);

                iSettingsView.setVisibleProgressBar(false);
                iSettingsView.setEnabledSaveBtn(true);
            }

            @Override
            public void onFailure(String msg) {
                iSettingsView.showError(msg);

                iSettingsView.setVisibleProgressBar(false);
                iSettingsView.setEnabledSaveBtn(true);
            }
        });
    }

    @Override
    public void updateInfo(final String userName) {

        if (!TextUtils.isEmpty(userName) && mainImageURI != null) {
            iSettingsView.setVisibleProgressBar(true);

            if (isProfileChanged) {
                users.saveNewProfileImage(mainImageURI, new IUsers.SaveNewProfileImageCallback() {
                    @Override
                    public void onUploaded(String url) {
                       updateCurrentUser(url, userName);
                    }

                    @Override
                    public void onFailure(String msg) {
                        iSettingsView.showError(msg);
                    }
                });

            } else {
                updateCurrentUser(mainImageURI.toString(), userName);
            }

            iSettingsView.setVisibleProgressBar(false);

        }else {
            iSettingsView.showError("Enter info!");
        }
    }


    private void  updateCurrentUser(String imageUrl, String userName){

        users.updateCurrentUser(imageUrl, userName, new IUsers.UpdateCurrentUserCallback() {
            @Override
            public void onUploaded() {
                iSettingsView.showError("The user settings are updated");
                iSettingsView.showMainActivity();
            }

            @Override
            public void onFailure(String msg) {
                iSettingsView.showError(msg);
            }
        });
    }
}
