package com.draxvel.simpleblog.data.source.usersData;

import android.net.Uri;

public interface IUsers {

    interface CurrentUserExistsCallback{

        void onNotExists();
    }
    void isCurrentUserExists(CurrentUserExistsCallback currentUserExistsCallback);

    interface LoadCurrentUserCallback{

        void OnUploaded(final String name, final String image);
        void onFailure(final String msg);
    }
    void loadCurrentUser(LoadCurrentUserCallback loadCurrentUserCallback);

    interface UpdateCurrentUserCallback{
        void onUploaded();
        void onFailure(final String msg);
    }
    void updateCurrentUser(String imageUrl, String userName, UpdateCurrentUserCallback updateCurrentUserCallback);

    interface GetUserByIdCallBack{
        void onGet(final String name, final String image);
    }
    void getUserById(final String userId, GetUserByIdCallBack getUserByIdCallBack);


    interface SaveNewProfileImageCallback{
        void onUploaded(String url);
        void onFailure(final String msg);
    }
    void saveNewProfileImage(Uri mainImageURI, SaveNewProfileImageCallback saveNewProfileImageCallback);

}
